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

import ccc.commands.CommandFailedException;


/**
 * Abstract base class for CCC exceptions.
 * TODO: Rename.
 *
 * @author Civic Computing Ltd.
 */
public abstract class RemoteExceptionSupport extends Exception {

    /**
     * Constructor.
     */
    public RemoteExceptionSupport() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     * @param cause The exception cause.
     */
    public RemoteExceptionSupport(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message The exception message.
     */
    public RemoteExceptionSupport(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause The exception cause.
     */
    public RemoteExceptionSupport(final Throwable cause) {
        super(cause);
    }

    /**
     * Convert a local exception to a remote exception.
     *
     * @return The corresponding remote exception.
     */
    public abstract CommandFailedException toRemoteException();
}
