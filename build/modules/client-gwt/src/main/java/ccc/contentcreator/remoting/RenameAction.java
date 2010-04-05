/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.remoting;

import java.util.UUID;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.core.Request;
import ccc.types.DBC;
import ccc.types.ResourcePath;


/**
 * Remote action for renaming.
 *
 * @author Civic Computing Ltd.
 */
public class RenameAction
    extends
        RemotingAction {

    private final String _name;
    private final UUID _id;
    private final ResourcePath _newPath;


    /**
     * Constructor.
     *
     * @param name The new name for this resource.
     * @param id The id of the resource to update.
     * @param newPath The updated absolute path to the resource.
     */
    public RenameAction(final UUID id,
                        final String name,
                        final ResourcePath newPath) {
        _id      = DBC.require().notNull(id);
        _name    = DBC.require().notEmpty(name);
        _newPath = DBC.require().notNull(newPath);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return ResourceSummaryModelData.rename(_name, _id, _newPath);
    }
}
