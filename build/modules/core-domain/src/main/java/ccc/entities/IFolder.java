/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.entities;

import java.util.List;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface IFolder {

    /**
     * Accessor for entries.
     *
     * @return A list of all the resources in this folder.
     */
    List<? extends IResource> entries();

    /**
     * Accessor for entries.
     *
     * <p>For example, calling entries(10, 3) will return the resources with
     * positions 20..29 in the list.
     *
     * @param count     The number of entries to return.
     * @param page      The page of entries to return.
     * @param sortOrder The order in which the entries should be sorted.
     *
     * @return A list of all the resources in this folder.
     */
    List<? extends IResource> entries(int count, int page, String sortOrder);

}
