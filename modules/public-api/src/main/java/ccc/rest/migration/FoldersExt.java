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
package ccc.rest.migration;

import java.util.Date;
import java.util.UUID;

import ccc.rest.CommandFailedException;
import ccc.rest.Folders;
import ccc.rest.dto.ResourceSummary;


/**
 * Folder Commands API, used to update folder data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface FoldersExt extends Folders {

    /**
     * Create a folder with the specified name and title.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     * @param title The title of the folder.
     * @param publish True if the title should be published, false otherwise.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(UUID parentId,
                                 String name,
                                 String title,
                                 boolean publish) throws CommandFailedException;

    /**
     * Create a folder with the specified name and title.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     * @param title The title of the folder.
     * @param publish True if the title should be published, false otherwise.
     * @param actorId The user id of the actor.
     * @param happenedOn When the command happened.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(UUID parentId,
                                 String name,
                                 String title,
                                 boolean publish,
                                 UUID actorId,
                                 Date happenedOn)
    throws CommandFailedException;


    /**
     * Create a root folder with the specified name.
     *
     * @param name The name of the root folder.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new root.
     */
    ResourceSummary createRoot(String name)
    throws CommandFailedException;
}
