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
package ccc.domain;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class InvalidSnapshotException
    extends
        RuntimeException {

    /**
     * Constructor.
     */
    public InvalidSnapshotException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public InvalidSnapshotException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message
     */
    public InvalidSnapshotException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause
     */
    public InvalidSnapshotException(final Throwable cause) {
        super(cause);
    }

}
