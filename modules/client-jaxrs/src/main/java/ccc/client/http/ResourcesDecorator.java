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
package ccc.client.http;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import ccc.api.Resources;
import ccc.api.dto.ResourceSummary;
import ccc.api.jaxrs.ResourcesImpl;
import ccc.api.types.DBC;
import ccc.api.types.HttpStatusCode;


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
    @Override
    public ResourceSummary resourceForPath(final String path) {
        /*
         * This method works around an encoding issue in REST-EASY 1.1.
         * FIXME: Make use of RestExceptionMapper to throw correct exceptions.
         */

        final ClientRequest request =
            new ClientRequest(_base+"/resources/by-path"+path, _http);

        try {
            final ClientResponse<ResourceSummary> response =
                request.get(ResourceSummary.class);
            if (response.getStatus() == HttpStatusCode.OK) {
                return response.getEntity();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Request failed.");
    }
}
