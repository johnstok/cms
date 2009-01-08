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

import ccc.domain.Folder;


/**
 * DAO API for the {@link Folder} class.
 *
 * @author Civic Computing Ltd.
 */
public interface FolderDao {

    /**
     * List all the root folders available.
     *
     * @return The collection of folders without a parent.
     */
    Collection<Folder> roots();

}
