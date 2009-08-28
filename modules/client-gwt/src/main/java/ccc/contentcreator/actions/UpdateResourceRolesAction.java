/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.types.ID;

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

    private final ID _resource;
    private final List<String> _roles;


    /**
     * Constructor.
     *
     * @param roles The roles for the resource.
     * @param resource The resource to update.
     */
    public UpdateResourceRolesAction(final ID resource,
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
