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
 * MVP View for updating a resource's name.
 *
 * @author Civic Computing Ltd.
 */
public interface RenameResource
    extends
        View<Editable>,
        Validatable {


    /**
     * Mutator.
     *
     * @param name The new resource name to set.
     */
    void setName(String name);


    /**
     * Accessor.
     *
     * @return The current resource name.
     */
    String getName();
}
