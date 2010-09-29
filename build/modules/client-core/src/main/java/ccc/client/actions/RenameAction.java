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
 * Remote action for renaming.
 *
 * @author Civic Computing Ltd.
 */
public final class RenameAction
    extends
        RemotingAction<Void> {

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
        super(I18n.UI_CONSTANTS.rename());
        _resource = DBC.require().notNull(resource);
        _name     = DBC.require().notEmpty(name);
        _newPath  = DBC.require().notNull(newPath);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Void> callback) {
        return
            new Request(
                HttpMethod.POST,
                Globals.API_URL
                    + _resource.rename().build(InternalServices.ENCODER),
                    _name,
                new CallbackResponseHandler<Void>(
                    I18n.UI_CONSTANTS.rename(),
                    callback,
                    new Parser<Void>() {
                        @Override public Void parse(final Response response) {
                            return null;
                        }}));
    }
}
