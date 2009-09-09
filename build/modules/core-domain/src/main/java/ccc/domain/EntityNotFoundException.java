/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.UUID;

import ccc.rest.RestException;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Exception indicating that the look up of an entity failed.
 *
 * @author Civic Computing Ltd.
 */
public class EntityNotFoundException
    extends
        CccCheckedException {

    private final UUID _id;

    /**
     * Constructor.
     *
     * @param id The entity's id.
     */
    public EntityNotFoundException(final UUID id) {
        _id = id;
    }

    /** {@inheritDoc} */
    @Override
    public RestException toRemoteException() {
        return new RestException(new Failure(FailureCode.NOT_FOUND));
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public UUID getId() {
        return _id;
    }
}
