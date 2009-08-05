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

import ccc.api.DBC;
import ccc.api.FailureCode;
import ccc.commands.CommandFailedException;


/**
 * An exception used to indicate that a resource is unlocked.
 *
 * @author Civic Computing Ltd.
 */
public class UnlockedException
    extends
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
    public CommandFailedException toRemoteException() {
        return new CommandFailedException(
            new Failure(FailureCode.UNLOCKED));
    }
}
