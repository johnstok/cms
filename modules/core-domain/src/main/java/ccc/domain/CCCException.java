/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;


/**
 * A base class for CCC specific exceptions.
 *
 * @author Civic Computing Ltd
 */
public class CCCException extends RuntimeException {


    /**
     * Constructor.
     */
    public CCCException() { super(); }

    /**
     * Constructor.
     *
     * @param message The error message.
     */
    public CCCException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The root cause of the error.
     */
    public CCCException(final Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message The error message.
     * @param cause The root cause of the error.
     */
    public CCCException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
