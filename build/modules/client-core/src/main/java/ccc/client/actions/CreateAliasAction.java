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

import ccc.api.core.Alias;
import ccc.api.core.ResourceSummary;
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
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateAliasAction
    extends
        RemotingAction<ResourceSummary> {

    private final Alias _alias;


    /**
     * Constructor.
     *
     * @param alias The new alias to create.
     */
    public CreateAliasAction(final Alias alias) {
        super(I18n.UI_CONSTANTS.createAlias());
        _alias = alias;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<ResourceSummary> callback) {
        final String path = Globals.API_URL+InternalServices.API.aliases();

        return
            new Request(
                HttpMethod.POST,
                path,
                writeAlias(_alias),
                new CallbackResponseHandler<ResourceSummary>(
                    I18n.UI_CONSTANTS.createAlias(),
                    callback,
                    new Parser<ResourceSummary>() {

                        @Override
                        public ResourceSummary parse(final Response response) {
                            return parseResourceSummary(response);
                        }}));
    }
}
