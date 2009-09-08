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
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Wrapper class for converting third party exceptions to use
 * {@link CccCheckedException}.
 *
 * @author Civic Computing Ltd.
 */
public class UnexpectedException
    extends
        CccCheckedException {

    /**
     * Constructor.
     *
     * @param e The third party exception.
     */
    public UnexpectedException(final Exception e) {
        super("Unexpected exception.", e);
    }

    /** {@inheritDoc} */
    @Override
    public RestException toRemoteException() {
        return new RestException(
            new Failure(FailureCode.UNEXPECTED));
    }
}
