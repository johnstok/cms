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
import ccc.api.types.Link;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Retrieve a template's delta.
 *
 * @author Civic Computing Ltd.
 */
public class PageDeltaAction
    extends
        RemotingAction<Page> {

    private final ResourceSummary _resource;
    private final String _name;


    /**
     * Constructor.
     *
     * @param actionName Name of the action.
     * @param resource The resource we need a delta for.
     */
    public PageDeltaAction(final String actionName,
                           final ResourceSummary resource) {
        super(actionName);
        _name = actionName;
        _resource = resource;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Globals.API_URL
            + new Link(_resource.getLink(Page.Links.WORKING_COPY))
              .build(InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest(final Callback<Page> callback) {
        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new CallbackResponseHandler<Page>(
                    _name,
                    callback,
                    new Parser<Page>() {
                        @Override public Page parse(final Response response) {
                            return readPage(response);
                        }}));
    }
}
