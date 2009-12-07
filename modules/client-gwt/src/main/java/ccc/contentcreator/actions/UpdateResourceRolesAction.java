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
package ccc.contentcreator.actions;

import java.util.List;
import java.util.UUID;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONString;


/**
 * Remote action for resource's roles updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceRolesAction
    extends
        RemotingAction {

    private final UUID _resource;
    private final List<String> _roles;


    /**
     * Constructor.
     *
     * @param roles The roles for the resource.
     * @param resource The resource to update.
     */
    public UpdateResourceRolesAction(final UUID resource,
                                      final List<String> roles) {
        super(GLOBALS.uiConstants().updateRoles(), RequestBuilder.POST);
        _resource = resource;
        _roles = roles;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_resource+"/roles";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final JSONArray body = new JSONArray();
        for (int i=0; i<_roles.size(); i++) {
            body.set(i, new JSONString(_roles.get(i)));
        }
        return body.toString();
    }
}
