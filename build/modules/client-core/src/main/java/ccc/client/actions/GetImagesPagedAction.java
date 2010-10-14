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

import java.util.HashMap;
import java.util.Map;

import ccc.api.core.File;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceCriteria;
import ccc.api.types.Link;
import ccc.client.core.Callback;
import ccc.client.core.CallbackResponseHandler;
import ccc.client.core.Globals;
import ccc.client.core.HttpMethod;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.Parser;
import ccc.client.core.RemotingAction;
import ccc.client.core.Request;
import ccc.client.core.Response;


/**
 * Get a list of all images from the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetImagesPagedAction
    extends
        RemotingAction<PagedCollection<File>> {

    private final int _pageNo;
    private final int _pageSize;
    private final ResourceCriteria _criteria;

    /**
     * Constructor.
     *
     * @param criteria The criteria the search.
     * @param pageNo The page to display.
     * @param pageSize The number of results per page.
     */
    public GetImagesPagedAction(final ResourceCriteria criteria,
                                final int pageNo,
                                final int pageSize) {
        super(I18n.uiConstants.selectImage(), HttpMethod.POST);
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
        Globals.API_URL + new Link(InternalServices.api.images())
        .build(params, InternalServices.encoder);
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writeResourceCriteria(_criteria);
    }

    /** {@inheritDoc} */
    @Override
    protected Request getRequest(
                             final Callback<PagedCollection<File>> callback) {
        return
            new Request(
                HttpMethod.POST,
                getPath(),
                getBody(),
                new CallbackResponseHandler<PagedCollection<File>>(
                    I18n.uiConstants.selectImage(),
                    callback,
                    new Parser<PagedCollection<File>>() {
                        @Override
                        public PagedCollection<File> parse(
                                                    final Response response) {
                            return readFileSummaries(response);
                        }}));
    }
}
