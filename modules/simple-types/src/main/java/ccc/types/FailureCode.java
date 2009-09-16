/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;


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
    NOT_FOUND;
}
