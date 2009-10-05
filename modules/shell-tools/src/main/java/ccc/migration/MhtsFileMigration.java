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

import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.cli.MigrateMhtsFiles.Options;
import ccc.rest.RestException;
import ccc.rest.Users;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MhtsFileMigration extends BaseMigrations {
    private static Logger log = Logger.getLogger(MhtsFileMigration.class);


    private FoldersExt _foldersExt;
    private ResourcesExt _resourcesExt;

    private String _username;

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
                             final FileUploader fileUploader,
                             final Options options) {
        _userCommands = users;
        _linkPrefix = options.getApp()+"/";
        _legacyQueries = legacyDBQueries;
        _foldersExt = foldersExt;
        _pagesExt = pagesExt;
        _resourcesExt = resourcesExt;
        _username = options.getUsername();
        _um = new UserMigration(_legacyQueries, _userCommands);
    }

    /**
     * TODO: Add a description for this method.
     * @param path
     *
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
                    "CREATED FOLDER",
                    _username,
                    log);

                final ResourceSummary rs = _foldersExt.createFolder(
                    parentFolderId,
                    r.name(),
                    r.title(),
                    false,
                    le.getUser().getId(),
                    le.getHappenedOn());
                log.debug("Created folder: "+r.contentId());

                // TODO set roles

                // migrate pages
                final PageDelta delta = assemblePage(r, 0, log);
                final ResourceSummary pageRs =
                    createPage(rs.getId(), r, 0, le, delta, log);

        }

        } catch (final RestException e) {
            // TODO Auto-generated catch block
            log.error("MHTS file migration failed.", e);
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
      return rs.getId();
    }



}
