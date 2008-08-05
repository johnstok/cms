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
        selenium.setSpeed("5000"); // give time to tree expands

        selenium.open("/content-creator/");
        assertEquals("ContentCreator", selenium.getTitle());
        selenium.mouseDown("gwt-debug-folder_tree-root-child0-content");

        assertEquals("Home", selenium.getText("//body[@id='ext-gen6']/div/div/div[3]/table/tbody/tr[2]/td[2]"));
        selenium.doubleClick("gwt-debug-folder_tree-root-child0-content");
        assertTrue(selenium.isElementPresent("gwt-debug-Other-content"));
        selenium.mouseDown("gwt-debug-Other-content");
        selenium.click("//button[@type='button']");
        selenium.type("//input[@type='text']", "Media_content2");
        selenium.click("//body[@id='ext-gen6']/div[2]/div/table/tbody/tr[2]/td[2]/div/table/tbody/tr[4]/td/button");
        selenium.click("//button[@type='button']");
        assertEquals("Media_content2", selenium.getValue("//input[@type='text']"));
        selenium.type("//input[@type='text']", "Media_content");
        selenium.click("//body[@id='ext-gen6']/div[2]/div/table/tbody/tr[2]/td[2]/div/table/tbody/tr[4]/td/button");
    }
}
