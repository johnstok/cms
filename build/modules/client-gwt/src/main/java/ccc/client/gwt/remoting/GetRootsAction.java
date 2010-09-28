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
package ccc.client.gwt.remoting;

import ccc.api.core.Folder;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.GlobalsImpl;


/**
 * Action for retrieving the root.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetRootsAction
    extends
        RemotingAction {


    public GetRootsAction() { super(USER_ACTIONS.internalAction()); }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return Globals.API_URL+GlobalsImpl.getAPI().getLink(Folder.ROOTS);
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {

        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(USER_ACTIONS.internalAction()) {

                    /** {@inheritDoc} */
                    @Override
                    public void onOK(final Response response) {
                        onSuccess(parseResourceSummaries(response));
                    }
                });
    }


    /**
     * Execute this action.
     *
     * @param root The root resource returned by the server.
     */
    protected abstract void onSuccess(
                                  final PagedCollection<ResourceSummary> roots);
}
