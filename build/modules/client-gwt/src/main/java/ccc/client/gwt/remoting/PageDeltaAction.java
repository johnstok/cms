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

import java.util.UUID;

import ccc.api.dto.PageDelta;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Retrieve a template's delta.
 *
 * @author Civic Computing Ltd.
 */
public abstract class PageDeltaAction
    extends
        RemotingAction {

    private final UUID _id;
    private final String _name;


    /**
     * Constructor.
     *
     * @param actionName Name of the action.
     * @param id The UUID.
     */
    public PageDeltaAction(final String actionName, final UUID id) {
        _name = actionName;
        _id = id;
    }


    @Override
    protected String getPath() {
        return "api/secure/pages/" + _id + "/delta";
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                RequestBuilder.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(_name) {
                    /** {@inheritDoc} */
                    @Override
                    public void onOK(final Response response) {
                        final JSONObject result =
                            JSONParser.parse(response.getText()).isObject();
                        final PageDelta delta = new PageDelta(new GwtJson(result));
                        execute(delta);
                    }
                });
    }


    /**
     * Handle a successful execution.
     *
     * @param delta The delta returned by the server.
     */
    protected abstract void execute(PageDelta delta);
}
