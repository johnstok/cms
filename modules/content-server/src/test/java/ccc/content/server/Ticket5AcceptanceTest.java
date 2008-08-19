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
import ccc.commons.XHTML;

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
     * TODO: This test doesn't make sense any more - delete or replace?
     * @throws IOException If the test can't access the test deployment.
     */
//    public void testValidXhtmlForFolder() throws IOException {
//
//        // ARRANGE
//        final InputStream page =
//            new URL("http://localhost:8080/content-server/content/")
//            .openStream();
//
//        // ACT
//        final boolean isValid = XHTML.isValid(page);
//        if (!isValid) {
//            XHTML.printErrors(
//                new URL("http://localhost:8080/content-server/content/")
//                .openStream(), System.out);
//        }
//
//        // ASSERT
//        assertTrue("Root folder did not supply valid XHTML", isValid);
//    }

    /**
     * Test.
     * @throws IOException If the test can't access the test deployment.
     */
    public void testCharactersAreEscapedCorrectly() throws IOException {

        // ARRANGE
        final InputStream page =
            new URL(
                "http://localhost:8080/content-server/content/"
                + "Our_Work/Our_Work/")
            .openStream();

        // ACT
        final String optionCount =
            XHTML.evaluateXPath(page, "count(//xhtml:h2)");

        // ASSERT
        assertEquals("5", optionCount);

    }

    /**
     * Test.
     */
    public void testTitlesAreSetCorrectly() {
        selenium.setSpeed("1000");
        selenium.open("content-server/content/Our_Work/Our_Work/");
        assertEquals("Our_Work", selenium.getTitle());
    }

    /**
     * Test.
     */
    public void testEachContentParagraphIsPresent() {
        selenium.setSpeed("1000");
        selenium.open("content-server/content/Our_Work/Our_Work/");
        selenium.waitForPageToLoad("30000");
        assertEquals("CONTENT", selenium.getText("//h2[1]"));
        assertEquals("HEADER", selenium.getText("//h2[3]"));
        assertEquals("Relationship", selenium.getText("//h2[5]"));

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
