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

import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.plugins.s11n.json.PageSerializer;

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
        _name = actionName;
        _resource = resource;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            Globals.API_URL
            + _resource.self().build(new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                HttpMethod.GET,
                getPath(),
                "",
                new ResponseHandlerAdapter(_name) {
                    /** {@inheritDoc} */
                    @Override
                    public void onOK(final Response response) {
                        final JSONObject result =
                            JSONParser.parse(response.getText()).isObject();
                        final Page delta =
                            new PageSerializer().read(new GwtJson(result));
                        execute(delta);
                    }
                });
    }


    /**
     * Handle a successful execution.
     *
     * @param delta The delta returned by the server.
     */
    protected abstract void execute(Page delta);
}
