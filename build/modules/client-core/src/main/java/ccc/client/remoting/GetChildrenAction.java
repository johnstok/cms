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
package ccc.client.remoting;

import java.util.Collection;

import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Remote action for children resource fetching.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetChildrenAction
    extends
        RemotingAction<PagedCollection<ResourceSummary>> {


    /**
     * Constructor.
     *
     * @param actionName The name of the action.
     */
    public GetChildrenAction(final String actionName) {
        super(actionName);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final PagedCollection<ResourceSummary> rs) {
        execute(rs.getElements());
    }


    /** {@inheritDoc} */
    @Override
    protected PagedCollection<ResourceSummary> parse(final Response response) {
        return parseResourceSummaries(response);
    }


    /**
     * Handle the result of a successful call.
     *
     * @param children The collection of folder children.
     */
    protected abstract void execute(Collection<ResourceSummary> children);
}
