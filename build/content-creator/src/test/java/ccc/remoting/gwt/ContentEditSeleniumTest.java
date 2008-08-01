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
package ccc.remoting.gwt;

import junit.framework.TestCase;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public final class ContentEditSeleniumTest extends TestCase {

    private Selenium selenium;
    private final int port = 5555;
    private final String browser = "*iexplore";

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
     */
    public void testContentEdit() {
        selenium.setSpeed("1000"); // give time to tree expands
        
        selenium.open("/content-creator/");
        assertEquals("ContentCreator", selenium.getTitle());
        selenium.clickAt("//div[@id='gwt-debug-folder_tree-root-child0']/table/tbody/tr/td[1]/img", "");
        selenium.clickAt("//div[@id='gwt-debug-folder_tree-root-child0']/div/div[3]/table/tbody/tr/td[1]/img", "");
        selenium.clickAt("gwt-uid-14", "25,11");
        selenium.click("//button[@type='button']");
        assertEquals("blue_panel_content", selenium.getValue("//input[@type='text']"));
        selenium.type("//input[@type='text']", "Test7");
        selenium.click("//td[2]/div/table/tbody/tr[4]/td/button");
        selenium.click("//button[@type='button']");
        assertEquals("Test7", selenium.getValue("//input[@type='text']"));
        selenium.clickAt("//input[@type='text']", "-1,13");
        selenium.type("//input[@type='text']", "");
        selenium.click("//td[2]/div/table/tbody/tr[4]/td/button");
        assertEquals("", selenium.getValue("//input[@type='text']"));
        selenium.type("//input[@type='text']", "blue_panel_content");
        selenium.click("//td[2]/div/table/tbody/tr[4]/td/button");
    }
}
