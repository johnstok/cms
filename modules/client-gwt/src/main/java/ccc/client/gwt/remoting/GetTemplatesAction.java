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
import java.util.HashMap;
import java.util.Map;

import ccc.api.core.Template;
import ccc.api.types.Link;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.GwtJson;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.json.TemplateSerializer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Get a list of templates from the server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetTemplatesAction
    extends
        RemotingAction {

    private final String _name;

    /**
     * Constructor.
     *
     * @param actionName Local-specific name for the action.
     */
    public GetTemplatesAction(final String actionName) {
        _name = actionName;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("count", new String[] {"999"});
        params.put("page", new String[] {"1"});

        return
            new Request(
                HttpMethod.GET,
                Globals.API_URL
                    + new Link(GlobalsImpl.getAPI().templates())
                    .build(params, new GWTTemplateEncoder()),
                "",
                new ResponseHandlerAdapter(_name) {

                    /** {@inheritDoc} */
                    @Override public void onOK(final Response response) {

                        final JSONObject obj =
                            JSONParser.parse(response.getText()).isObject();
                        final JSONArray result =
                            obj.get(JsonKeys.ELEMENTS).isArray();

                        final Collection<Template> templates =
                            new ArrayList<Template>();
                        for (int i=0; i<result.size(); i++) {
                            final Template t =
                                new TemplateSerializer().read(
                                    new GwtJson(result.get(i).isObject()));
                            templates.add(t);
                        }
                        execute(templates);
                    }
                });
    }


    /**
     * Handle the data returned from the server.
     *
     * @param templates The available templates.
     */
    @Deprecated
    protected abstract void execute(Collection<Template> templates);
}
