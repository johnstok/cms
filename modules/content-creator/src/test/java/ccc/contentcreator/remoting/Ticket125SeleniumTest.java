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
package ccc.contentcreator.remoting;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public final class Ticket125SeleniumTest extends TestCase {

    private Selenium selenium;
    private final int port = 5555;
    private final String browser = "*custom firefox -P Selenium -no-remote";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        final String url = "http://localhost:8080/";
        selenium = new DefaultSelenium("localhost", port, browser, url);
        selenium.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        selenium.stop();
    }

    /**
     * Test.
     * TODO: enable - doesn't work with v1.0beta1
     */
    public void testCreateFolder() {
//        selenium.setSpeed("1000");
//        selenium.open("/content-creator/");
//        selenium.click("//div[@id='content']/table/tbody/tr/td[2]/div");
//        selenium.mouseUpRightAt("//div[@id='Other']/table/tbody/tr/td[6]/span", "");
//        selenium.click("create-folder");
//        selenium.type("folder-name", "hello_world");
//        selenium.click("//table[@id='create-folder-ok']/tbody/tr/td[2]/em/button");
//        selenium.click("//div[@id='hello_world']/table/tbody/tr/td[1]/div/div");
    }
}
