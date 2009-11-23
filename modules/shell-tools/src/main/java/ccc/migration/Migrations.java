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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import ccc.cli.Migrate.Options;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.Paragraph;

/**
 * Data migration from CCC6 to CCC7.
 *
 * @author Civic Computing Ltd
 */
public class Migrations extends BaseMigrations {
    private static Logger log = Logger.getLogger(Migrations.class);

    private final ResourceSummary _contentRoot;
    private final ResourceSummary _templateFolder;
    private final ResourceSummary _filesFolder;
    private final ResourceSummary _contentImagesFolder;
    private final ResourceSummary _assetsImagesFolder;
    private final ResourceSummary _cssFolder;

    private Set<Integer>    _menuItems;


    private final FoldersExt _foldersExt;

    private final boolean _migrateHomepage;
    private final boolean _migrateIsMajorEdit;
    private final boolean _migrateVersions;

    private final String _userName;

    private final List<String> _ignoreList;

    private final FileMigrator _fm;


    /**
     * Constructor.
     *
     * @param legacyQueries Queries
     * @param resourcesExt The available commands for CCC7.
     * @param fu The file up-loader to use.
     * @param pagesExt Pages API implementation.
     * @param foldersExt Folders API implementation.
     * @param userCommands Templates API implementation.
     * @param templates Templates API implementation.
     */
    public Migrations(final LegacyDBQueries legacyQueries,
                      final ResourcesExt resourcesExt,
                      final PagesExt pagesExt,
                      final FoldersExt foldersExt,
                      final Users userCommands,
                      final FileUploader fu,
                      final Templates templates,
                      final Options options
                      ) {
        _legacyQueries = legacyQueries;
        _resourcesExt = resourcesExt;
        _pagesExt = pagesExt;
        _foldersExt = foldersExt;
        _userCommands = userCommands;
        _linkPrefix = "/"+options.getApp()+"/";
        _migrateHomepage = options.isMigrateHomepage();
        _migrateIsMajorEdit = options.isMigrateIsMajorEdit();
        _migrateVersions = options.isMigrateVersions();
        _userName = options.getUsername();
        _ignoreList = new ArrayList<String>();

        final String ignore = options.getIgnorePaths();
        if (ignore != null && !ignore.trim().isEmpty()) {
            for (final String item : ignore.split(";")) {
                _ignoreList.add(item);
            }
        }

        try {
            _contentRoot = _resourcesExt.resourceForPath("/content");
            _filesFolder = _resourcesExt.resourceForPath("/content/files");
            _contentImagesFolder =
                _resourcesExt.resourceForPath("/content/images");

            _templateFolder =
                _resourcesExt.resourceForPath("/assets/templates");
            _assetsImagesFolder =
                _resourcesExt.resourceForPath("/assets/images");
            _cssFolder = _resourcesExt.resourceForPath("/assets/css");
        } catch (final RestException e) {
            throw new MigrationException(
                "Failed to retrieve default folder structure.", e);
        }

        _fm = new FileMigrator(fu, _legacyQueries, "files/", "images/", "css/");
        _um = new UserMigration(_legacyQueries, _userCommands);
        _tm = new TemplateMigration(_legacyQueries, templates);
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
        } catch (final RestException e) {
            log.error("Catastrophic failure.", e);
        }
    }

    private void migrateHomepages() throws RestException {
        final Map<Integer, Integer> map = _legacyQueries.homepages();
        for (final Entry<Integer, Integer> e : map.entrySet()) {
            final ResourceSummary f =
                _resourcesExt.resourceForLegacyId(""+e.getKey());
            final ResourceSummary hp =
                _resourcesExt.resourceForLegacyId(""+map.get(e.getKey()));
            if (f != null && hp != null) {
                _resourcesExt.lock(UUID.fromString(f.getId().toString()));
                _foldersExt.updateFolder(
                    f.getId(),
                    new FolderDelta(f.getSortOrder(), hp.getId(), null));
                _resourcesExt.unlock(f.getId());
            }
        }
        log.info("Migrated home page information of the folders.");
    }


    // TODO: Move under command-resourceDao?
    private void publishRecursive(final ResourceSummary resource)
                                                 throws RestException {
        _resourcesExt.lock(UUID.fromString(resource.getId().toString()));
        _resourcesExt.publish(resource.getId());
        if ("FOLDER".equals(resource.getType().name())) {
            final Collection<ResourceSummary> children =
                _foldersExt.getChildren(resource.getId());
            for (final ResourceSummary child : children) {
                publishRecursive(child);
            }
        }
        _resourcesExt.unlock(resource.getId());
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
            if (_ignoreList.contains(""+r.contentId())) {
                log.warn("Ignoring resource as requested: "+r.contentId()
                    + " "+r.name());
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
                r.contentId(),
                r.legacyVersion(),
                "CREATED FOLDER",
                _userName,
                log);

            final ResourceSummary rs = _foldersExt.createFolder(
                    parentFolderId,
                    r.name(),
                    r.title(),
                    false,
                    le.getUser().getId(),
                    le.getHappenedOn());
            log.debug("Created folder: "+r.contentId());

            _resourcesExt.lock(
                UUID.fromString(
                    rs.getId().toString()),
                    le.getUser().getId(),
                    le.getHappenedOn());
            setTemplateForResource(r, rs, le, _templateFolder);
            publish(r, rs, le);
            showInMainMenu(r, rs, le);
            setMetadata(r, rs, le);
            setResourceRoles(r, rs, le, log);
            _resourcesExt.unlock(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());

            migrateResources(rs.getId(), r.contentId());

        } catch (final Exception e) {
//          log.warn("Error migrating folder "+r.contentId(),  e);
            log.warn(logMigrationError("Error migrating folder ", r, e));
        }
    }


    private String logMigrationError(final String errorText, final ResourceBean r, final Exception e) {

        return errorText + r.contentId()+ ", " + r.cleanTitle() + ": "+e.getMessage();
    }

    private void migratePage(final UUID parentFolderId,
                             final ResourceBean resource) {

        try {
            // Query the versions of a page
            final List<Integer> paragraphVersions = determinePageVersions(resource);

            // Create the page
            LogEntryBean le = null;
            Integer createVersion = paragraphVersions.remove(0);
            while (le == null) {
                try {
                    le = logEntryForVersion(
                        resource.contentId(),
                        createVersion, "CREATED PAGE",
                        _userName,
                        log);
                } catch (final MigrationException e) {
                    log.warn("Skipped version "+createVersion+" of page "
                        +resource.contentId());
                    if (paragraphVersions.size() == 0) {
                        throw e;
                    }
                    createVersion = paragraphVersions.remove(0);
                }
            }

            PageDelta delta = assemblePage(resource, createVersion.intValue());
            final ResourceSummary rs =
                createPage(parentFolderId, resource, createVersion, le, delta);

            // Apply all updates
            for (final Integer version : paragraphVersions) {
                try {
                    le = logEntryForVersion(
                        resource.contentId(),
                        version,
                        "UPDATE",
                        _userName,
                        log);

                    _resourcesExt.lock(
                        UUID.fromString(
                            rs.getId().toString()),
                            le.getUser().getId(),
                            le.getHappenedOn());
                    try {
                        delta = assemblePage(resource, version);
                        updatePage(resource, rs, version, le, delta);
                    } catch (final MigrationException e) {
                        log.warn("Update skipped(inner) for version  "+version
                            +" of page "+resource.contentId());
                    }
                    _resourcesExt.unlock(
                        rs.getId(), le.getUser().getId(), le.getHappenedOn());
                } catch (final MigrationException e) {
                    log.warn("Update skipped for version "+version
                        +" of page "+resource.contentId());
                }
            }

            _resourcesExt.lock(
                UUID.fromString(
                    rs.getId().toString()),
                    le.getUser().getId(),
                    le.getHappenedOn());
            setTemplateForResource(resource, rs, le, _templateFolder);
            publish(resource, rs, le);
            showInMainMenu(resource, rs, le);

            for (final Paragraph paragraph : delta.getParagraphs()) {
                if ("Description_Custom".equals(paragraph.name())) {
                    rs.setDescription(paragraph.text());
                } else if ("Keywords_Custom".equals(paragraph.name())) {
                    rs.setTags(paragraph.text());
                }
            }

            setMetadata(resource, rs, le);
            setResourceRoles(resource, rs, le, log);
            _resourcesExt.unlock(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());

            log.debug("Migrated page "+resource.contentId());

        } catch (final RestException exception) {
            log.warn(logMigrationError("Error migrating page ", resource, exception));
        } catch (final RuntimeException exception) {
            log.warn(logMigrationError("Error migrating page ", resource, exception));
        }
    }


    private void showInMainMenu(final ResourceBean r,
                                final ResourceSummary rs,
                                final LogEntryBean le)
                                                 throws RestException {
        if (_menuItems.contains(Integer.valueOf(r.contentId()))) {
            _resourcesExt.includeInMainMenu(
                rs.getId(), true, le.getUser().getId(), le.getHappenedOn());
        }
    }


    private List<Integer> determinePageVersions(final ResourceBean r) {
        final List<Integer> only0 = new ArrayList<Integer>();
        only0.add(Integer.valueOf(0));

        if (!_migrateVersions) {
            return only0;
        }

        final List<Integer> paragraphVersions =
            _legacyQueries.selectParagraphVersions(r.contentId());
        log.debug("Page versions available: "+paragraphVersions);
        if (paragraphVersions.size() == 0) {
            log.warn("No versions found! Uses version 0 only for resource "+r.contentId());
            return only0;
        }


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
                            final PageDelta d) throws RestException {

        final String userComment =
            _legacyQueries.selectUserComment(r.contentId(), version);

        Boolean isMajorEdit = Boolean.valueOf(false);
        if (_migrateIsMajorEdit) {
            isMajorEdit =
                _legacyQueries.selectIsMajorEdit(r.contentId(), version);
        }
        _pagesExt.updatePage(
            rs.getId(),
            d,
            userComment,
            isMajorEdit.booleanValue(),
            le.getUser().getId(),
            le.getHappenedOn());

        log.debug("Updated page: "+r.contentId()+" v."+version);
    }

}
