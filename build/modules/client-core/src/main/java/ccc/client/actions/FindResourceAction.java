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

import java.util.UUID;

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
public abstract class FindResourceAction
    extends
        RemotingAction {

    private UUID _resourceId;

    /**
     * Constructor.
     *
     * @param resourceId The resource ID to look up.
     */
    public FindResourceAction(final UUID resourceId) {
        super(USER_ACTIONS.unknownAction());
        _resourceId = resourceId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return
            new Link(InternalServices.ROOTS.getLink("element")).build(
            "id", _resourceId.toString(), InternalServices.ENCODER);

    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary r = readResourceSummary(response);
        execute(r);
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) { execute(null); }


    /**
     * Handle a successful execution.
     *
     * @param resource The corresponding resource or NULL if none exists.
     */
    protected abstract void execute(ResourceSummary resource);
}
