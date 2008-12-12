/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import java.util.Collection;
import java.util.UUID;

import ccc.domain.Folder;


/**
 * DAO API for the {@link Folder} class.
 *
 * @author Civic Computing Ltd.
 */
public interface FolderDao {

    /**
     * Create a folder in the specified folder.
     *
     * @param parentId The {@link UUID} for the containing folder/
     * @param newFolder The folder to be created.
     * @return The newly created folder.
     */
    Folder create(UUID parentId, Folder newFolder);

    /**
     * List all the root folders available.
     *
     * @return The collection of folders without a parent.
     */
    Collection<Folder> roots();

    /**
     * Create a new root folder.
     *
     * @param f The folder to create.
     */
    void createRoot(Folder f);

}
