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

import java.util.ArrayList;
import java.util.Collection;

import ccc.api.core.Folder;
import ccc.api.core.ResourceSummary;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.json.ResourceSummarySerializer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Action for retrieving the roots.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetRootsAction
    extends
        RemotingAction {


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
                        final JSONObject obj =
                            JSONParser.parse(response.getText()).isObject();

                        final JSONArray results =
                            obj.get(JsonKeys.ELEMENTS).isArray();

                        final Collection<ResourceSummary> roots =
                            new ArrayList<ResourceSummary>();
                        for (int i=0; i<results.size(); i++) {
                            roots.add(
                                new ResourceSummarySerializer().read(
                                    new GwtJson(results.get(i).isObject())));
                        }
                        onSuccess(roots);
                    }
                });
    }


    /**
     * Execute this action.
     *
     * @param roots The root resources returned by the server.
     */
    protected abstract void onSuccess(final Collection<ResourceSummary> roots);
}
