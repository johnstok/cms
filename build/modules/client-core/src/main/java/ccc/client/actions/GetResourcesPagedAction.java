/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.types.Link;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Get paged child resources for specified folder.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetResourcesPagedAction
    extends
        RemotingAction<PagedCollection<ResourceSummary>> {

    private final int _pageNo;
    private final int _pageSize;
    private final ResourceCriteria  _criteria;

    /**
     * Constructor.
     *
     * @param criteria The criteria for the retrieve.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     */
    public GetResourcesPagedAction(final ResourceCriteria criteria,
                                  final int pageNo,
                                  final int pageSize) {
        super(UI_CONSTANTS.getChildrenPaged(), HttpMethod.POST);
        _criteria = criteria;
        _pageNo = pageNo;
        _pageSize = pageSize;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        final Map<String, String[]> params = new HashMap<String, String[]>();
        params.put("count", new String[] {""+_pageSize});
        params.put("page", new String[] {""+_pageNo});

        return
        new Link(ccc.api.synchronous.ResourceIdentifiers.Resource.SEARCH2
            + "?{-join|&|count,page,sort,order}")
        .build(params, InternalServices.ENCODER);
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writeResourceCriteria(_criteria);
    }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final PagedCollection<ResourceSummary> resources) {
        execute(resources.getElements(), resources.getTotalCount());

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
     * @param totalCount The total comments available on the server.
     */
    protected abstract void execute(Collection<ResourceSummary> children,
                                    long totalCount);
}
