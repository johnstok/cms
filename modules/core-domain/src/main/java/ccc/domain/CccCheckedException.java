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
package ccc.domain;

import ccc.rest.RestException;



/**
 * Abstract base class for CCC exceptions.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CccCheckedException extends Exception {

    /**
     * Constructor.
     */
    public CccCheckedException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause The exception cause.
     */
    public CccCheckedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public CccCheckedException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The exception cause.
     */
    public CccCheckedException(final Throwable cause) {
        super(cause);
    }

    /**
     * Convert a local exception to a remote exception.
     *
     * @return The corresponding remote exception.
     */
    public abstract RestException toRemoteException();
}
