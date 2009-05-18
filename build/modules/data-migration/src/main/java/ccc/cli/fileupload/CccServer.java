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

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.ID;
import ccc.api.Queries;
import ccc.api.ResourceSummary;
import ccc.domain.ResourcePath;
import ccc.migration.FileUploader;


/**
 * Implementation of the {@link Server} interface - calls the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public class CccServer implements Server {
    private static final Logger LOG = Logger.getLogger(CccServer.class);

    ResourcePath _rootPath;
    FileUploader _uploader;
    Commands _commands;
    Queries _queries;



    /**
     * Constructor.
     *
     * @param rootPath
     * @param uploader
     * @param commands
     * @param queries
     */
    public CccServer(final ResourcePath rootPath,
                     final FileUploader uploader,
                     final Commands commands,
                     final Queries queries) {
        _rootPath = rootPath;
        _uploader = uploader;
        _commands = commands;
        _queries = queries;
    }


    /** {@inheritDoc} */
    @Override
    public void createFile(final UUID parentFolder,
                           final File f,
                           final boolean publish) {
        _uploader.uploadFile(
            parentFolder, f.getName(), f.getName(), "", f, publish);
    }


    /** {@inheritDoc} */
    @Override
    public UUID createFolder(final UUID parentFolder,
                             final String name,
                             final boolean publish)
                                                 throws CommandFailedException {
        final ResourceSummary rs = _commands.createFolder(
            new ID(parentFolder.toString()), name, name, publish);
        return UUID.fromString(rs.getId().toString());
    }


    /** {@inheritDoc} */
    @Override
    public UUID getRoot() {
        final ResourceSummary rs =
            _queries.resourceForPath(_rootPath.toString());
        return UUID.fromString(rs.getId().toString());
    }
}
