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
package ccc.serialization;



/**
 * Exception indicating an attempt to change a snapshot to an invalid state.
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
     * @param message Error message.
     * @param cause The cause of the exception.
     */
    public InvalidSnapshotException(final String message,
                                    final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message Error message.
     */
    public InvalidSnapshotException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The cause of the exception.
     */
    public InvalidSnapshotException(final Throwable cause) {
        super(cause);
    }

}
