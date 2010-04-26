/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import ccc.api.Folders;
import ccc.api.Groups;
import ccc.api.Resources;
import ccc.api.ServiceLocator;
import ccc.api.Users;
import ccc.api.dto.ACL;
import ccc.api.dto.GroupDto;
import ccc.api.dto.PageDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.UserDto;
import ccc.api.dto.ACL.Entry;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.ResourceExistsException;
import ccc.api.types.DBC;
import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;
import ccc.api.types.ResourceName;
import ccc.commons.CharConversion;
import ccc.rest.extensions.ResourcesExt;
import ccc.services.Migration;

/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class BaseMigrations {
    private static Logger log = Logger.getLogger(BaseMigrations.class);

    private final ServiceLocator _services;

    private final Migration _migration;
    private final ResourcesExt _resourcesExt;

    private final LegacyDBQueries _legacyQueries;
    private final TemplateMigration _tm;
    private final String _linkPrefix;

    private Map<String, GroupDto> _cachedGroups =
        new HashMap<String, GroupDto>();

    private final Properties _paragraphTypes =
        ccc.commons.Resources.readIntoProps("paragraph-types.properties");





    /**
     * Constructor.
     */
    protected BaseMigrations(final ServiceLocator service,
                             final Migration pagesExt,
                             final ResourcesExt resourcesExt,
                             final LegacyDBQueries legacyQueries,
                             final TemplateMigration tm,
                             final String linkPrefix) {
        _services      = DBC.require().notNull(service);
        _migration      = DBC.require().notNull(pagesExt);
        _resourcesExt  = DBC.require().notNull(resourcesExt);
        _legacyQueries = DBC.require().notNull(legacyQueries);
        _tm            = DBC.require().notNull(tm);
        _linkPrefix    = DBC.require().notNull(linkPrefix);
    }


    protected LogEntryBean logEntryForVersion(final int id,
                                              final int version,
                                              final String action,
                                              final String username) {
        final LogEntryBean le =
            _legacyQueries.selectUserFromLog(id, version, action);

        if (null==le && version == 0) {
            final LogEntryBean fe = new LogEntryBean(0, new Date());
            final List<UserDto> users =
              getUsers()
                  .listUsers(username, null, null, null, null, null, null, 1, 1)
                  .getElements();
            fe.setUser(users.get(0));
            return fe;
        } else if (null == le) {
            throw new MigrationException(
                "Log entry missing: "+id+" v."+version+", action: "+action);
        }

        log.debug("Actor for "+id+" v."+version+" is "+le.getActor());

        final UserDto user = userForLegacyId(le.getActor());
        if (null == user) {
            throw new MigrationException("User missing: "+le.getActor());
        }
        le.setUser(user);


        return le;
    }


    private UserDto userForLegacyId(final int ccc6UserId) {
        try {
            return getUsers().userByLegacyId(""+ccc6UserId);
        } catch (final CCException e) {
            throw new RuntimeException(
                "User fetching failed with legacyID "+ccc6UserId, e);
        }
    }


    /**
     * Assemble a page delta from the specified CCC6 resource.
     *
     * @param r The CCC6 resource data.
     * @param version The version of the CCC6 page to duplicate.
     *
     * @return A page delta representing the CCC6 resource.
     */
    protected PageDto assemblePage(final ResourceBean r, final int version) {
        final Set<Paragraph> paragraphDeltas =
            new HashSet<Paragraph>();
        final Map<String, StringBuffer> paragraphs =
            assembleParagraphs(r.contentId(), version);
        for (final Map.Entry<String, StringBuffer> para
            : paragraphs.entrySet()) {

            final String name = para.getKey();
            final ParagraphType type = getParagraphType(name);
            final String value = para.getValue().toString();

            switch (type) {
                case TEXT:
                    paragraphDeltas.add(
                        Paragraph.fromText(name, value));
                    break;

                case NUMBER:
                    paragraphDeltas.add(
                        Paragraph.fromNumber(name, new BigDecimal(value)));
                    break;

                default:
                    throw new RuntimeException(
                        "Unsupported paragraph type: "+type);
            }
        }

        final PageDto delta = PageDto.delta(paragraphDeltas);

        return delta;
    }


    protected ResourceSummary createPage(final UUID parentFolderId,
                                         final ResourceBean r,
                                         final Integer version,
                                         final LogEntryBean le,
                                         final PageDto delta) {

        delta.setTitle(r.cleanTitle());
        delta.setComment(null);
        delta.setMajorChange(true);
        delta.setName(ResourceName.escape(r.name()));
        delta.setTemplate(null);

        ResourceSummary rs;
        try {
            rs = _migration.createPage(
                parentFolderId,
                delta,
                false,
                le.getUser().getId(),
                le.getHappenedOn());
        } catch (final EJBException e) {
            if (e.getCausedByException() instanceof ResourceExistsException) {
                delta.setName(ResourceName.escape(r.name()+"1"));
                rs = _migration.createPage(
                    parentFolderId,
                    delta,
                    false,
                    le.getUser().getId(),
                    le.getHappenedOn());
                log.warn("Renamed page '"+r.name()+"' to '"+r.name()+"1'.");
            } else {
                throw e;
            }
        }
        log.debug("Created page: "+r.contentId()+" v."+version);
        return rs;
    }


    protected void setResourceRoles(final ResourceBean r,
                                    final ResourceSummary rs,
                                    final LogEntryBean le) {
        if (r.isSecure()) {
            log.info(
                "Resource has security constraints "
                + r.contentId() + ", " + r.cleanTitle());

            final Collection<String> roles =
                _legacyQueries.selectRolesForResource(r.contentId());
            final Set<UUID> groupList =
                UserMigration.migrateGroups(roles, _cachedGroups, getGroups());
            final Set<Entry> groupEntries = new HashSet<Entry>();
            for (final UUID gId : groupList) {
                final Entry e = new Entry(); // FIXME: Read perm's from cc6?
                e._canRead = true;
                e._canWrite = true;
                e._principal = gId;
                groupEntries.add(e);
            }

            final Collection<Integer> users =
                _legacyQueries.selectUsersForResource(r.contentId());
            final Set<Entry> userList = new HashSet<Entry>();
            for (final Integer user : users) {
                final Entry e = new Entry(); // FIXME: Read perm's from cc6?
                e._canRead = true;
                e._canWrite = true;
                e._principal = userForLegacyId(user.intValue()).getId();
                userList.add(e);
            }

            final ACL acl =
                new ACL()
                    .setGroups(groupEntries)
                    .setUsers(userList);

            _migration.changeAcl(
                rs.getId(),
                acl,
                le.getUser().getId(),
                le.getHappenedOn());
        }
    }


    /**
     * Set the template for a resource.
     *
     * @param templateFolder The folder where the migration creates templates.
     * @param r The CCC6 resource.
     * @param rs The equivalent CCC7 resource.
     * @param le Audit details for the metadata change.
     */
    protected void setTemplateForResource(
                                      final ResourceBean r,
                                      final ResourceSummary rs,
                                      final LogEntryBean le,
                                      final ResourceSummary templateFolder) {
        final String templateName = r.displayTemplate();
        final String templateDescription = r.templateDescription();

        if (null == templateName) { // Resource has no template
            return;
        }

        final UUID templateId = _tm.getTemplate(
            new ResourceName(templateName),
            templateDescription,
            templateFolder);
        _migration.updateResourceTemplate(
            rs.getId(), templateId, le.getUser().getId(), le.getHappenedOn());
    }


    /**
     * Publish a resource.
     *
     * @param r The CCC6 resource.
     * @param rs The equivalent CCC7 resource.
     * @param le Audit details for the metadata change.
     */
    protected void publish(final ResourceBean r,
                           final ResourceSummary rs,
                           final LogEntryBean le) {
        if (r.isPublished()) {
            _migration.publish(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());
        }
    }


    /**
     * Set the metadata for a resource.
     *
     * @param r The CCC6 resource.
     * @param rs The equivalent CCC7 resource.
     * @param le Audit details for the metadata change.
     */
    protected void setMetadata(final ResourceBean r,
                               final ResourceSummary rs,
                               final LogEntryBean le) {

        final Map<String, String> metadata =
            new HashMap<String, String>();
        setStyleSheet(r, metadata);
        setFlagged(r, metadata);
        metadata.put("legacyId", ""+r.contentId());
        if (r.useInIndex() != null && !r.useInIndex().equals("")) {
            metadata.put("useInIndex", ""+r.useInIndex());
        }

        _migration.updateMetadata(
            rs.getId(),
            rs.getTitle(),
            rs.getDescription(),
            rs.getTags(),
            metadata,
            le.getUser().getId(),
            le.getHappenedOn());
    }


    private ParagraphType getParagraphType(final String paragraphName) {
        final String pType = _paragraphTypes.getProperty(paragraphName, "TEXT");
        return ParagraphType.valueOf(pType);
    }


    private Map<String, StringBuffer> assembleParagraphs(final int pageId,
                                                         final int version) {
        log.debug("Assembling paragraphs for "+pageId+" v."+version);

        final Map<String, StringBuffer> map =
            new HashMap<String, StringBuffer>();
        final List<ParagraphBean> paragraphs =
            _legacyQueries.selectParagraphs(pageId, version);

        for (final ParagraphBean p : paragraphs) {
            if (isEmpty(p)) { // ignore empty/null texts
                log.debug("Ignoring empty part for paragraph "+p.key());

            } else if (map.containsKey(p.key())) { // merge
                final StringBuffer sb = map.get(p.key());
                map.put(p.key(), sb.append(p.text()));
                log.debug("Appended to paragraph "+p.key());

            } else { // new item
                map.put(p.key(), new StringBuffer(p.text()));
                log.debug("Created paragraph "+p.key());
            }
        }
        log.debug("Assembly done.");

        new LinkFixer(_linkPrefix, String.valueOf(pageId)).extractURLs(map);
        new CharConversion().fix(map);

        checkDuplicateKeys(map);

        return map;
    }


    private boolean isEmpty(final ParagraphBean p) {
        return p.text() == null || p.text().equals("");
    }


    /**
     * Check that a paragraph map has no duplicate paragraphs.
     *
     * @param map The map to check.
     */
    final void checkDuplicateKeys(final Map<String, StringBuffer> map) {
        boolean hasDuplicates = false;
        for (final String key : map.keySet()) {
            for (final String possDuplicate : map.keySet()) {
                if (!key.equals(possDuplicate)
                    && key.equalsIgnoreCase(possDuplicate)) {
                    hasDuplicates = true;
                    log.warn(key+" is a duplicate of "+possDuplicate);
                }
            }
        }
        if (hasDuplicates) {
            throw new RuntimeException("Duplicate paragraphs found.");
        }
    }


    private void setStyleSheet(final ResourceBean r,
                               final Map<String, String> properties) {
        final String styleSheet =
            _legacyQueries.selectStyleSheet(r.contentId());
        if (styleSheet != null && !styleSheet.equals("")) {
            if (isValidMetadatum(styleSheet)) {
                properties.put("bodyId", styleSheet);
            } else {
                log.warn(
                    "Ignored invalid stylesheet '"
                    + styleSheet
                    + "' for page "
                    + r.contentId());
            }
        }
    }


    private void setFlagged(final ResourceBean r,
                            final Map<String, String> properties) {
        final String flagged = _legacyQueries.selectFlagged(r.contentId());
        if (flagged != null && flagged.equals("Y")) {
            properties.put("flagged", Boolean.TRUE.toString());
        }
    }


    private boolean isValidMetadatum(final String value) {
        return value.matches("[^<^>]*");
    }


    /**
     * Accessor.
     *
     * @return Returns the CCC7 users API.
     */
    protected final Users getUsers() {
        return _services.getUsers();
    }


    /**
     * Accessor.
     *
     * @return Returns the CCC7 pages API.
     */
    protected final Migration getMigrations() {
        return _migration;
    }


    /**
     * Accessor.
     *
     * @return Returns the CCC7 resources API.
     */
    protected final Resources getResources() {
        return _services.getResources();
    }


    /**
     * Accessor.
     *
     * @return Returns the CCC7 folders API.
     */
    protected final Folders getFolders() {
        return _services.getFolders();
    }


    /**
     * Accessor.
     *
     * @return Returns the extended CCC7 resources API.
     */
    protected final ResourcesExt getResourcesExt() {
        return _resourcesExt;
    }


    /**
     * Accessor.
     *
     * @return Returns the CCC7 groups API.
     */
    protected Groups getGroups() {
        return _services.getGroups();
    }


    /**
     * Accessor.
     *
     * @return Returns the legacyQueries.
     */
    protected final LegacyDBQueries getLegacyQueries() {
        return _legacyQueries;
    }


    /**
     * Accessor.
     *
     * @return Returns the tm.
     */
    protected final TemplateMigration getTm() {
        return _tm;
    }


    /**
     * Accessor.
     *
     * @return Returns the linkPrefix.
     */
    protected final String getLinkPrefix() {
        return _linkPrefix;
    }
}
