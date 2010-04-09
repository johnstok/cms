/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.tests.acceptance;

import com.thoughtworks.selenium.SeleneseTestCase;


/**
 * Login test for GWT client.
 *
 * @author Civic Computing Ltd.
 */
public class FooSeleniumTest
    extends
        SeleneseTestCase {

    /** {@inheritDoc} */
    @Override public void setUp() throws Exception {
        setUp("http://localhost:8080", "*chrome");
    }

    /**
     * Test.
     */
    public void testNew() {
        selenium.setSpeed("2000");
        selenium.open("/ash/creator/ContentCreator.jsp?dec=");
        selenium.type("username", "super");
        selenium.type("password", "sup3r2008");
        selenium.click("//button[@type='button']");
        assertEquals("Content Creator", selenium.getTitle());
    }
}

