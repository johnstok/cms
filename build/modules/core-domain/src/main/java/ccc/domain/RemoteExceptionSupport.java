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

import java.util.UUID;

import ccc.api.CommandFailedException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class RemoteExceptionSupport extends Exception {

    private UUID _id = UUID.randomUUID();

    /**
     * Constructor.
     */
    public RemoteExceptionSupport() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public RemoteExceptionSupport(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param message
     */
    public RemoteExceptionSupport(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause
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

    /**
     * Accessor for this exception's unique identifier.
     *
     * @return The unique identifier as a UUID.
     */
    public UUID getUUID() {
        return _id;
    }
}
