/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

/**
 * Basic {@link CallbackHandler} implementation
 * that supports a username and a password.
 */
public class UserNamePasswordHandler implements CallbackHandler {

   private final String _username;
   private final String _password;

    /**
     * Constructor.
     *
     * @param theUsername The username.
     * @param thePassword The password.
     */
    public UserNamePasswordHandler(final String theUsername,
                                   final String thePassword) {
        _username = theUsername;
        _password = thePassword;
    }


    /** {@inheritDoc} */
    public void handle(final Callback[] callbacks)
    throws UnsupportedCallbackException {
        for(final Callback theCallback : callbacks){
            if (theCallback instanceof NameCallback) {
                ((NameCallback) theCallback).setName(_username);
            } else if (theCallback instanceof PasswordCallback) {
                ((PasswordCallback) theCallback).setPassword(
                    _password.toCharArray());
            }else {
                throw new UnsupportedCallbackException(theCallback);
            }
        }
    }
}
