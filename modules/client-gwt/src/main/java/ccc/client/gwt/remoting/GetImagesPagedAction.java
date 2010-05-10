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

import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.plugins.s11n.JsonKeys;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Get a list of all images from the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetImagesPagedAction
    extends
        RemotingAction {

    private final ResourceSummary _parent;
    private int _pageNo;
    private int _pageSize;
    private final String _name;

    /**
     * Constructor.
     *
     * @param parent The folder containing the images.
     * @param actionName Local-specific name for the action.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     */
    public GetImagesPagedAction(final String actionName,
                                final ResourceSummary parent,
                                final int pageNo,
                                final int pageSize) {
        _name = actionName;
        _parent = parent;
        _pageNo = pageNo;
        _pageSize = pageSize;

    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("count", new String[] {""+_pageSize});
        params.put("page", new String[] {""+_pageNo});
        return
            Globals.API_URL
            + _parent.images().build(params, new GWTTemplateEncoder());
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
                    public void onOK(final ccc.client.gwt.core.Response response) {
                        final JSONObject obj =
                            JSONParser.parse(response.getText()).isObject();

                        final int totalCount =
                            (int) obj.get(JsonKeys.SIZE).isNumber().doubleValue();

                        final JSONArray result =
                            obj.get(JsonKeys.ELEMENTS).isArray();
                        final Collection<File> files = new ArrayList<File>();
                        for (int i=0; i<result.size(); i++) {
                            files.add(
                                new File(
                                    new GwtJson(result.get(i).isObject())));
                        }

                        execute(files, totalCount);
                    }
                });
    }


    /**
     * Handle the data returned from the server.
     *
     * @param images The available images.
     * @param totalCount The total comments available on the server.
     */
    protected abstract void execute(Collection<File> images,
                                    int totalCount);
}
