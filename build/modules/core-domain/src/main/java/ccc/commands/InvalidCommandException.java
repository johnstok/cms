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
package ccc.commands;

import ccc.domain.CccCheckedException;
import ccc.rest.RestException;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Indicates a command was invoked with invalid arguments.
 *
 * @author Civic Computing Ltd.
 */
public class InvalidCommandException
    extends
        CccCheckedException {

    /** {@inheritDoc} */
    @Override
    public RestException toRemoteException() {
        return new RestException(new Failure(FailureCode.INVALID));
    }

}
