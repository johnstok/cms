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
package ccc.types;


/**
 * A list of HTTP status codes.
 *
 * @author Civic Computing Ltd.
 */
public final class HttpStatusCode {


    /** OK : int. */
    public static final int OK = 200;


    /** IM_A_TEAPOT : int. */
    public static final int IM_A_TEAPOT = 418;


    /** NOT_FOUND : int. */
    public static final int NOT_FOUND = 404;


    /** ERROR : int. */
    public static final int ERROR = 500;


    /**
     * Constructor.
     */
    private HttpStatusCode() {
        super();
    }
}
