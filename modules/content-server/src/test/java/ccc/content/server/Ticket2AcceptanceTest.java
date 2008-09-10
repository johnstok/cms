/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.content.server;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


/**
 * Acceptance tests for ticket #2.
 *
 * @author Civic Computing Ltd.
 */
public final class Ticket2AcceptanceTest extends TestCase {

    private Selenium     _selenium;
    private final int    _port    = 5555;
    private final String _browser = "*custom firefox -P Selenium -no-remote";

    /**
     * Test.
     */
    public void testTemplatesFolderAccesibleViaAssetsView() {
        _selenium.setSpeed("1000");
        _selenium.open("/content-creator/");
        _selenium.click("//div[@id='content-navigator']/span"); // Close
        _selenium.click("//div[@id='assets-navigator']/span");  // Open
        _selenium.click("//div[@id='assets']/table/tbody/tr/td[6]/span");
        _selenium.click("//div[@id='templates']/table/tbody/tr/td[1]/div/div");
    }

    /**
     * Test.
     */
    public void testTemplatesFolderContainsMigratedTemplates() {
        _selenium.setSpeed("1000");
        _selenium.open("/content-creator/");
        _selenium.click("assets-navigator");
        _selenium.click("//div[@id='assets']/table/tbody/tr/td[2]/div");
        _selenium.click("//div[@id='templates']/table/tbody/tr/td[6]/span");
        _selenium.click(
            "//div[@id='ash_display.jsp']/table/tbody/tr/td[1]/div/div");
        _selenium.click(
            "//div[@id='ash_display_home.jsp']/table/tbody/tr/td[1]/div/div");
        _selenium.click(
            "//div[@id='ash_display_sectionhome.jsp']"
            + "/table/tbody/tr/td[1]/div/div");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        final String url = "http://localhost:8080/";
        _selenium = new DefaultSelenium("localhost", _port, _browser, url);
        _selenium.start();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        _selenium.stop();
    }
}
