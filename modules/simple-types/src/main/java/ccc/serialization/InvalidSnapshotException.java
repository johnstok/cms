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
     *
     * @param cause The cause of the exception.
     */
    public InvalidSnapshotException(final Throwable cause) {
        super("Invalid snapshot", cause);
    }

}
