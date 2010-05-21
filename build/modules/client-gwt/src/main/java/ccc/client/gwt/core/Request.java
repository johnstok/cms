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
package ccc.client.gwt.core;

import ccc.api.types.DBC;


/**
 * A HTTP request.
 *
 * @author Civic Computing Ltd.
 */
public class Request {

    private final HttpMethod      _method;
    private final String          _path;
    private final String          _body;
    private final ResponseHandler _callback;


    /**
     * Constructor.
     *
     * @param method   The request's HTTP method.
     * @param path     The request's path.
     * @param body     The request's body.
     * @param callback The callback fired when the request completes.
     */
    public Request(final HttpMethod method,
                   final String path,
                   final String body,
                   final ResponseHandler callback) {
        _method =   DBC.require().notNull(method);
        _path =     DBC.require().notNull(path);
        _body =     DBC.require().notNull(body);
        _callback = DBC.require().notNull(callback);
    }


    /**
     * Accessor.
     *
     * @return Returns the request method.
     */
    public HttpMethod getMethod() { return _method; }


    /**
     * Accessor.
     *
     * @return The request body, as a string.
     */
    public String getBody() { return _body; }


    /**
     * Accessor.
     *
     * @return The request path.
     */
    public String getPath() { return _path; }


    /**
     * Accessor.
     *
     * @return The callback to be invoked when the request completes.
     */
    public ResponseHandler getCallback() { return _callback; }
}
