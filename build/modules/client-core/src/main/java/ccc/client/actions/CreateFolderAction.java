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
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateFolderAction
    extends
        RemotingAction<Folder> {

    private final String _name;
    private final UUID _parentFolder;

    /**
     * Constructor.
     *
     * @param name The folder's name.
     * @param parentFolder The folder's parent folder.
     */
    public CreateFolderAction(final UUID parentFolder, final String name) {
        super(I18n.UI_CONSTANTS.createFolder());
        _parentFolder = parentFolder;
        _name = name;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Folder> callback) {
        final String path = Globals.API_URL+InternalServices.API.folders();

        final Folder f = new Folder();
        f.setParent(_parentFolder);
        f.setName(new ResourceName(_name));

        return
            new Request(
                HttpMethod.POST,
                path,
                writeFolder(f),
                new CallbackResponseHandler<Folder>(
                    I18n.UI_CONSTANTS.createFolder(),
                    callback,
                    new Parser<Folder>() {
                        @Override public Folder parse(final Response response) {
                            return readFolder(response);
                        }}));
    }
}
