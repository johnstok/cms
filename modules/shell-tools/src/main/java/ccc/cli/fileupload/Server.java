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

import ccc.rest.CommandFailedException;



/**
 * Server API for file upload.
 *
 * @author Civic Computing Ltd.
 */
public interface Server {

    /**
     * Create a file in the new system.
     *
     * @param parentFolder The folder where the file will be created.
     * @param f The file to create.
     * @param publish Should the file be published.
     */
    void createFile(final UUID parentFolder, final File f, boolean publish);

    /**
     * Create a folder in the new system.
     *
     * @param parentFolder The parent folder where the new folder will be
     *  created.
     * @param name The name of the folder.
     * @param publish Should the folder be published.
     * @return The id of the newly created folder.
     * @throws CommandFailedException If it wasn't possible to create the
     *  folder.
     */
    UUID createFolder(final UUID parentFolder, String name, boolean publish)
    throws CommandFailedException;

    /**
     * Get the id of the root folder in the new system.
     *
     * @return The id of the root folder.
     */
    UUID getRoot();
}
