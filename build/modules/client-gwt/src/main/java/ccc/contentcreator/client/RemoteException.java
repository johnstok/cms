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
package ccc.contentcreator.client;

import ccc.api.DBC;
import ccc.api.FailureCode;
import ccc.contentcreator.overlays.FailureOverlay;


/**
 * An exception class representing a remote failure.
 *
 * @author Civic Computing Ltd.
 */
public class RemoteException
    extends
        Exception {

    private final FailureOverlay _failure;


    /**
     * Constructor.
     *
     * @param failure The remote failure.
     */
    public RemoteException(final FailureOverlay failure) {
        DBC.require().notNull(failure);
        _failure = failure;
    }


    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return _failure.getCode()+": "+_failure.getId();
    }


    /**
     * Get the code for the exception.
     *
     * @return The failure code.
     */
    public FailureCode getCode() {
        return _failure.getCode();
    }
}
