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
package ccc.plugins.s11n;



/**
 * Exception indicating an attempt to change a snapshot to an invalid state.
 *
 * @author Civic Computing Ltd.
 */
public class S11nException
    extends
        RuntimeException {

    /**
     * Constructor.
     *
     * @param cause The cause of the exception.
     */
    public S11nException(final Throwable cause) {
        super("Invalid snapshot", cause);
    }


    /**
     * Constructor.
     *
     * @param detail The details of the exception.
     * @param cause The cause of the exception.
     */
    public S11nException(final String detail,
                                    final Throwable cause) {
        super("Invalid snapshot:\n"+detail, cause);
    }
}
