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
package ccc.client.actions;

import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceName;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Check that a resource name exists.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ResourceNameExistsAction
    extends
        RemotingAction<ResourceSummary> {

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
            "name", _resourceName.toString(), InternalServices.encoder);

    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final ResourceSummary r) {
        execute(null!=r);
    }


    /** {@inheritDoc} */
    @Override
    protected ResourceSummary parse(final Response response) {
        return readResourceSummary(response);
    }


    /**
     * Handle a successful execution.
     *
     * @param nameExists True if the resource name exists, false otherwise.
     */
    protected abstract void execute(boolean nameExists);
}
