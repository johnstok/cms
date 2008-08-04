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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import junit.framework.TestCase;
import ccc.commons.jee.XHTML;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;


/**
 * Acceptance tests for ticket #5.
 *
 * @author Civic Computing Ltd
 */
public final class Ticket5AcceptanceTest extends TestCase {

    private Selenium     selenium;
    private final int    port    = 5555;
    private final String browser = "*iexplore";

    /**
     * Test.
     * @throws IOException If the test can't access the test deployment.
     */
    public void testValidXhtmlForFolder() throws IOException {

        // ARRANGE
        final InputStream page =
            new URL("http://localhost:8080/content-server/content/")
            .openStream();

        // ACT
        final boolean isValid = XHTML.isValid(page);
        if (!isValid) {
            XHTML.printErrors(
                new URL("http://localhost:8080/content-server/content/")
                .openStream(), System.out);
        }

        // ASSERT
        assertTrue("Root folder did not supply valid XHTML", isValid);
    }

    /**
     * Test.
     * @throws IOException If the test can't access the test deployment.
     */
    public void testCharactersAreEscapedCorrectly() throws IOException {

        // ARRANGE
        final InputStream page =
            new URL(
                "http://localhost:8080/content-server/content/"
                + "Young_People/Tell_us_your_story/"
                + "Tell_us_your_story_content/")
            .openStream();

        // ACT
        final String optionCount =
            XHTML.evaluateXPath(page, "count(//xhtml:form)");

        // ASSERT
        assertEquals("1", optionCount);

    }

    /**
     * Test.
     */
    public void testTitlesAreSetCorrectly() {
        selenium.open("/content-server/content/");
        assertEquals("Folder: content", selenium.getTitle());
        selenium.click("link=Home");
        selenium.waitForPageToLoad("30000");
        assertEquals("Folder: Home", selenium.getTitle());
        selenium.click("link=blue_panel_content");
        selenium.waitForPageToLoad("30000");
        assertEquals("blue_panel_content", selenium.getTitle());
    }

    /**
     * Test.
     */
    public void testEachContentParagraphIsPresent() {
        selenium.open("/content-server/content/");
        selenium.click("link=Information_for_the_Public");
        selenium.waitForPageToLoad("30000");
        selenium.click("link=Your_Rights___Responsibilities_content");
        selenium.waitForPageToLoad("30000");
        assertEquals("SUMMARY", selenium.getText("//h2[1]"));
        assertEquals("CONTENT", selenium.getText("//h2[2]"));
        assertEquals("HEADER", selenium.getText("//h2[4]"));

    }

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
}
