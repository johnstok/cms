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
package ccc.rest.extensions;

import java.util.UUID;

import ccc.rest.dto.ResourceSummary;
import ccc.rest.exceptions.RestException;


/**
 * Folder Commands API, used to update folder data in CCC.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated
public interface FoldersExt {

    /**
     * Create a folder with the specified name and title.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     * @param title The title of the folder.
     * @param publish True if the title should be published, false otherwise.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(UUID parentId,
                                 String name,
                                 String title,
                                 boolean publish) throws RestException;


    /**
     * Create a root folder with the specified name.
     *
     * @param name The name of the root folder.
     *
     * @throws RestException If the method fails.
     *
     * @return A resource summary describing the new root.
     */
    ResourceSummary createRoot(String name)
    throws RestException;
}
