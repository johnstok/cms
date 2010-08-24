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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ccc.api.core.File;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;
import ccc.client.core.ResponseHandlerAdapter;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.plugins.s11n.json.Json;
import ccc.plugins.s11n.json.SerializerFactory;

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
        final String path =
            Globals.API_URL
            + _parent.images().build(params, new GWTTemplateEncoder());
        return path;
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
                        final Json json =
                            new GwtJson(
                                JSONParser.parse(
                                    response.getText()).isObject());
                        final PagedCollection<File> rsCollection =
                            SerializerFactory.create(PagedCollection.class).read(json);
                        execute(
                            rsCollection.getElements(),
                            rsCollection.getTotalCount());
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
                                    long totalCount);
}
