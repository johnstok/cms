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
import ccc.types.CommandType;
import ccc.types.DBC;
import ccc.types.Failure;
import ccc.types.FailureCode;



/**
 * This exception indicates that a user attempted to perform an operation
 * without sufficient privileges.
 *
 * @author Civic Computing Ltd.
 */
public class InsufficientPrivilegesException
    extends
        CccCheckedException {

    private final CommandType _action;
    private final User _user;

    /**
     * Constructor.
     *
     * @param action The action that was disallowed.
     * @param user The user attempting to perform the action.
     */
    public InsufficientPrivilegesException(final CommandType action,
                                           final User user) {
        DBC.require().notNull(action);
        DBC.require().notNull(user);
        _action = action;
        _user = user;
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return
            "User "
            + _user.username()
            + _user.roles()
            + " may not perform action: "
            + _action;
    }

    /** {@inheritDoc} */
    @Override
    public RestException toRemoteException() {
        return new RestException(new Failure(FailureCode.PRIVILEGES));
    }
}
