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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * A HTTP response wrapper for debugging.
 *
 * @author Civic Computing Ltd.
 */
class DebugHttpServletResponse
    extends
        HttpServletResponseWrapper {

    private int    _status;
    private String _location;


    /**
     * Constructor.
     *
     * @param response The response to wrap.
     */
    DebugHttpServletResponse(final HttpServletResponse response) {
        super(response);
    }


    /** {@inheritDoc} */
    @Override
    public void sendError(final int sc) throws IOException {
        _status = sc;
        super.sendError(sc);
    }


    /** {@inheritDoc} */
    @Override
    public void sendError(final int sc,
                          final String msg) throws IOException {
        _status = sc;
        super.sendError(sc, msg);
    }


    /** {@inheritDoc} */
    @Override
    public void setStatus(final int sc) {
        _status = sc;
        super.setStatus(sc);
    }


    /** {@inheritDoc} */
    @Override
    public void addHeader(final String name, final String value) {
        super.addHeader(name, value);
        if ("Location".equals(name)) { _location = value; }
    }


    /** {@inheritDoc} */
    @Override
    public void sendRedirect(final String location) throws IOException {
        super.sendRedirect(location);
        _status = SC_MOVED_TEMPORARILY;
        _location = encodeRedirectURL(location);
    }


    /**
     * Accessor.
     *
     * @return The response's HTTP status code, as an integer.
     */
    int getStatus() {
        return _status;
    }


    /**
     * Accessor.
     *
     * @return The response's 'Location' header, as a string.
     */
    String getLocation() {
        return _location;
    }
}
