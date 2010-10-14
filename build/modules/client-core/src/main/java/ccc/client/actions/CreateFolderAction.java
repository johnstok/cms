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

import java.util.UUID;

import ccc.api.core.Folder;
import ccc.api.types.ResourceName;
import ccc.client.callbacks.FolderCreatedCallback;
import ccc.client.commands.CreateFolderCommand;
import ccc.client.core.Action;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;


/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateFolderAction
    implements
        Action {

    private final String _name;
    private final UUID _parentFolder;


    /**
     * Constructor.
     *
     * @param name The folder's name.
     * @param parentFolder The folder's parent folder.
     */
    public CreateFolderAction(final UUID parentFolder, final String name) {
        _parentFolder = parentFolder;
        _name = name;
    }


    /** {@inheritDoc} */
    @Override
    public void execute() {

        final Folder f = new Folder();
        f.setParent(_parentFolder);
        f.setName(new ResourceName(_name));

        new CreateFolderCommand(f).invoke(
            InternalServices.api,
            new FolderCreatedCallback(I18n.uiConstants.createFolder()));
    }
}
