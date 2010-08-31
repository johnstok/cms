/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.http;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.core.BaseClientResponse;

import ccc.api.core.Resource;
import ccc.api.core.Resources;
import ccc.api.jaxrs.ResourcesImpl;
import ccc.api.jaxrs.providers.RestExceptionMapper;
import ccc.api.types.DBC;
import ccc.api.types.HttpStatusCode;
import ccc.commons.HTTP;


/**
 * Resources decorator to work around RestEasy client bugs.
 *
 * @author Civic Computing Ltd.
 */
class ResourcesDecorator
    extends
        ResourcesImpl {

    private final String _base;
    private final HttpClient _http;


    /**
     * Constructor.
     *
     * @param delegate The resources implementation delegated to.
     * @param basePath The base path of the remote server.
     * @param http The HTTP client to use.
     */
    public ResourcesDecorator(final Resources delegate,
                              final String basePath,
                              final HttpClient http) {
        super(delegate);
        _base     = DBC.require().notNull(basePath);
        _http     = DBC.require().notNull(http);
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Resource resourceForPath(final String path) {
        /* This method works around an encoding issue in REST-EASY 1.1. */
        final String uri =
            ccc.api.core.ResourceIdentifiers.Resource.SEARCH_PATH_SIMPLE
            + path;
        final ClientRequest request = new ClientRequest(_base+uri, _http);

        BaseClientResponse response;
        try {
            response = (BaseClientResponse) request.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        if (response.getStatus() == HttpStatusCode.OK) {
            return (Resource) response.getEntity(Resource.class);
        }
        final String errorEntity =
            (String) response.getEntity(String.class);
        throw new RestExceptionMapper().fromResponse(errorEntity);

    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public String fileContentsFromPath(final String absolutePath, final String charset) {
        /* This method works around an encoding issue in REST-EASY 1.1. */
        final String uri =
            ccc.api.core.ResourceIdentifiers.Resource.TEXT_SIMPLE
            + absolutePath
            + "?charset=" + HTTP.encode(charset, "UTF-8");
        final ClientRequest request = new ClientRequest(_base+uri, _http);

        BaseClientResponse response;
        try {
            response = (BaseClientResponse) request.get();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        if (response.getStatus() == HttpStatusCode.OK) {
            return
            (String) response.getEntity(String.class);
        } else if (response.getStatus() == HttpStatusCode.NO_CONTENT) {
            return null;
        }

        final String errorEntity =
            (String) response.getEntity(String.class);
        throw new RestExceptionMapper().fromResponse(errorEntity);

    }
}
