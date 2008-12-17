/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
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
