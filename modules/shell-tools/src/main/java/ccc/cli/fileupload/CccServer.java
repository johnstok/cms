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
package ccc.cli.fileupload;

import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.cli.FileUpload;
import ccc.migration.FileUploader;
import ccc.rest.CommandFailedException;
import ccc.rest.FolderCommands;
import ccc.rest.Queries;
import ccc.rest.dto.ResourceSummary;
import ccc.types.FailureCode;
import ccc.types.ID;
import ccc.types.ResourcePath;


/**
 * Implementation of the {@link Server} interface - calls the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public class CccServer implements Server {
    private static final Logger LOG = Logger.getLogger(FileUpload.class);

    private ResourcePath _rootPath;
    private FileUploader _uploader;
    private FolderCommands _folderCommands;
    private Queries _queries;



    /**
     * Constructor.
     *
     * @param rootPath The absolute path to the folder where files will be
     *  uploaded.
     * @param uploader The file up-loader to use.
     * @param commands The command API.
     * @param queries The query API.
     */
    public CccServer(final ResourcePath rootPath,
                     final FileUploader uploader,
                     final FolderCommands folderCommands,
                     final Queries queries) {
        _rootPath = rootPath;
        _uploader = uploader;
        _folderCommands = folderCommands;
        _queries = queries;
    }


    /** {@inheritDoc} */
    @Override
    public void createFile(final UUID parentFolder,
                           final File f,
                           final boolean publish) {
        _uploader.uploadFile(
            parentFolder, f.getName(), f.getName(), "", null, f, publish);
    }


    /** {@inheritDoc} */
    @Override
    public UUID createFolder(final UUID parentFolder,
                             final String name,
                             final boolean publish)
                                                 throws CommandFailedException {

        try {
            final ResourceSummary rs = _folderCommands.createFolder(
                new ID(parentFolder.toString()), name, name, publish);
            return UUID.fromString(rs.getId().toString());
        } catch (final CommandFailedException e) {
            if (FailureCode.EXISTS==e.getCode()) {
                LOG.warn("Folder already exists: "+name);
                return UUID.fromString(
                    e.getFailure().getParams().get("existing_id"));
            }
            throw e;
        }
    }


    /** {@inheritDoc} */
    @Override
    public UUID getRoot() {
        final ResourceSummary rs =
            _queries.resourceForPath(_rootPath.toString());
        return UUID.fromString(rs.getId().toString());
    }
}
