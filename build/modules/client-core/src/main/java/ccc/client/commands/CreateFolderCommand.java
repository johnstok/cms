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
package ccc.client.commands;

import ccc.api.core.API;
import ccc.api.core.Folder;
import ccc.api.types.DBC;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Command;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.Parser;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderCommand
    extends
        Command<API, Folder> {

    private final Folder _f;

    /**
     * Constructor.
     *
     * @param f The folder to create.
     */
    public CreateFolderCommand(final Folder f) {
        _f = DBC.require().notNull(f);
    }

    /** {@inheritDoc} */
    @Override
    public void invoke(final API api, final Callback<Folder> callback) {
        final String path = getBaseUrl()+api.folders();

        final Request r =
            new Request(
                HttpMethod.POST,
                path,
                writeFolder(_f),
                new CallbackResponseHandler<Folder>(
                    I18n.UI_CONSTANTS.createFolder(),
                    callback,
                    new Parser<Folder>() {
                        @Override public Folder parse(final Response response) {
                            return readFolder(response);
                        }}));

        getExecutor().invokeRequest(r);
    }

}
