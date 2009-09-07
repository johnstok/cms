/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev: 220 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2008-08-07 15:22:12 +0100 (Thu, 07 Aug 2008) $
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ccc.commons.Resources;
import ccc.commons.WordCharFixer;
import ccc.domain.CCCException;
import ccc.rest.CommandFailedException;
import ccc.rest.Queries;
import ccc.rest.Users;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;
import ccc.rest.migration.Commands;
import ccc.rest.migration.Folders;
import ccc.rest.migration.Pages;
import ccc.types.FailureCode;
import ccc.types.Paragraph;
import ccc.types.ParagraphType;

/**
 * Data migration from CCC6 to CCC7.
 *
 * @author Civic Computing Ltd
 */
public class Migrations {
    private static Logger log = Logger.getLogger(Migrations.class);

    private final ResourceSummary _contentRoot;
    private final ResourceSummary _templateFolder;
    private final ResourceSummary _filesFolder;
    private final ResourceSummary _contentImagesFolder;
    private final ResourceSummary _assetsImagesFolder;
    private final ResourceSummary _cssFolder;

    private Set<Integer>    _menuItems;

    private final LegacyDBQueries _legacyQueries;
    private final Commands _commands;
    private final Pages _pages;
    private final Folders _folders;
    private final Users _userCommands;
    private final Queries _queries;
    private final String _linkPrefix;

    private final boolean _migrateHomepage;
    private final boolean _migrateIsMajorEdit;
    private final boolean _migrateVersions;

    private final FileMigrator _fm;
    private final UserMigration _um;
    private final TemplateMigration _tm;

    private final Properties _paragraphTypes =
        Resources.readIntoProps("paragraph-types.properties");


    /**
     * Constructor.
     *
     * @param legacyQueries Queries
     * @param linkPrefix The prefix to attach to legacy URLs.
     * @param commands The available commands for CCC7.
     * @param queries The available queries for CCC7.
     * @param fu The file up-loader to use.
     * @param migrateHomepage The boolean for home page migration.
     * @param migrateIsMajorEdit The boolean for is_major_edit migration.
     * @param migrateVersions The boolean for page versions migration.
     */
    public Migrations(final LegacyDBQueries legacyQueries,
                      final String linkPrefix,
                      final Commands commands,
                      final Pages pages,
                      final Folders folders,
                      final Users userCommands,
                      final Queries queries,
                      final FileUploader fu,
                      final boolean migrateHomepage,
                      final boolean migrateIsMajorEdit,
                      final boolean migrateVersions) {
        _legacyQueries = legacyQueries;
        _queries = queries;
        _commands = commands;
        _pages = pages;
        _folders = folders;
        _userCommands = userCommands;
        _linkPrefix = linkPrefix;
        _migrateHomepage = migrateHomepage;
        _migrateIsMajorEdit = migrateIsMajorEdit;
        _migrateVersions = migrateVersions;

        _contentRoot = _commands.resourceForPath("/content");
        _filesFolder = _commands.resourceForPath("/content/files");
        _contentImagesFolder = _commands.resourceForPath("/content/images");

        _templateFolder = _commands.resourceForPath("/assets/templates");
        _assetsImagesFolder = _commands.resourceForPath("/assets/images");
        _cssFolder = _commands.resourceForPath("/assets/css");

        _fm = new FileMigrator(fu, _legacyQueries, "files/", "images/", "css/");
        _um = new UserMigration(_legacyQueries, _userCommands);
        _tm = new TemplateMigration(_legacyQueries, _commands);
    }


    /**
     * Migrate to CCC7.
     */
    public void migrate() {
        try {
            loadSupportingData();
            _um.migrateUsers();
            migrateResources(_contentRoot.getId(), 0);
            _fm.migrateManagedFilesAndImages(
                _filesFolder, _contentImagesFolder);
            _fm.migrateImages(_assetsImagesFolder);
            _fm.migrateCss(_cssFolder);
            publishRecursive(_cssFolder);
            publishRecursive(_assetsImagesFolder);
            publishRecursive(_filesFolder);
            publishRecursive(_contentImagesFolder);
            if (_migrateHomepage) {
                migrateHomepages();
            }
        } catch (final CommandFailedException e) {
            log.error("Catastrophic failure.", e);
        }
    }

    private void migrateHomepages() throws CommandFailedException {
        final Map<Integer, Integer> map = _legacyQueries.homepages();
        for (final Entry<Integer, Integer> e : map.entrySet()) {
            final ResourceSummary f = _commands.resourceForLegacyId(""+e.getKey());
            final ResourceSummary hp =
                _commands.resourceForLegacyId(""+map.get(e.getKey()));
            if (f != null && hp != null) {
                _commands.lock(UUID.fromString(f.getId().toString()));
                _folders.updateFolder(
                    f.getId(),
                    new FolderDelta(f.getSortOrder(), hp.getId(), null));
                _commands.unlock(f.getId());
            }
        }
        log.info("Migrated home page information of the folders.");
    }


    // TODO: Move under command-resourceDao?
    private void publishRecursive(final ResourceSummary resource)
                                                 throws CommandFailedException {
        _commands.lock(UUID.fromString(resource.getId().toString()));
        _commands.publish(resource.getId());
        if ("FOLDER".equals(resource.getType().name())) {
            final Collection<ResourceSummary> children =
                _folders.getChildren(resource.getId());
            for (final ResourceSummary child : children) {
                publishRecursive(child);
            }
        }
        _commands.unlock(resource.getId());
    }


    private void loadSupportingData() {
        _menuItems = _legacyQueries.selectMenuItems();
    }


    private void migrateResources(final UUID parentFolderId,
                                  final int parent) {

        final List<ResourceBean> resources =
            _legacyQueries.selectResources(parent);

        for (final ResourceBean r : resources) {
            if (r.name() == null || r.name().trim().equals("")) {
                log.warn("Ignoring resource with missing name: "+r.contentId());
                continue;
            }

            if (r.type().equals("FOLDER")) {
                migrateFolder(parentFolderId, r);
            } else if (r.type().equals("PAGE")) {
                migratePage(parentFolderId, r);
            } else {
                log.warn("Ignoring unsupported type "
                    +r.type()+" for resource "+r.contentId());
            }
        }
    }

    private void migrateFolder(final UUID parentFolderId,
                               final ResourceBean r) {

        try {
            final LogEntryBean le = logEntryForVersion(
                r.contentId(), r.legacyVersion(), "CREATED FOLDER");

            final ResourceSummary rs = _folders.createFolder(
                    parentFolderId,
                    r.name(),
                    r.title(),
                    false,
                    le.getUser().getId(),
                    le.getHappenedOn());
            log.debug("Created folder: "+r.contentId());

            _commands.lock(
                UUID.fromString(rs.getId().toString()), le.getUser().getId(), le.getHappenedOn());
            setTemplateForResource(r, rs, le);
            publish(r, rs, le);
            showInMainMenu(r, rs, le);
            setMetadata(r, rs, le);
            setResourceRoles(r, rs, le);
            _commands.unlock(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());

            migrateResources(rs.getId(), r.contentId());

        } catch (final Exception e) {
//          log.warn("Error migrating folder "+r.contentId(),  e);
            log.warn("Error migrating folder "
                +r.contentId()+": "+e.getMessage());
        }
    }

    private void migratePage(final UUID parentFolderId,
                             final ResourceBean r) {

        try {
            // Query the versions of a page
            final List<Integer> paragraphVersions = determinePageVersions(r);

            // Create the page
            LogEntryBean le = null;
            Integer createVersion = paragraphVersions.remove(0);
            while (le == null) {
                try {
                    le = logEntryForVersion(
                        r.contentId(), createVersion, "CREATED PAGE");
                } catch (final MigrationException e) {
                    log.warn("Skipped version "+createVersion+" of page "
                        +r.contentId());
                    if (paragraphVersions.size() == 0) {
                        throw e;
                    }
                    createVersion = paragraphVersions.remove(0);
                }
            }

            PageDelta delta = assemblePage(r, createVersion.intValue());
            final ResourceSummary rs =
                createPage(parentFolderId, r, createVersion, le, delta);

            // Apply all updates
            for (final Integer version : paragraphVersions) {
                try {
                    le = logEntryForVersion(r.contentId(), version, "UPDATE");
                    delta = assemblePage(r, version);
                    updatePage(r, rs, version, le, delta);
                } catch (final MigrationException e) {
                    log.warn("Update skipped for version "+version
                        +" of page "+r.contentId());
                }
            }

            _commands.lock(
                UUID.fromString(rs.getId().toString()), le.getUser().getId(), le.getHappenedOn());
            setTemplateForResource(r, rs, le);
            publish(r, rs, le);
            showInMainMenu(r, rs, le);

            for (final Paragraph paragraph : delta.getParagraphs()) {
                if ("Description_Custom".equals(paragraph.name())) {
                    rs.setDescription(paragraph.text());
                } else if ("Keywords_Custom".equals(paragraph.name())) {
                    rs.setTags(paragraph.text());
                }

            }
            setMetadata(r, rs, le);
            setResourceRoles(r, rs, le);
            _commands.unlock(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());

            log.debug("Migrated page "+r.contentId());

        } catch (final Exception e) {
//            log.warn("Error migrating page "+r.contentId(),  e);
            log.warn(
                "Error migrating page " +r.contentId()+": "+e.getMessage());
        }
    }


    private void showInMainMenu(final ResourceBean r,
                                final ResourceSummary rs,
                                final LogEntryBean le)
                                                 throws CommandFailedException {
        if (_menuItems.contains(Integer.valueOf(r.contentId()))) {
            _commands.includeInMainMenu(
                rs.getId(), true, le.getUser().getId(), le.getHappenedOn());
        }
    }


    private List<Integer> determinePageVersions(final ResourceBean r) {

        if (!_migrateVersions) {
            final List<Integer> only0 = new ArrayList<Integer>();
            only0.add(Integer.valueOf(0));
            return only0;
        }

        final List<Integer> paragraphVersions =
            _legacyQueries.selectParagraphVersions(r.contentId());
        log.debug("Page versions available: "+paragraphVersions);

        if (-1 == paragraphVersions.get(0)) { // Discard working version
            paragraphVersions.remove(0);
            log.debug("Ignoring working copy for page: "+r.contentId());
        }

        if (0 != paragraphVersions.get(0)) { // Do version 0 last
            throw new MigrationException(
                "No 'current version' for page "+r.contentId());
        }

        paragraphVersions.remove(0);
        paragraphVersions.add(Integer.valueOf(0));

        log.debug(
            "Page "+r.contentId()
            +" contains "+paragraphVersions.size()+" versions.");
        return paragraphVersions;
    }


    private void updatePage(final ResourceBean r,
                            final ResourceSummary rs,
                            final int version,
                            final LogEntryBean le,
                            final PageDelta d)
                                                 throws CommandFailedException {

        _commands.lock(UUID.fromString(rs.getId().toString()), le.getUser().getId(), le.getHappenedOn());

        final String userComment =
            _legacyQueries.selectUserComment(r.contentId(), version);

        Boolean isMajorEdit = Boolean.valueOf(false);
        if (_migrateIsMajorEdit) {
            isMajorEdit =
                _legacyQueries.selectIsMajorEdit(r.contentId(), version);
        }
        _pages.updatePage(
            rs.getId(),
            d,
            userComment,
            isMajorEdit.booleanValue(),
            le.getUser().getId(),
            le.getHappenedOn());
        _commands.unlock(rs.getId(), le.getUser().getId(), le.getHappenedOn());

        log.debug("Updated page: "+r.contentId()+" v."+version);
    }


    private ResourceSummary createPage(final UUID parentFolderId,
                                       final ResourceBean r,
                                       final Integer version,
                                       final LogEntryBean le,
                                       final PageDelta delta)
                                                 throws CommandFailedException {

        final String pageTitle = r.cleanTitle();

        ResourceSummary rs;
        try {
            rs = _pages.createPage(
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
        } catch (final CommandFailedException e) {
            if (FailureCode.EXISTS ==e.getCode()) {
                rs = _pages.createPage(
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


    private void publish(final ResourceBean r,
                         final ResourceSummary rs,
                         final LogEntryBean le) throws CommandFailedException {
        if (r.isPublished()) {
            _commands.publish(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());
        }
    }

    private void setMetadata(final ResourceBean r,
                             final ResourceSummary rs,
                             final LogEntryBean le)
                                                 throws CommandFailedException {

        final Map<String, String> metadata =
            new HashMap<String, String>();
        setStyleSheet(r, metadata);
        setFlagged(r, metadata);
        metadata.put("legacyId", ""+r.contentId());
        if (r.useInIndex() != null) {
            metadata.put("useInIndex", ""+r.useInIndex());
        }

        _commands.updateMetadata(
            rs.getId(),
            rs.getTitle(),
            rs.getDescription(),
            rs.getTags(),
            metadata,
            le.getUser().getId(),
            le.getHappenedOn());
    }

    private void setResourceRoles(final ResourceBean r,
                                  final ResourceSummary rs,
                                  final LogEntryBean le)
                                                 throws CommandFailedException {
        if (r.isSecure()) {
            log.info("Resource "+r.contentId()+" has security constraints");
            _commands.changeRoles(
                rs.getId(),
                _legacyQueries.selectRolesForResource(r.contentId()),
                le.getUser().getId(),
                le.getHappenedOn());
        }
    }

    private void setStyleSheet(final ResourceBean r,
                               final Map<String, String> properties) {
        final String styleSheet =
            _legacyQueries.selectStyleSheet(r.contentId());
        if (styleSheet != null) {
            properties.put("bodyId", styleSheet);
        }
    }

    private void setFlagged(final ResourceBean r,
                            final Map<String, String> properties) {
        final String flagged = _legacyQueries.selectFlagged(r.contentId());
        if (flagged != null && flagged.equals("Y")) {
            properties.put("flagged", Boolean.TRUE.toString());
        }
    }

    private PageDelta assemblePage(final ResourceBean r,
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


    private ParagraphType getParagraphType(final String paragraphName) {
        final String pType = _paragraphTypes.getProperty(paragraphName, "TEXT");
        return ParagraphType.valueOf(pType);
    }


    private void setTemplateForResource(final ResourceBean r,
                                        final ResourceSummary rs,
                                        final LogEntryBean le)
                                                 throws CommandFailedException {
        final String templateName = r.displayTemplate();
        final String templateDescription = r.templateDescription();

        if (null == templateName) { // Resource has no template
            return;
        }

        final UUID templateId = _tm.getTemplate(
            templateName,
            templateDescription,
            _templateFolder);
        _commands.updateResourceTemplate(
            rs.getId(), templateId, le.getUser().getId(), le.getHappenedOn());
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

        new LinkFixer(_linkPrefix).extractURLs(map);
        new WordCharFixer().warn(map);

        return map;
    }


    private LogEntryBean logEntryForVersion(final int id,
                                            final int version,
                                            final String action) {
        final LogEntryBean le =
            _legacyQueries.selectUserFromLog(id, version, action);

        if (null==le) {
            throw new MigrationException(
                "Log entry missing: "+id+" v."+version+", action: "+action);
        }

        log.debug("Actor for "+id+" v."+version+" is "+le.getActor());

        final UserDto user =_um.getUser(le.getActor());
        le.setUser(user);

        if (null==user) {
            throw new MigrationException("User missing: "+le.getActor());
        }

        return le;
    }
}
