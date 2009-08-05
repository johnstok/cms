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
package ccc.api;


/**
 * Failure codes for the public API.
 *
 * @author Civic Computing Ltd.
 */
public enum  FailureCode {
    UNEXPECTED,
    UNLOCKED,
    LOCK_MISMATCH,
    EXISTS,
    PRIVILEGES,
    WC_UNSUPPORTED,
    CYCLE;
}
