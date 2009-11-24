/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.commons.Resources;
import ccc.commons.WordCharFixer;
import ccc.domain.CCCException;
import ccc.rest.RestException;
import ccc.rest.Users;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.FailureCode;
import ccc.types.Paragraph;
import ccc.types.ParagraphType;
import ccc.types.ResourceName;
import ccc.types.Username;

/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class BaseMigrations {
    private static Logger log = Logger.getLogger(BaseMigrations.class);

    protected  LegacyDBQueries _legacyQueries;
    protected Users _userCommands;
    protected UserMigration _um;
    protected PagesExt _pagesExt;
    protected ResourcesExt _resourcesExt;
    protected TemplateMigration _tm;
    protected String _linkPrefix;

    private final Properties _paragraphTypes =
        Resources.readIntoProps("paragraph-types.properties");


    @SuppressWarnings("unchecked")
    protected LogEntryBean logEntryForVersion(final int id,
                                              final int version,
                                              final String action,
                                              final String username,
                                              final Logger log) {
        final LogEntryBean le =
            _legacyQueries.selectUserFromLog(id, version, action);

        if (null==le && version == 0) {
            final LogEntryBean fe = new LogEntryBean(0, new Date());
            final List<UserDto> users = new ArrayList(
                _userCommands.listUsersWithUsername(new Username(username)));
            fe.setUser(users.get(0));
            return fe;
        } else if (null == le) {
            throw new MigrationException(
                "Log entry missing: "+id+" v."+version+", action: "+action);
        }

        log.debug("Actor for "+id+" v."+version+" is "+le.getActor());

        UserDto user = null;
        try {
            user = _userCommands.userByLegacyId(""+le.getActor());
        } catch (final RestException e) {
            throw new MigrationException(
                "User fetching failed with legacyID "+le.getActor(),e);
        }
        if (null == user) {
            throw new MigrationException("User missing: "+le.getActor());
        }
        le.setUser(user);


        return le;
    }


    protected PageDelta assemblePage(final ResourceBean r,
                                     final int version) {
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
                    throw new CCCException("Unsupported paragraph type: "+type);
            }
        }

        final PageDelta delta = new PageDelta(paragraphDeltas);

        return delta;
    }


    protected ResourceSummary createPage(final UUID parentFolderId,
                                       final ResourceBean r,
                                       final Integer version,
                                       final LogEntryBean le,
                                       final PageDelta delta)
                                                 throws RestException {

        final String pageTitle = r.cleanTitle();

        ResourceSummary rs;
        try {
            rs = _pagesExt.createPage(
                parentFolderId,
                delta,
                r.name(),
                false,
                null,
                pageTitle,
                le.getUser().getId(),
                le.getHappenedOn(),
                null,
                true);
        } catch (final RestException e) {
            if (FailureCode.EXISTS ==e.getCode()) {
                rs = _pagesExt.createPage(
                    parentFolderId,
                    delta,
                    r.name()+"1",
                    false,
                    null,
                    pageTitle,
                    le.getUser().getId(),
                    le.getHappenedOn(),
                    null,
                    true);
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
                                  final LogEntryBean le,
                                  final Logger log)
                                                 throws RestException {
        if (r.isSecure()) {
            log.info("Resource has security constraints " +r.contentId() + ", " + r.cleanTitle());
            _resourcesExt.changeRoles(
                rs.getId(),
                _legacyQueries.selectRolesForResource(r.contentId()),
                le.getUser().getId(),
                le.getHappenedOn());
        }
    }


    protected void setTemplateForResource(final ResourceBean r,
                                          final ResourceSummary rs,
                                          final LogEntryBean le,
                                          final ResourceSummary templateFolder)
                                                 throws RestException {
        final String templateName = r.displayTemplate();
        final String templateDescription = r.templateDescription();

        if (null == templateName) { // Resource has no template
            return;
        }

        final UUID templateId = _tm.getTemplate(
            new ResourceName(templateName),
            templateDescription,
            templateFolder);
        _resourcesExt.updateResourceTemplate(
            rs.getId(), templateId, le.getUser().getId(), le.getHappenedOn());
    }


    protected void publish(final ResourceBean r,
                         final ResourceSummary rs,
                         final LogEntryBean le) throws RestException {
        if (r.isPublished()) {
            _resourcesExt.publish(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());
        }
    }


    protected void setMetadata(final ResourceBean r,
                             final ResourceSummary rs,
                             final LogEntryBean le)
                                                 throws RestException {

        final Map<String, String> metadata =
            new HashMap<String, String>();
        setStyleSheet(r, metadata);
        setFlagged(r, metadata);
        metadata.put("legacyId", ""+r.contentId());
        if (r.useInIndex() != null) {
            metadata.put("useInIndex", ""+r.useInIndex());
        }

        _resourcesExt.updateMetadata(
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
            if (p.text() == null) { // ignore empty/null texts
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
        new WordCharFixer().warn(map);

        return map;
    }


    private void setStyleSheet(final ResourceBean r,
                               final Map<String, String> properties) {
        final String styleSheet =
            _legacyQueries.selectStyleSheet(r.contentId());
        if (styleSheet != null) {
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
}
