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

import ccc.api.DBC;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.Jsonable;



/**
 * A partial update, changing a user's password.
 *
 * @author Civic Computing Ltd.
 */
public class UserPasswordPU implements Jsonable {

    private final String _password;


    /**
     * Constructor.
     *
     * @param password The password to set.
     */
    public UserPasswordPU(final String password) {
        DBC.require().notEmpty(password);
        _password = password;
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
    @Override public void toJson(final Json json) {
        json.set(JsonKeys.PASSWORD, _password);
    }
}
