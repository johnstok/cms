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
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.cli.MigrateMhtsFiles.Options;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.Paragraph;



/**
 * MHTS members area specific migration tool.
 *
 * @author Civic Computing Ltd.
 */
public class MhtsFileMigration extends BaseMigrations {
    private static Logger log = Logger.getLogger(MhtsFileMigration.class);

    private final ResourceSummary _templateFolder;

    private FoldersExt _foldersExt;

    private String _username;

    private FileUploader _fu;

    private String _filePath;


    /**
     * Constructor.
     * @param fileUploader
     * @param users
     * @param foldersExt
     * @param pagesExt
     * @param resourcesExt
     * @param legacyDBQueries
     * @param options
     *
     */
    public MhtsFileMigration(final LegacyDBQueries legacyDBQueries,
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

            for (final ResourceBean r : resources) {
                final LogEntryBean le = logEntryForVersion(
                    r.contentId(),
                    r.legacyVersion(),
                    "CREATED PAGE",
                    _username,
                    log);

                final ResourceSummary folderRs = _foldersExt.createFolder(
                    parentFolderId,
                    r.name().substring(0, r.name().lastIndexOf(".")),
                    r.title(),
                    false,
                    le.getUser().getId(),
                    le.getHappenedOn());
                log.debug("Created folder: "+r.contentId());

                _resourcesExt.lock(
                        folderRs.getId(),
                        le.getUser().getId(),
                        le.getHappenedOn());
                setTemplateForResource(r, folderRs, le, _templateFolder);
                publish(r, folderRs, le);
                // showInMainMenu(r, folderRs, le); // TODO ?
                setMetadata(r, folderRs, le);
                setResourceRoles(r, folderRs, le, log);

                migratePage(r, folderRs, le);

                _resourcesExt.unlock(
                    folderRs.getId(), le.getUser().getId(), le.getHappenedOn());
            }

        } catch (final RestException e) {
            log.error("MHTS file migration failed.", e);
        }

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
