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

import java.util.UUID;

import ccc.contentcreator.core.RemotingAction;

import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetAbsolutePathAction
    extends
        RemotingAction {

    private final UUID _resourceId;

    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     * @param resourceId The resource's id.
     */
    public GetAbsolutePathAction(final String actionName,
                                 final UUID resourceId) {
        super(actionName);
        _resourceId = resourceId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_resourceId+"/path";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final String path = response.getText();
        execute(path);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param path The path returned.
     */
    protected abstract void execute(String path);
}
