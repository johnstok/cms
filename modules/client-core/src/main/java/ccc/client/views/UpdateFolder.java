/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.client.views;

import java.util.List;
import java.util.UUID;

import ccc.api.core.Folder;
import ccc.client.core.Editable;
import ccc.client.core.Validatable;
import ccc.client.core.View;


/**
 * MVP View for updating a folder.
 *
 * @author Civic Computing Ltd.
 */
public interface UpdateFolder  extends View<Editable>, Validatable {

    /**
     * Mutator.
     *
     * @param id The folder index page id.
     */
    void setIndexPage(UUID id);

    /**
     * Accessor.
     *
     * @return The folder index page id.
     */
    UUID getIndexPage();

    /**
     * Accessor.
     *
     * @return The folder.
     */
    Folder getFolder();

    /**
     * Mutator.
     *
     * @param folder The folder.
     */
    void setFolder(Folder folder);

    /**
     * Accessor.
     *
     * @return The orderlist.
     */
    List<String> getOrderList();

}
