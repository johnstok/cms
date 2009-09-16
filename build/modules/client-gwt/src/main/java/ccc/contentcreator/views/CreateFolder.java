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
package ccc.contentcreator.views;

import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.Validatable;
import ccc.contentcreator.client.View;


/**
 * MVP view for creating a folder.
 *
 * @author Civic Computing Ltd.
 */
public interface CreateFolder
    extends
        View<Editable>,
        Validatable {

    /**
     * Accessor.
     *
     * @return The folder name.
     */
    String getName();


    /**
     * Mutator.
     *
     * @param name The new folder name to set.
     */
    void setName(String name);
}
