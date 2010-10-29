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
 * Revision      $Rev: 3212 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-10-14 14:54:42 +0100 (Thu, 14 Oct 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.Link;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Find a resource using its ID.
 *
 * @author Civic Computing Ltd.
 */
public abstract class FindResourceWithPathAction
    extends
        RemotingAction<Resource> {

    private final String _path;


    /**
     * Constructor.
     *
     * @param path The resource path to look up.
     */
    public FindResourceWithPathAction(final String path) {
        super(USER_ACTIONS.unknownAction());
        _path = path;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        // FIXME hard coded path
        return new Link("/secure/resources/by-path-secure{path}")
        .build("path", _path, InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Resource r) { execute(r); }


    /** {@inheritDoc} */
    @Override
    protected Resource parse(final Response response) {
        return readResource(response);
    }


    /**
     * Handle a successful execution.
     *
     * @param resource The corresponding resource or NULL if none exists.
     */
    protected abstract void execute(ResourceSummary resource);
}
