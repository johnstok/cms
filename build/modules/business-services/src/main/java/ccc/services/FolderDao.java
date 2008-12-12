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
 * TODO: Add Description for this type.
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
     * TODO: Add a description of this method.
     *
     * @return
     */
    Collection<Folder> roots();

    /**
     * TODO: Add a description of this method.
     *
     * @param f
     * @return
     */
    void createRoot(Folder f);

}
