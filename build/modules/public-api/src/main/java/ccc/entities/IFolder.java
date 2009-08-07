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

}