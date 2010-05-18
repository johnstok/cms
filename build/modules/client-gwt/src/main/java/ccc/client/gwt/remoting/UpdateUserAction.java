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

import ccc.api.core.User;
import ccc.api.temp.UserSerializer;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;
import ccc.client.gwt.core.ResponseHandlerAdapter;


/**
 * Remote action for user updating.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateUserAction
    extends
        RemotingAction {

    private final User _userDetails;


    /**
     * Constructor.
     * @param userDetails The updated user details.
     */
    public UpdateUserAction(final User userDetails) {
        _userDetails = userDetails;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return Globals.API_URL + _userDetails.self();
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        new UserSerializer().write(json, _userDetails);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {

        return
            new Request(
                HttpMethod.PUT,
                getPath(),
                getBody(),
                new ResponseHandlerAdapter(UI_CONSTANTS.editUser()) {
                    /** {@inheritDoc} */
                    @Override public void onNoContent(final ccc.client.gwt.core.Response response) {
                        done();
                    }
                });
    }

    protected abstract void done();
}
