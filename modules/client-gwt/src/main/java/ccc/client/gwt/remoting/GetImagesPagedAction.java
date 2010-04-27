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
import java.util.UUID;

import ccc.api.core.FileDto;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;
import ccc.plugins.s11n.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
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

    private final UUID _parentId;
    private int _pageNo;
    private int _pageSize;
    private final String _name;

    /**
     * Constructor.
     *
     * @param parentId The id of the folder.
     * @param actionName Local-specific name for the action.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     */
    public GetImagesPagedAction(final String actionName,
                                  final UUID parentId,
                                  final int pageNo,
                                  final int pageSize) {
        _name = actionName;
        _parentId = parentId;
        _pageNo = pageNo;
        _pageSize = pageSize;

    }


    @Override
    protected String getPath() {
        return "api/secure/files/images/"+_parentId+"/?page="+_pageNo+"&count="+_pageSize;
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
                        final JSONObject obj = JSONParser.parse(response.getText()).isObject();

                        final int totalCount =
                            (int) obj.get(JsonKeys.SIZE).isNumber().doubleValue();

                        final JSONArray result =obj.get(JsonKeys.ELEMENTS).isArray();
                        final Collection<FileDto> files = new ArrayList<FileDto>();
                        for (int i=0; i<result.size(); i++) {
                            files.add(new FileDto(new GwtJson(result.get(i).isObject())));
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
    protected abstract void execute(Collection<FileDto> images,
                                    int totalCount);
}
