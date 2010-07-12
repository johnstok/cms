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
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.api.types.ResourceName;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.FolderSerializer;


/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateFolderAction
    extends
        RemotingAction {

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
    protected Request getRequest() {
        return createFolder(_name, _parentFolder);
    }


    /**
     * Create a new folder.
     *
     * @param name The new folder's name.
     * @param parentFolder The parent folder.
     *
     * @return The HTTP request to create a folder.
     */
    // FIXME: Should pass a folder here.
    public Request createFolder(final String name,
                                       final UUID parentFolder) {
        final String path = Globals.API_URL+InternalServices.API.folders();

        final Json json = InternalServices.PARSER.newJson();
        final Folder f = new Folder();
        f.setParent(parentFolder);
        f.setName(new ResourceName(name));
        new FolderSerializer().write(json, f);

        return
            new Request(
                HttpMethod.POST,
                path,
                json.toString(),
                new FolderCreatedCallback(
                    I18n.UI_CONSTANTS.createFolder()));
    }


    /**
     * Callback handler for creating a folder.
     *
     * @author Civic Computing Ltd.
     */
    public class FolderCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         *
         * @param name The action name.
         */
        public FolderCreatedCallback(final String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final ccc.client.core.Response response) {
            final ResourceSummary rs = parseResourceSummary(response);
            final Event<CommandType> event =
                new Event<CommandType>(CommandType.FOLDER_CREATE);
            event.addProperty("resource", rs);
            InternalServices.REMOTING_BUS.fireEvent(event);
        }
    }
}
