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

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.cli.MigrateMhtsFiles.Options;
import ccc.rest.Actions;
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

    private static final boolean DONT_PUBLISH = false;
    private static final String CREATED_PAGE_ACTION = "CREATED PAGE";
    private static Logger log = Logger.getLogger(MhtsFileMigration.class);

    private final ResourceSummary _templateFolder;

    private FoldersExt _foldersExt;
    private String _username;
    private FileUploader _fu;
    private String _filePath;
    private Actions _actions;

    /**
     * Constructor.
     * @param legacyDBQueries
     * @param actions TODO
     * @param resourcesExt
     * @param pagesExt
     * @param foldersExt
     * @param users
     * @param fileUploader
     * @param options
     *
     */
    public MhtsFileMigration(final LegacyDBQueries legacyDBQueries,
                             final Actions actions,
                             final ResourcesExt resourcesExt,
                             final PagesExt pagesExt,
                             final FoldersExt foldersExt,
                             final Users users,
                             final Templates templates,
                             final FileUploader fileUploader,
                             final Options options) {
        _userCommands = users;
        _linkPrefix = options.getApp()+"/";
        _legacyQueries = legacyDBQueries;
        _actions = actions;
        _foldersExt = foldersExt;
        _pagesExt = pagesExt;
        _resourcesExt = resourcesExt;
        _username = options.getUsername();
        _filePath = options.getLocalPath();
        _um = new UserMigration(_legacyQueries, _userCommands);
        _fu = fileUploader;

        try {

            _templateFolder =
                _resourcesExt.resourceForPath("/assets/templates");
        } catch (final RestException e) {
            throw new MigrationException(
                "Failed to retrieve default folder structure.", e);
        }

        _tm = new TemplateMigration(_legacyQueries, templates);
    }

    /**
     * Migrate MHTS members area.
     *
     * @param legacyParent The content id for the legacy members area.
     * @param path The path to migrate members area files in CCC7.
     */
    public void migrate(final int legacyParent, final String path) {

        final List<ResourceBean> resources =
            _legacyQueries.selectResources(legacyParent);

        try {
            final UUID parentFolderId = createFileFolder(legacyParent, path);

            for (final ResourceBean resourceBean : resources) {

                final int contentId = resourceBean.contentId();
                final int legacyVersion = resourceBean.legacyVersion();

                final LogEntryBean logEntryBean =
                    logEntryForVersion(contentId,
                                       legacyVersion,
                                       CREATED_PAGE_ACTION,
                                       _username, log);

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
                                 logEntryBean, log);

                migratePage(resourceBean, folderResourceSummary, logEntryBean);

                unlockFolder(logEntryBeanId, happenedOn, folderId);
                createDeleteOnExpiryAction(resourceBean, folderResourceSummary);
            }

        } catch (final RestException e) {
            log.error("MHTS file migration failed.", e);
        }
    }

    private ResourceSummary createFolderResourceSummary(final UUID parentFolderId,
                                                        final ResourceBean resourceBean,
                                                        final UUID logEntryBeanId,
                                                        final Date happenedOn)
                                                        throws RestException {

        final int lastIndexOf = resourceBean.name().lastIndexOf(".");
        final String resourceBeanName = resourceBean.name().substring(0, lastIndexOf);
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

        _resourcesExt.lock(folderId, logEntryBeanId, happenedOn);
    }

    private void unlockFolder(final UUID logEntryBeanId,
                              final Date happenedOn,
                              final UUID folderId) throws RestException {

        _resourcesExt.unlock(folderId, logEntryBeanId, happenedOn);
    }

    private void createDeleteOnExpiryAction(final ResourceBean resourceBean,
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


        final PageDelta delta = assemblePage(pageBean, 0, log);
        final ResourceSummary pageRs =
            createPage(folderRs.getId(), pageBean, 0, le, delta, log);

        _resourcesExt.lock(pageRs.getId(),
                le.getUser().getId(),
                le.getHappenedOn());
        publish(pageBean, pageRs, le);
        _resourcesExt.unlock(
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
            _resourcesExt.resourceForPath(path);

        final ResourceBean legacyFileFolder =
            _legacyQueries.selectSingleResource(legacyParent);

        final LogEntryBean le = logEntryForVersion(
            legacyFileFolder.contentId(),
            legacyFileFolder.legacyVersion(),
            "CREATED FOLDER",
            _username,
            log);

        final ResourceSummary rs = _foldersExt.createFolder(
            parentFolder.getId(),
            legacyFileFolder.name(),
            legacyFileFolder.title(),
            false,
            le.getUser().getId(),
            le.getHappenedOn());

        log.debug("Created folder: "+legacyFileFolder.contentId());
        _resourcesExt.lock(
            UUID.fromString(
                rs.getId().toString()),
                le.getUser().getId(),
                le.getHappenedOn());
        setTemplateForResource(legacyFileFolder, rs, le, _templateFolder);
        publish(legacyFileFolder, rs, le);

        _resourcesExt.unlock(
            rs.getId(), le.getUser().getId(), le.getHappenedOn());
        return rs.getId();
    }
}
