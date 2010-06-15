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
package ccc.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * A servlet action that prints details of each servlet request / response.
 *
 * @author Civic Computing Ltd.
 */
public class DebugFilter
    implements
        Filter {
    private static final Logger LOG =
        Logger.getLogger(DebugFilter.class);


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
                                        throws IOException, ServletException {

        final DebugHttpServletResponse resp =
            new DebugHttpServletResponse((HttpServletResponse) response);
        final HttpServletRequest  req  = (HttpServletRequest)  request;

        LOG.debug("URI: "+req.getRequestURI());
        LOG.debug("\tProtocol: "+req.getProtocol());
        LOG.debug("\tMethod: "+req.getMethod());
        LOG.debug("\tQuery: "+req.getQueryString());
        LOG.debug("\tPrincipal: "+req.getUserPrincipal());

        chain.doFilter(request, resp);

        LOG.debug("Response code: "+resp.getStatus());
        LOG.debug("Response location: "+resp.getLocation());
    }


    /** {@inheritDoc} */
    @Override
    public void destroy() { /* NO OP */ }


    /** {@inheritDoc} */
    @Override
    public void init(final FilterConfig filterConfig) { /* NO OP */ }
}
