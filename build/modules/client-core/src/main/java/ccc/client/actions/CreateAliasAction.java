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
import ccc.api.types.CommandType;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.AliasSerializer;


/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateAliasAction
    extends
        RemotingAction {

    private final Alias _alias;


    /**
     * Constructor.
     *
     * @param alias The new alias to create.
     */
    public CreateAliasAction(final Alias alias) {
        _alias = alias;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return createAlias(_alias);
    }


    /**
     * Create a new alias.
     *
     * @param alias The alias to create.
     *
     * @return The HTTP request to create an alias.
     */
    private Request createAlias(final Alias alias) {
        final String path = Globals.API_URL+InternalServices.API.aliases();

        final Json json = InternalServices.PARSER.newJson();
        new AliasSerializer().write(json, alias);

        return
            new Request(
                HttpMethod.POST,
                path,
                json.toString(),
                new AliasCreatedCallback(
                    I18n.UI_CONSTANTS.createAlias()));
    }



    /**
     * Callback handler for creating an alias.
     *
     * @author Civic Computing Ltd.
     */
    public class AliasCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         *
         * @param name The action name.
         */
        public AliasCreatedCallback(final String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final Response response) {
            final ResourceSummary newAlias = parseResourceSummary(response);
            final Event<CommandType> event =
                new Event<CommandType>(CommandType.ALIAS_CREATE);
            event.addProperty("resource", newAlias);
            InternalServices.REMOTING_BUS.fireEvent(event);
        }
    }
}
