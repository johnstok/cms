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
package ccc.api.types;


/**
 * Failure codes for the public API.
 *
 * @author Civic Computing Ltd.
 */
public enum  FailureCode {

    /** UNEXPECTED : FailureCode. */
    UNEXPECTED,
    /** UNLOCKED : FailureCode. */
    UNLOCKED,
    /** LOCK_MISMATCH : FailureCode. */
    LOCK_MISMATCH,
    /** EXISTS : FailureCode. */
    EXISTS,
    /** PRIVILEGES : FailureCode. */
    PRIVILEGES,
    /** WC_UNSUPPORTED : FailureCode. */
    WC_UNSUPPORTED,
    /** CYCLE : FailureCode. */
    CYCLE,
    /** NOT_FOUND : FailureCode. */
    NOT_FOUND,
    /** INVALID : FailureCode. */
    INVALID,
    /** USER_NOT_FOUND : FailureCode. */
    USER_NOT_FOUND;
}
