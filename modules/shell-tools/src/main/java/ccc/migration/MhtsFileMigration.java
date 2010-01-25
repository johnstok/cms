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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.client1.IFileUploader;
import ccc.cli.MigrateMhtsFiles.Options;
import ccc.rest.Actions;
import ccc.rest.Groups;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.dto.ActionDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.CommandType;
import ccc.types.Paragraph;



/**
 * MHTS members area specific migration tool.
 *
 * @author Civic Computing Ltd.
 */
public class MhtsFileMigration extends BaseMigrations {
    private static Logger log = Logger.getLogger(MhtsFileMigration.class);

    private static final boolean DONT_PUBLISH = false;
    private static final String CREATED_PAGE_ACTION = "CREATED PAGE";

    private final ResourceSummary _templateFolder;

    private FoldersExt _foldersExt;
    private String _username;
    private IFileUploader _fu;
    private String _filePath;
    private Actions _actions;

    /**
     * Constructor.
     * @param legacyDBQueries
     * @param actions
     * @param foldersExt
     * @param users The CCC7 users API.
     * @param pagesExt The CCC7 pages API.
     * @param resourcesExt The CCC7 resources API.
     * @param fileUploader
     * @param options
     *
     */
    public MhtsFileMigration(final LegacyDBQueries legacyDBQueries,
                             final Actions actions,
                             final ResourcesExt resourcesExt,
                             final PagesExt pagesExt,
                             final FoldersExt foldersExt,
                             final Groups groups,
                             final Users users,
                             final Templates templates,
                             final IFileUploader fileUploader,
                             final Options options) {
        super(
            users,
            pagesExt,
            resourcesExt,
            groups,
            legacyDBQueries,
            new TemplateMigration(legacyDBQueries, templates),
            "/"+options.getApp()+"/");
        _actions = actions;
        _foldersExt = foldersExt;
        _username = options.getUsername();
        _filePath = options.getLocalPath();
        _fu = fileUploader;

        try {
            _templateFolder = resourcesExt.resourceForPath("/assets/templates");
        } catch (final RestException e) {
            throw new MigrationException(
                "Failed to retrieve default folder structure.", e);
        }
    }

    /**
     * Migrate MHTS members area.
     *
     * @param legacyParent The content id for the legacy members area.
     * @param path The path to migrate members area files in CCC7.
     */
    public void migrate(final int legacyParent, final String path) {

        final List<ResourceBean> resources =
            getLegacyQueries().selectResources(legacyParent);

        try {
            final UUID parentFolderId = createFileFolder(legacyParent, path);

            for (final ResourceBean resourceBean : resources) {

                final int contentId = resourceBean.contentId();
                final int legacyVersion = resourceBean.legacyVersion();

                final LogEntryBean logEntryBean =
                    logEntryForVersion(contentId,
                                       legacyVersion,
                                       CREATED_PAGE_ACTION,
                                       _username);

                final UUID logEntryBeanId = logEntryBean.getUser().getId();
                final Date happenedOn = logEntryBean.getHappenedOn();

                final ResourceSummary folderResourceSummary =
                    createFolderResourceSummary(parentFolderId,
                                                resourceBean,
                                                logEntryBeanId,
                                                happenedOn);

                log.debug("Created folder: " + contentId);

                final UUID folderId = folderResourceSummary.getId();

                lockFolder(logEntryBeanId, happenedOn, folderId);
                setTemplateForResource(resourceBean,
                                       folderResourceSummary,
                                       logEntryBean,
                                       _templateFolder);
                publish(resourceBean, folderResourceSummary, logEntryBean);
                // showInMainMenu(r, folderRs, le); // TODO ?
                setMetadata(resourceBean, folderResourceSummary, logEntryBean);
                setResourceRoles(resourceBean,
                                 folderResourceSummary,
                                 logEntryBean);

                migratePage(resourceBean, folderResourceSummary, logEntryBean);

                unlockFolder(logEntryBeanId, happenedOn, folderId);
                getResources().lock(folderId);
                createDeleteOnExpiryAction(resourceBean, folderResourceSummary);
            }

        } catch (final RestException e) {
            log.error("MHTS file migration failed.", e);
        }
    }


    private ResourceSummary createFolderResourceSummary(
                                                final UUID parentFolderId,
                                                final ResourceBean resourceBean,
                                                final UUID logEntryBeanId,
                                                final Date happenedOn)
                                                throws RestException {

        final int lastIndexOf = resourceBean.name().lastIndexOf(".");
        final String resourceBeanName =
            resourceBean.name().substring(0, lastIndexOf);
        final String folderTitle = resourceBean.title();
        final ResourceSummary folderResourceSummary =
            _foldersExt.createFolder(parentFolderId,
                                     resourceBeanName,
                                     folderTitle,
                                     DONT_PUBLISH,
                                     logEntryBeanId,
                                     happenedOn);
        return folderResourceSummary;
    }

    private void lockFolder(final UUID logEntryBeanId,
                            final Date happenedOn,
                            final UUID folderId) throws RestException {

        getResources().lock(folderId, logEntryBeanId, happenedOn);
    }

    private void unlockFolder(final UUID logEntryBeanId,
                              final Date happenedOn,
                              final UUID folderId) throws RestException {

        getResources().unlock(folderId, logEntryBeanId, happenedOn);
    }

    private void createDeleteOnExpiryAction(
                                    final ResourceBean resourceBean,
                                    final ResourceSummary folderResourceSummary)
                                    throws RestException {

        final ActionDto actionDto = new ActionDto(folderResourceSummary.getId(),
            CommandType.RESOURCE_DELETE,
            resourceBean.expiryDate(),
            null);
        _actions.createAction(actionDto);
    }

    /**
     * Migrate the case page and upload its files.
     *
     * @param r
     * @param folderRs
     * @param le
     * @throws RestException
     */
    private void migratePage(final ResourceBean r,
                             final ResourceSummary folderRs,
                             final LogEntryBean le) throws RestException {

        final ResourceBean pageBean = new ResourceBean(
            r.contentId(),
            r.type(),
            r.name().substring(0, r.name().lastIndexOf(".")),
            r.displayTemplate(),
            r.isPublished(),
            r.legacyVersion(),
            r.isSecure(),
            r.title(),
            r.useInIndex(),
            r.templateDescription(),
            r.expiryDate());


        final PageDelta delta = assemblePage(pageBean, 0);
        final ResourceSummary pageRs =
            createPage(folderRs.getId(), pageBean, 0, le, delta);

        getResources().lock(pageRs.getId(),
                le.getUser().getId(),
                le.getHappenedOn());
        publish(pageBean, pageRs, le);
        getResources().unlock(
            pageRs.getId(), le.getUser().getId(), le.getHappenedOn());

        for (final Paragraph para : delta.getParagraphs()) {
            if (para.name().startsWith("Document")) {
                final String filename = para.text();
                final File file = new File(_filePath+filename);

                _fu.uploadFile(folderRs.getId(),
                    filename,
                    filename,
                    "",
                    null,
                    file,
                    true);
            }
        }
    }

    private UUID createFileFolder(final int legacyParent,
                                  final String path) throws RestException {

        final ResourceSummary parentFolder =
            getResources().resourceForPath(path);

        final ResourceBean legacyFileFolder =
            getLegacyQueries().selectSingleResource(legacyParent);

        final LogEntryBean le = logEntryForVersion(
            legacyFileFolder.contentId(),
            legacyFileFolder.legacyVersion(),
            "CREATED FOLDER",
            _username);

        final ResourceSummary rs = _foldersExt.createFolder(
            parentFolder.getId(),
            legacyFileFolder.name(),
            legacyFileFolder.title(),
            false,
            le.getUser().getId(),
            le.getHappenedOn());

        log.debug("Created folder: "+legacyFileFolder.contentId());
        getResources().lock(
            UUID.fromString(
                rs.getId().toString()),
                le.getUser().getId(),
                le.getHappenedOn());
        setTemplateForResource(legacyFileFolder, rs, le, _templateFolder);
        publish(legacyFileFolder, rs, le);

        getResources().unlock(
            rs.getId(), le.getUser().getId(), le.getHappenedOn());
        return rs.getId();
    }
}
