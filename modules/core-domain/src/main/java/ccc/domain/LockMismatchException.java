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

import ccc.commons.DBC;
import ccc.services.api.ActionType;
import ccc.services.api.CCCRemoteException;


/**
 * An exception used to indicate that a resource is locked by another user.
 *
 * @author Civic Computing Ltd.
 */
public class LockMismatchException
    extends
        RemoteExceptionSupport {

    private final Resource _resource;


    /**
     * Constructor.
     *
     * @param resource The resource.
     */
    public LockMismatchException(final Resource resource) {
        DBC.require().notNull(resource);
        _resource = resource;
    }

    /**
     * Accessor for the resource.
     *
     * @return The resource.
     */
    public Resource resource() {
        return _resource;
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return "Mismatch confirming lock on "+_resource.id()+".";
    }

    /** {@inheritDoc} */
    @Override
    public CCCRemoteException toRemoteException(final ActionType action) {
        return new CCCRemoteException(
            CCCRemoteException.LOCK_MISMATCH, action, getUUID().toString());
    }
}
