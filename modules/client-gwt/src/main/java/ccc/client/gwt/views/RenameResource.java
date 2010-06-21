/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views;

import ccc.client.core.Editable;
import ccc.client.core.Validatable;
import ccc.client.core.View;


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
