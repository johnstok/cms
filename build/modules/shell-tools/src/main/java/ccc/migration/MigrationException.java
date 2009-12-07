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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import ccc.domain.CCCException;



/**
 * Migration specific runtime exception.
 *
 * @author Civic Computing Ltd
 */
public class MigrationException extends CCCException {

    /**
     * Constructor.
     */
    public MigrationException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message The message.
     * @param cause The cause.
     */
    public MigrationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message The message.
     */
    public MigrationException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The cause.
     */
    public MigrationException(final Throwable cause) {
        super(cause);
    }

}
