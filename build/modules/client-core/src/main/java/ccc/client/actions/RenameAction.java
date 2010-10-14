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
package ccc.client.actions;


import ccc.api.core.Resource;
import ccc.api.types.DBC;
import ccc.api.types.ResourcePath;
import ccc.client.callbacks.ResourceRenamedCallback;
import ccc.client.commands.RenameResourceCommand;
import ccc.client.core.Action;
import ccc.client.core.I18n;


/**
 * Remote action for renaming.
 *
 * @author Civic Computing Ltd.
 */
public final class RenameAction
    implements
        Action {

    private final String _name;
    private final Resource _resource;
    private final ResourcePath _newPath;


    /**
     * Constructor.
     *
     * @param resource The resource to update.
     * @param name The new name for this resource.
     * @param newPath The updated absolute path to the resource.
     */
    public RenameAction(final Resource resource,
                        final String name,
                        final ResourcePath newPath) {
        _resource = DBC.require().notNull(resource);
        _name     = DBC.require().notEmpty(name);
        _newPath  = DBC.require().notNull(newPath);
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {
        new RenameResourceCommand(_name).invoke(
            _resource,
            new ResourceRenamedCallback(
                I18n.uiConstants.rename(),
                _name,
                _resource.getId(),
                _newPath));
    }
}
