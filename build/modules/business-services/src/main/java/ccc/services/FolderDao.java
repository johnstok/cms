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
import ccc.domain.ResourceOrder;


/**
 * DAO API for the {@link Folder} class.
 *
 * @author Civic Computing Ltd.
 */
public interface FolderDao {

    /** NAME : String. */
    String NAME = "FolderDao";

    /**
     * List all the root folders available.
     *
     * @return The collection of folders without a parent.
     */
    Collection<Folder> roots();

    /**
     * Change the sort order for a folder.
     *
     * @param folderId The id of the folder.
     * @param order The new sort order.
     */
    void updateSortOrder(UUID folderId, ResourceOrder order);
}
