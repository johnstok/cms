/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


/**
 * Filter providing support for the X-HTTP-Method-Override HTTP request header.
 *
 * @author Civic Computing Ltd.
 */
public class MethodOverrideFilter
    implements
        Filter {


    /** {@inheritDoc} */
    @Override public void destroy() { /* No-op */ }


    /** {@inheritDoc} */
    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) throws IOException,
                                                         ServletException {
        if (request instanceof HttpServletRequest) {
            chain.doFilter(
                new XHttpServletRequest((HttpServletRequest) request),
                response);
        } else {
            chain.doFilter(request, response);
        }
    }


    /** {@inheritDoc} */
    @Override public void init(final FilterConfig filterConfig) { /* No-op */ }
}
