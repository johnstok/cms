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
package ccc.cli.fileupload;

import java.io.File;
import java.util.UUID;

import ccc.rest.RestException;



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
     * @throws RestException If it wasn't possible to create the
     *  folder.
     */
    UUID createFolder(final UUID parentFolder, String name, boolean publish)
    throws RestException;

    /**
     * Get the id of the root folder in the new system.
     * TODO: Throw a more appropriate exception.
     *
     * @throws RestException If the root folder doesn't exist.
     *
     * @return The id of the root folder.
     */
    UUID getRoot() throws RestException;
}
