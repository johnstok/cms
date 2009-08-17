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


/**
 * User's own details.
 *
 * @author Civic Computing Ltd.
 */
public class UserOwn implements Jsonable {

    private String _email;
    private final String    _password;

    /**
     * Constructor.
     *
     * @param email    The email of the user.
     * @param password The user's password.
     */
    public UserOwn(final String email, final String password) {
        _email = email;
        _password = password;
    }


    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    public final String getEmail() {
        return _email;
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
        json.set(JsonKeys.EMAIL, _email);
        json.set(JsonKeys.PASSWORD, _password);
    }
}
