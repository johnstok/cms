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

import java.util.HashMap;

import ccc.commons.DBC;
import ccc.services.api.ActionType;
import ccc.services.api.CCCRemoteException;
import ccc.services.api.RemoteExceptionSupport;


/**
 * An exception used to indicate that a resource is unlocked.
 *
 * @author Civic Computing Ltd.
 */
public class UnlockedException
    extends
        Exception
    implements
        RemoteExceptionSupport {

    private final Resource _resource;


    /**
     * Constructor.
     *
     * @param resource The unlocked resource.
     */
    public UnlockedException(final Resource resource) {
        DBC.require().notNull(resource);
        _resource = resource;
    }

    /**
     * Accessor for the unlocked resource.
     *
     * @return The unlocked resource.
     */
    public Resource resource() {
        return _resource;
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return "Resource "+_resource.id()+" is Unlocked.";
    }

    /** {@inheritDoc} */
    @Override
    public CCCRemoteException toRemoteException(final ActionType action) {
        return new CCCRemoteException(1, action,new HashMap<String, String>());
    }
}
