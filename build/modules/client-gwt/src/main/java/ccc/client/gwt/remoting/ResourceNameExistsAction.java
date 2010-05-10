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

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceName;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;

import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ResourceNameExistsAction
    extends
        RemotingAction {

    private ResourceSummary _folder;
    private ResourceName _resourceName;

    /**
     * Constructor.
     * @param resourceName The resource name to check.
     * @param folder The folder to check.
     */
    public ResourceNameExistsAction(final ResourceSummary folder,
                                    final ResourceName resourceName) {
        super(USER_ACTIONS.checkUniqueResourceName());
        _folder = folder;
        _resourceName = resourceName;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _folder.exists().build(
            "name", _resourceName.toString(), new GWTTemplateEncoder());

    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final boolean nameExists =
            JSONParser.parse(response.getText()).isBoolean().booleanValue();
        execute(nameExists);
    }

    /**
     * Handle a successful execution.
     *
     * @param nameExists True if the resource name exists, false otherwise.
     */
    protected abstract void execute(boolean nameExists);
}
