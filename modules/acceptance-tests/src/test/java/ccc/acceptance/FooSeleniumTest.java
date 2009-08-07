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
package ccc.acceptance;

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

