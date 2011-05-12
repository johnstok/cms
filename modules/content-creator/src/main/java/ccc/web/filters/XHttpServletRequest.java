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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * A HTTP servlet request wrapper that supports the 'X-HTTP-Method-Override'
 * header defined by Google.
 *
 * @author Civic Computing Ltd.
 */
class XHttpServletRequest
    extends
        HttpServletRequestWrapper {

    private final String _xHttpMethod;


    /**
     * Constructor.
     *
     * @param delegate The request implementation to delegate to.
     */
    XHttpServletRequest(final HttpServletRequest delegate) {
        super(delegate);
        _xHttpMethod = delegate.getHeader("X-HTTP-Method-Override");
    }


    /** {@inheritDoc} */
    @Override
    public String getMethod() {
        final String delegateMethod = getHttpServletRequest().getMethod();
        if ("POST".equals(delegateMethod)) { // Only correct for 'POST'.
            if ("PUT".equals(_xHttpMethod)) {    // Correct 'PUT'.
                return "PUT";
            } else if ("DELETE".equals(_xHttpMethod)) { // Correct 'DELETE'.
                return "DELETE";
            }
        }
        return delegateMethod;
    }


    private HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) super.getRequest();
    }
}
