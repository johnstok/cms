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
package ccc.persistence;

import java.util.Collection;
import java.util.Date;

import ccc.rest.CommandFailedException;
import ccc.rest.ResourceSummary;
import ccc.types.ID;


/**
 * Folder Commands API, used to update folder data in CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface FolderCommands {

    /** NAME : String. */
    String NAME = "PublicFolderCommands";

    /**
     * Create a folder with the specified name.
     *
     * @param parentId The folder in which the new folder should be created.
     * @param name The name of the new folder.
     *
     * @throws CommandFailedException If the method fails.
     *
     * @return A resource summary describing the new folder.
     */
    ResourceSummary createFolder(ID parentId, String name)
    throws CommandFailedException;

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
    ResourceSummary createFolder(ID parentId,
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
    ResourceSummary createFolder(ID parentId,
                                 String name,
                                 String title,
                                 boolean publish,
                                 ID actorId,
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


    /**
     * Update the specified folder.
     *
     * @param folderId The id of the folder to update.
     * @param sortOrder The new sort order.
     * @param indexPageId The id of the index page to update.
     * @param sortList The new order of folder's items.
     *
     * @throws CommandFailedException If the method fails.
     */
    void updateFolder(ID folderId,
                      String sortOrder,
                      ID indexPageId,
                      Collection<String> sortList)
    throws CommandFailedException;
}
