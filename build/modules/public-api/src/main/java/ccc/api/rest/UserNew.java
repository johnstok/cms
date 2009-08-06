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
package ccc.api.rest;

import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.Jsonable;
import ccc.api.UserDelta;


/**
 * A new user.
 *
 * @author Civic Computing Ltd.
 */
public class UserNew implements Jsonable {

    private final UserDelta _delta;
    private final String    _password;

    /**
     * Constructor.
     *
     * @param delta    The delta describing the user.
     * @param password The user's password.
     */
    public UserNew(final UserDelta delta, final String password) {
        _delta = delta;
        _password = password;
    }


    /**
     * Accessor.
     *
     * @return Returns the delta.
     */
    public final UserDelta getDelta() {
        return _delta;
    }


    /**
     * Accessor.
     *
     * @return Returns the password.
     */
    public final String getPassword() {
        return _password;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.PASSWORD, _password);
    }
}
