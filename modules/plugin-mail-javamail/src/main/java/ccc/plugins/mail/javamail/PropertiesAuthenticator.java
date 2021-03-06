/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.mail.javamail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import ccc.api.types.DBC;


/**
 * A JavaMail authenticator that reads credentials from a {@link Properties}
 * object.
 *
 * @author Civic Computing Ltd.
 */
public class PropertiesAuthenticator
    extends
        Authenticator {

    /** USERNAME : String. */
    static final String USERNAME = "auth.username";
    /** PASSWORD : String. */
    static final String PASSWORD = "auth.password";

    private final Properties _props;


    /**
     * Constructor.
     *
     * @param props The properties object containing credentials.
     */
    public PropertiesAuthenticator(final Properties props) {
        _props = DBC.require().notNull(props);
    }



    /** {@inheritDoc} */
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        final String username = _props.getProperty(USERNAME);
        final String password = _props.getProperty(PASSWORD);
        return new PasswordAuthentication(username, password);
      }

}
