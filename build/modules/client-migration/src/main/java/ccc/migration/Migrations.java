/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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

import ccc.api.ServiceLocator;
import ccc.api.dto.FolderDto;
import ccc.api.dto.PageDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.exceptions.CCException;
import ccc.api.types.Paragraph;
import ccc.cli.Migrate.Options;
import ccc.rest.extensions.ResourcesExt;
import ccc.services.Migration;

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

    private final Options _options;

    private final FileMigrator _fm;
    private final UserMigration _um;


    /**
     * Constructor.
     *
     * @param legacyQueries Queries
     * @param resourcesExt The available commands for CCC7.
     * @param fu The file up-loader to use.
     * @param pagesExt Pages API implementation.
     * @param foldersExt Folders API implementation.
     */
    public Migrations(final ServiceLocator service,
                      final LegacyDBQueries legacyQueries,
                      final ResourcesExt resourcesExt,
                      final Migration pagesExt,
                      final IFileUploader fu,
                      final Options options) {
        super(
            service,
            pagesExt,
            resourcesExt,
            legacyQueries,
            new TemplateMigration(legacyQueries, service.getTemplates()),
            "/"+options.getApp()+"/");

        _options = options;

        try {
            _contentRoot = getResources().resourceForPath("");
            _filesFolder = getResources().resourceForPath("/files");
            _contentImagesFolder =
                getResources().resourceForPath("/images");

            _templateFolder =
                getResources().resourceForPath("/assets/templates");
            _assetsImagesFolder =
                getResources().resourceForPath("/assets/images");
            _cssFolder = getResources().resourceForPath("/assets/css");
        } catch (final CCException e) {
            throw new MigrationException(
                "Failed to retrieve default folder structure.", e);
        }

        _fm = new FileMigrator(fu, legacyQueries, "files/", "images/", "css/");
        _um = new UserMigration(legacyQueries, getUsers(), getGroups());
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
            if (_options.isMigrateHomepage()) {
                migrateHomepages();
            }
        } catch (final RuntimeException e) {
            log.error("Catastrophic failure.", e);
        }
    }

    private void migrateHomepages() throws CCException {
        final Map<Integer, Integer> map = getLegacyQueries().homepages();
        for (final Entry<Integer, Integer> e : map.entrySet()) {
            final ResourceSummary f =
                getResources().resourceForLegacyId(""+e.getKey());
            final ResourceSummary hp =
                getResources().resourceForLegacyId(""+map.get(e.getKey()));
            if (f != null && hp != null) {
                getResources().lock(UUID.fromString(f.getId().toString()));
                getFolders().updateFolder(
                    f.getId(),
                    new FolderDto(f.getSortOrder(), hp.getId(), null));
                getResources().unlock(f.getId());
            }
        }
        log.info("Migrated home page information of the folders.");
    }


    // TODO: Move under command-resourceDao?
    private void publishRecursive(final ResourceSummary resource) {
        getResources().lock(UUID.fromString(resource.getId().toString()));
        getResources().publish(resource.getId());
        if ("FOLDER".equals(resource.getType().name())) {
            final Collection<ResourceSummary> children =
                getFolders().getChildren(resource.getId());
            for (final ResourceSummary child : children) {
                publishRecursive(child);
            }
        }
        getResources().unlock(resource.getId());
    }


    private void loadSupportingData() {
        _menuItems = getLegacyQueries().selectMenuItems();
    }


    private void migrateResources(final UUID parentFolderId,
                                  final int parent) {

        final List<ResourceBean> resources =
            getLegacyQueries().selectResources(parent);
        final List<String> ignoreList = _options.getIgnorePaths();

        for (final ResourceBean r : resources) {
            if (r.name() == null || r.name().trim().equals("")) {
                log.warn("Ignoring resource with missing name: "+r.contentId());
                continue;
            }
            if (ignoreList.contains(""+r.contentId())) {
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
                _options.getUsername());

            final ResourceSummary rs = getMigrations().createFolder(
                    parentFolderId,
                    r.name(),
                    r.cleanTitle(),
                    false,
                    le.getUser().getId(),
                    le.getHappenedOn());
            log.debug("Created folder: "+r.contentId());

            getMigrations().lock(
                UUID.fromString(
                    rs.getId().toString()),
                    le.getUser().getId(),
                    le.getHappenedOn());
            setTemplateForResource(r, rs, le, _templateFolder);
            publish(r, rs, le);
            showInMainMenu(r, rs, le);
            setMetadata(r, rs, le);
            setResourceRoles(r, rs, le);
            getMigrations().unlock(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());

            migrateResources(rs.getId(), r.contentId());

        } catch (final Exception e) {
//          log.warn("Error migrating folder "+r.contentId(),  e);
            log.warn(logMigrationError("Error migrating folder ", r, e), e);
        }
    }


    private String logMigrationError(final String errorText,
                                     final ResourceBean r,
                                     final Exception e) {

        return
            errorText + r.contentId()+ ", "
            + r.cleanTitle() + ": "+e.getMessage();
    }

    private void migratePage(final UUID parentFolderId,
                             final ResourceBean resource) {

        try {
            // Query the versions of a page
            final List<Integer> paragraphVersions =
                determinePageVersions(resource);

            // Create the page
            LogEntryBean le = null;
            Integer createVersion = paragraphVersions.remove(0);
            while (le == null) {
                try {
                    le = logEntryForVersion(
                        resource.contentId(),
                        createVersion, "CREATED PAGE",
                        _options.getUsername());
                } catch (final MigrationException e) {
                    log.warn("Skipped version "+createVersion+" of page "
                        +resource.contentId()+" "+e.getMessage());
                    if (paragraphVersions.size() == 0) {
                        throw e;
                    }
                    createVersion = paragraphVersions.remove(0);
                }
            }

            PageDto delta = assemblePage(resource, createVersion.intValue());
            final ResourceSummary rs =
                createPage(parentFolderId, resource, createVersion, le, delta);

            // Apply all updates
            for (final Integer version : paragraphVersions) {
                try {
                    le = logEntryForVersion(
                        resource.contentId(),
                        version,
                        "UPDATE",
                        _options.getUsername());

                    getMigrations().lock(
                        UUID.fromString(
                            rs.getId().toString()),
                            le.getUser().getId(),
                            le.getHappenedOn());
                    try {
                        delta = assemblePage(resource, version);
                        updatePage(resource, rs, version, le, delta);
                    } catch (final MigrationException e) {
                        log.warn("Update skipped(inner) for version  "+version
                          +" of page "+resource.contentId()+" "+e.getMessage());
                    }
                    getMigrations().unlock(
                        rs.getId(), le.getUser().getId(), le.getHappenedOn());
                } catch (final MigrationException e) {
                    log.warn("Update skipped for version "+version
                        +" of page "+resource.contentId()+" "+e.getMessage());
                }
            }

            getMigrations().lock(
                UUID.fromString(
                    rs.getId().toString()),
                    le.getUser().getId(),
                    le.getHappenedOn());
            setTemplateForResource(resource, rs, le, _templateFolder);
            publish(resource, rs, le);
            showInMainMenu(resource, rs, le);

            for (final Paragraph paragraph : delta.getParagraphs()) {
                if ("Description_Custom".equals(paragraph.getName())) {
                    rs.setDescription(paragraph.getText());
                } else if ("Keywords_Custom".equals(paragraph.getName())) {
                    rs.setTags(paragraph.getText());
                }
            }

            setMetadata(resource, rs, le);
            setResourceRoles(resource, rs, le);
            getMigrations().unlock(
                rs.getId(), le.getUser().getId(), le.getHappenedOn());

            log.debug("Migrated page "+resource.contentId());

        } catch (final CCException e) {
            log.warn(
                logMigrationError(
                    "Error migrating page ", resource, e), e);
        } catch (final RuntimeException e) {
            log.warn(
                logMigrationError(
                    "Error migrating page ", resource, e), e);
        }
    }


    private void showInMainMenu(final ResourceBean r,
                                final ResourceSummary rs,
                                final LogEntryBean le)
                                                 throws CCException {
        if (_menuItems.contains(Integer.valueOf(r.contentId()))) {
            getMigrations().includeInMainMenu(
                rs.getId(), true, le.getUser().getId(), le.getHappenedOn());
        }
    }


    private List<Integer> determinePageVersions(final ResourceBean r) {
        final List<Integer> only0 = new ArrayList<Integer>();
        only0.add(Integer.valueOf(0));

        if (!_options.isMigrateVersions()) {
            return only0;
        }

        final List<Integer> paragraphVersions =
            getLegacyQueries().selectParagraphVersions(r.contentId());
        log.debug("Page versions available: "+paragraphVersions);
        if (paragraphVersions.size() == 0) {
            log.warn(
                "No versions found! Uses version 0 only for resource "
                + r.contentId());
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
                            final PageDto d) {

        d.setComment(
            getLegacyQueries().selectUserComment(r.contentId(), version));

        Boolean isMajorEdit = Boolean.valueOf(false);
        if (_options.isMigrateIsMajorEdit()) {
            isMajorEdit =
                getLegacyQueries().selectIsMajorEdit(r.contentId(), version);
        }
        d.setMajorChange(isMajorEdit.booleanValue());

        getMigrations().updatePage(
            rs.getId(),
            d,
            le.getUser().getId(),
            le.getHappenedOn());

        log.debug("Updated page: "+r.contentId()+" v."+version);
    }

}
