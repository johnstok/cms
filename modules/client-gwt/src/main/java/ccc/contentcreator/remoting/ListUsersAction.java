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
package ccc.contentcreator.remoting;

import java.util.ArrayList;
import java.util.Collection;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.UserCriteria;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ListUsersAction
    extends
        RemotingAction {

    private UserCriteria _uc;
    private int _pageNo;
    private int _pageSize;

    /**
     * Constructor.
     * @param order
     * @param string
     */
    public ListUsersAction(final UserCriteria uc,
                     final int pageNo,
                     final int pageSize,
                     final String string,
                     final String order) {
        super(USER_ACTIONS.viewUsers());
        _uc = uc;
        _pageNo = pageNo;
        _pageSize = pageSize;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final StringBuilder path = new StringBuilder();
        path.append("/users?page="+_pageNo
        +"&count="+_pageSize);
        if (null != _uc) {
            if (null != _uc.getEmail()) {
                path.append("&email="+encode(_uc.getEmail()));
            }
            if (null != _uc.getUsername()) {
                path.append("&username="+encode(_uc.getUsername()));
            }
            if (null != _uc.getGroups()) {
                path.append("&groups="+encode(_uc.getGroups()));
            }
        }
        return path.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {

        final JSONObject obj = JSONParser.parse(response.getText()).isObject();

        final int totalCount =
            (int) obj.get(JsonKeys.SIZE).isNumber().doubleValue();

        final JSONArray result =obj.get(JsonKeys.ELEMENTS).isArray();

        final Collection<UserDto> children =
            new ArrayList<UserDto>();
        for (int i=0; i<result.size(); i++) {
            children.add(new UserDto(new GwtJson(result.get(i).isObject())));
        }

        execute(children, totalCount);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param users The collection of users returned.
     */
    protected abstract void execute(Collection<UserDto> users, int totalCount);
}
