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
 package ccc.web.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A filter chain that ends a chain, capturing the request and response.
 *
 * @author Civic Computing Ltd.
 */
final class CapturingFilterChain
    implements
        FilterChain {

    private ServletRequest  _request;
    private ServletResponse _response;


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response) {
        setRequest(request);
        setResponse(response);
    }


    /**
     * Mutator.
     *
     * @param request The request to set.
     */
    public void setRequest(final ServletRequest request) {
        _request = request;
    }


    /**
     * Accessor.
     *
     * @return Returns the request.
     */
    public ServletRequest getRequest() {
        return _request;
    }


    /**
     * Mutator.
     *
     * @param response The response to set.
     */
    public void setResponse(final ServletResponse response) {
        _response = response;
    }


    /**
     * Accessor.
     *
     * @return Returns the response.
     */
    public ServletResponse getResponse() {
        return _response;
    }
}
