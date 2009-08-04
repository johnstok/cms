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

import ccc.api.CommandFailedException;
import ccc.api.FailureCodes;


/**
 * Wrapper class for converting third party exceptions to use
 * {@link RemoteExceptionSupport}.
 *
 * @author Civic Computing Ltd.
 */
public class UnexpectedException
    extends
        RemoteExceptionSupport {

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
    public CommandFailedException toRemoteException() {
        return new CommandFailedException(
            FailureCodes.UNEXPECTED, getUUID().toString());
    }
}
