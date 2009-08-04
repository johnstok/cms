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
public class FailureCodes {
    /** UNEXPECTED : int. */
    public static final int UNEXPECTED    = 0;
    /** UNLOCKED : int. */
    public static final int UNLOCKED      = 1;
    /** LOCK_MISMATCH : int. */
    public static final int LOCK_MISMATCH = 2;
    /** EXISTS : int. */
    public static final int EXISTS        = 3;
    /** PRIVILEGES : int. */
    public static final int PRIVILEGES    = 4;
    /** WC_UNSUPPORTED : int. */
    public static final int WC_UNSUPPORTED = 5;
    /** CYCLE : int. */
    public static final int CYCLE = 6;
}
