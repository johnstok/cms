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
        selenium.setSpeed("1000");
        selenium.open("/content-creator/");
        assertEquals("ContentCreator", selenium.getTitle());
        selenium.mouseDown("gwt-debug-folder_tree-root-child0");
        assertEquals("Other", selenium.getText("gwt-debug-Other"));
        selenium.doubleClick("gwt-debug-folder_tree-root-child0");
        selenium.mouseDown("gwt-debug-Other");
        assertEquals("Media", selenium.getText("gwt-debug-children_grid-1-1"));
        selenium.click("//button[@type='button']");

        assertEquals("Media", selenium.getValue("//input[@type='text']"));
        selenium.type("//input[@type='text']", "Media_content2");

        selenium.selectFrame("xpath=//iframe[@id='gwt-debug-bodyRTACONTENT']");
        selenium.isElementPresent("//html/body");
        selenium.type("//html/body", "New Test Content");

        selenium.selectFrame("relative=top");
        selenium.click("gwt-debug-saveButton");

        selenium.click("//button[@type='button']");
        assertEquals("Media_content2", selenium.getValue("//input[@type='text']"));
        selenium.selectFrame("xpath=//iframe[@id='gwt-debug-bodyRTACONTENT']");
        assertEquals("New Test Content", selenium.getText("//html/body"));
        selenium.selectFrame("relative=top");
        
        selenium.type("//input[@type='text']", "Media");
        selenium.click("gwt-debug-saveButton");
    }
    
    /**
     * Test.
     */
    public void testContentEditLocalization() {
        selenium.setSpeed("1000");
        selenium.open("/content-creator?locale=fi");
        assertEquals("ContentCreator", selenium.getTitle());
        selenium.doubleClick("gwt-debug-folder_tree-root-child0");
        selenium.mouseDown("gwt-debug-Other");
        assertEquals("Media", selenium.getText("gwt-debug-children_grid-1-1"));
        
        assertEquals("Tyyppi", selenium.getText("gwt-debug-children_grid-0-0"));
        assertEquals("Otsikko", selenium.getText("gwt-debug-children_grid-0-1"));
        assertEquals("Toiminnat", selenium.getText("gwt-debug-children_grid-0-2"));
        selenium.click("//button[@type='button']");
        assertEquals("Päivitä sisältö", selenium.getText("gwt-debug-dialogBox-caption"));
    }
}
