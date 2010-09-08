/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import ccc.api.core.ResourceSummary;
import ccc.api.types.Link;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.json.ResourceSummarySerializer;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Get paged resources.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetResourcesPagedAction
extends
RemotingAction{

    private final Link _link;
    private final int _pageNo;
    private final int _pageSize;
    private final String _sort;
    private final SortOrder _order;
    private final ResourceType _type;
    private final String _name;

    /**
     * Constructor.
     *
     * @param link The link to use for the search.
     * @param name The name of the resource.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @param sort The column to sort.
     * @param order The sort order (ASC/DESC).
     * @param type The resource type to fetch, null for all.
     */
    public GetResourcesPagedAction(final Link link,
                                  final String name,
                                  final int pageNo,
                                  final int pageSize,
                                  final String sort,
                                  final SortOrder order,
                                  final ResourceType type) {
        super(UI_CONSTANTS.getChildrenPaged());
        _link = link;
        _name = name;
        _pageNo = pageNo;
        _pageSize = pageSize;
        _sort = sort;
        _order = order;
        _type = type;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final HashMap<String, String[]> params =
            new HashMap<String, String[]>();
        params.put("name", new String[] {_name});
        params.put("sort", new String[] {_sort});
        params.put("order", new String[] {_order.name()});
        params.put("page", new String[] {""+_pageNo});
        params.put("count", new String[] {""+_pageSize});
        params.put("type", new String[] {(null==_type) ? null : _type.name()});
        return _link.build(params, new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {

        final JSONObject obj = JSONParser.parse(response.getText()).isObject();

        final int totalCount =
            (int) obj.get(JsonKeys.SIZE).isNumber().doubleValue();

        final JSONArray result =obj.get(JsonKeys.ELEMENTS).isArray();

        final Collection<ResourceSummary> children =
            new ArrayList<ResourceSummary>();
        for (int i=0; i<result.size(); i++) {
            children.add(
                new ResourceSummarySerializer().read(
                    new GwtJson(result.get(i).isObject())));
        }

        execute(children, totalCount);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param children The collection of folder children.
     * @param totalCount The total comments available on the server.
     */
    protected abstract void execute(Collection<ResourceSummary> children,
                                    int totalCount);
}
