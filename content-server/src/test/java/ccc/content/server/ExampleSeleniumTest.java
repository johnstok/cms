/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.content.server;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class ExampleSeleniumTest extends TestCase {

    private Selenium selenium;

    @Override
    public void setUp() throws Exception {
        final String url = "http://localhost:8080/";
        selenium = new DefaultSelenium("localhost", 5555, "*iexplore", url);
        selenium.start();
    }

    @Override
    protected void tearDown() throws Exception {
        selenium.stop();
    }

    public void testGoogle() throws Throwable {
        selenium.open("/content-server/content/");
        selenium.click("link=Home");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Downloads_content");
        selenium.waitForPageToLoad("30000");
        assertTrue(selenium.isTextPresent("Downloads"));
    }


}
