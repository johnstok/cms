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

import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.api.types.CommandType;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.events.Event;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.json.PageSerializer;


/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public final class CreatePageAction
    extends
        RemotingAction {

    private final Page _page;


    /**
     * Constructor.
     *
     * @param page The page's content.
     */
    public CreatePageAction(final Page page) {
        _page = page;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return createPage(_page);
    }


    /**
     * Create a new page.
     *
     * @param page The page to create.
     *
     * @return The HTTP request to create a folder.
     */
    public Request createPage(final Page page) {
        final String path =  Globals.API_URL+InternalServices.API.pages();

        final Json json = InternalServices.PARSER.newJson(); // FIXME: Broken.
        new PageSerializer().write(json, page);

        return
            new Request(
                HttpMethod.POST,
                path,
                json.toString(),
                new PageCreatedCallback(
                    I18n.UI_CONSTANTS.createPage()));
    }


    /**
     * Callback handler for creating a page.
     *
     * @author Civic Computing Ltd.
     */
    public class PageCreatedCallback extends ResponseHandlerAdapter {

        /**
         * Constructor.
         *
         * @param name The action name.
         */
        public PageCreatedCallback(final String name) {
            super(name);
        }

        /** {@inheritDoc} */
        @Override
        public void onOK(final ccc.client.core.Response response) {
            final ResourceSummary rs = parseResourceSummary(response);
            final Event<CommandType> event =
                new Event<CommandType>(CommandType.PAGE_CREATE);
            event.addProperty("resource", rs);
            InternalServices.REMOTING_BUS.fireEvent(event);
        }
    }
}
