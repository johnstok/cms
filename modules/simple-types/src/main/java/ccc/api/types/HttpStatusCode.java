/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.api.types;


/**
 * A list of HTTP status codes.
 *
 * @author Civic Computing Ltd.
 */
public final class HttpStatusCode {

    /** OK : int. */
    public static final int OK = 200;

    /** NO_CONTENT : int. */
    public static final int NO_CONTENT = 204;

    /** NOT_FOUND : int. */
    public static final int NOT_FOUND = 404;

    /** ERROR : int. */
    public static final int ERROR = 500;

    /** UNAUTHORIZED : int. */
    public static final int UNAUTHORIZED = 401;

    /** CONFLICT : int. */
    public static final int CONFLICT = 409;

    /** BAD_REQUEST : int. */
    public static final int BAD_REQUEST = 400;

    /** MS_IE6_1223 : int. */
    public static final int MS_IE6_1223 = 1223;


    /**
     * Constructor.
     */
    private HttpStatusCode() {
        super();
    }
}
