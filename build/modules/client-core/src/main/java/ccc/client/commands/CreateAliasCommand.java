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
import ccc.api.core.Alias;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Command;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Request;


/**
 * Create a new alias.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasCommand
    extends
        Command<API, ResourceSummary> {

    private Alias _alias;


    /**
     * Constructor.
     *
     * @param alias The alias to create.
     */
    public CreateAliasCommand(final Alias alias) {
        _alias = DBC.require().notNull(alias);
    }


    /** {@inheritDoc} */
    @Override
    public void invoke(final API subject,
                       final Callback<ResourceSummary> callback) {
        final String path = Globals.API_URL+InternalServices.api.aliases();

        final Request r =
            new Request(
                HttpMethod.POST,
                path,
                writeAlias(_alias),
                new CallbackResponseHandler<ResourceSummary>(
                    I18n.uiConstants.createAlias(),
                    callback,
                    resourceSummaryParser()));

        getExecutor().invokeRequest(r);
    }
}
