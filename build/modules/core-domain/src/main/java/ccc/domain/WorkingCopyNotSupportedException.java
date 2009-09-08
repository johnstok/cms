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

import ccc.rest.RestException;
import ccc.types.DBC;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * This exception is thrown when a working copy command is attempted for a
 * resource that doesn't support working copies.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyNotSupportedException
    extends
        CccCheckedException {

    private final Resource _resource;


    /**
     * Constructor.
     *
     * @param resource The resource.
     */
    public WorkingCopyNotSupportedException(final Resource resource) {
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
        return
            "Resource "
            + _resource.id()
            + ", of type "
            + _resource.type()
            + " is not working copy aware.";
    }

    /** {@inheritDoc} */
    @Override
    public RestException toRemoteException() {
        return new RestException(
            new Failure(FailureCode.WC_UNSUPPORTED));
    }
}
