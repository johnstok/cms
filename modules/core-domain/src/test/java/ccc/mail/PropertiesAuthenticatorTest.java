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
package ccc.mail;

import java.util.Properties;

import javax.mail.PasswordAuthentication;

import junit.framework.TestCase;


/**
 * Tests for the {@link PropertiesAuthenticator} class.
 *
 * @author Civic Computing Ltd.
 */
public class PropertiesAuthenticatorTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testMissingPropertiesGiveNull() {

        // ARRANGE
        final PropertiesAuthenticator pa =
            new PropertiesAuthenticator(new Properties());

        // ACT
        final PasswordAuthentication credentials =
            pa.getPasswordAuthentication();

        // ASSERT
        assertNull(credentials.getUserName());
        assertNull(credentials.getPassword());
    }


    /**
     * Test.
     */
    public void testPropertiesGivesCorrectValues() {

        // ARRANGE
        final Properties config = new Properties();
        config.put(PropertiesAuthenticator.USERNAME, "un");
        config.put(PropertiesAuthenticator.PASSWORD, "pw");
        final PropertiesAuthenticator pa = new PropertiesAuthenticator(config);

        // ACT
        final PasswordAuthentication credentials =
            pa.getPasswordAuthentication();

        // ASSERT
        assertEquals("un", credentials.getUserName());
        assertEquals("pw", credentials.getPassword());
    }
}
