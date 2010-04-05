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

import java.util.UUID;

import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.core.Request;
import ccc.contentcreator.core.ResponseHandlerAdapter;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Retrieves the absolute path for a resource.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetAbsolutePathAction
    extends
        RemotingAction {

    private final UUID _resourceId;
    private final String _name;


    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     * @param resourceId The resource's id.
     */
    public GetAbsolutePathAction(final String actionName,
                                 final UUID resourceId) {
        _name = actionName;
        _resourceId = resourceId;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return
            new Request(
                RequestBuilder.GET,
                "api/secure/resources/"+_resourceId+"/path",
                "",
                new ResponseHandlerAdapter(_name) {

                    /** {@inheritDoc} */
                    @Override
                    public void onOK(final Response response) {
                        final String path = response.getText();
                        execute(path);
                    }
                });
    }


    /**
     * Handle the result of a successful call.
     *
     * @param path The path returned.
     */
    @Deprecated
    protected abstract void execute(String path);
}
