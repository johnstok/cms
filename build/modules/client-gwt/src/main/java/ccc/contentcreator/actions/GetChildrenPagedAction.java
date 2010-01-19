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
package ccc.contentcreator.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.ResourceSummary;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * Get paged child resources for specified folder.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetChildrenPagedAction
extends
RemotingAction{

    private final UUID _parentId;
    private int _pageNo;
    private int _pageSize;
    private String _sort;
    private String _order;

    /**
     * Constructor.
     *
     * @param parentId The id of the parent folder.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     * @param sort The column to sort.
     * @param order The sort order (ASC/DESC).
     */
    public GetChildrenPagedAction(final UUID parentId,
                                  final int pageNo,
                                  final int pageSize,
                                  final String sort,
                                  final String order) {
        super(GLOBALS.uiConstants().getChildrenPaged());
        _parentId = parentId;
        _pageNo = pageNo;
        _pageSize = pageSize;
        _sort = sort;
        _order = order;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders/"+_parentId+"/children-paged?page="+_pageNo
        +"&count="+_pageSize+"&sort="+_sort+"&order="+_order;
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<ResourceSummary> children =
            new ArrayList<ResourceSummary>();
        for (int i=0; i<result.size(); i++) {
            children.add(
                new ResourceSummary(new GwtJson(result.get(i).isObject())));
        }

        execute(children);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param children The collection of folder children.
     */
    protected abstract void execute(Collection<ResourceSummary> children);
}
