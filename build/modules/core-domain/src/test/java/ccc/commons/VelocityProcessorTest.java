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
package ccc.commons;

import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;

import junit.framework.TestCase;
import ccc.domain.CCCException;
import ccc.domain.Page;
import ccc.domain.Paragraph;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessorTest extends TestCase {

    /**
     * Test.
     */
    public void testMacroLoading() {

        // ARRANGE
        final VelocityProcessor vp = new VelocityProcessor();

        // ACT
        final String actual =
            vp.render("#hello()", new HashMap<String, Object>());


        // ASSERT
        assertEquals("hello!", actual);
    }

    /**
     * Test.
     */
    public void testRenderToOutputStream() {

        // ARRANGE
        final StringWriter output = new StringWriter();

        // ACT
        _vp.render("foo", output, new HashMap<String, Object>());

        // ASSERT
        assertEquals("foo", output.toString());
    }

    /**
     * Test.
     */
    public void testRenderToString() {

        // ARRANGE

        // ACT
        final String output = _vp.render("foo", new HashMap<String, Object>());

        // ASSERT
        assertEquals("foo", output);
    }

    /**
     * Test.
     */
    public void testRenderResource() {

        // ARRANGE
        final Page foo = new Page("foo");
        foo.addParagraph(Paragraph.fromText("bar", "baz"));
        final String template = "Hello $resource.id()";

        // ACT
        final String html =
            _vp.render(
                template,
                Collections.<String, Object>singletonMap("resource", foo));

        // ASSERT
        assertEquals("Hello "+foo.id(), html);
    }

    /**
     * Test.
     */
    public void testRenderVelocityError() {

        // ARRANGE
        final Page foo = new Page("foo");
        final String template = "#macro failthis #end";
        final String expectedMessage =
            "Encountered \"#end\" at VelocityProcessor[line 1, column 17]";
        final StringWriter renderedOutput = new StringWriter();
        // ACT
        try {
            _vp.render(
                template,
                renderedOutput,
                Collections.<String, Object>singletonMap("resource", foo));
            fail("should throw CCCException");
        } catch (final CCCException e) {
            assertTrue(e.getMessage().startsWith(
                expectedMessage));
        }

        // ASSERT
        final String html = renderedOutput.toString();
        assertTrue(html.startsWith(expectedMessage));
    }

    /**
     * Test.
     */
    public void testRenderVelocityBadPath() {

        // ARRANGE
        final Page foo = new Page("foo");
        final String template = "$helper.path(\"badpath\")";
        final String expectedMessage =
            "Invocation of method 'path' in  class ccc.commons.VelocityHelper "
            +"threw exception ccc.domain.CCCException: badpath does not match "
            +"the regular expression: (/[\\.\\-\\w]+)* at VelocityProcessor"
            +"[line 1, column 9]";
        final StringWriter renderedOutput = new StringWriter();
        // ACT
        try {
            _vp.render(
                template,
                renderedOutput,
                Collections.<String, Object>singletonMap("resource", foo));
            fail("should throw CCCException");
        } catch (final CCCException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

        // ASSERT
        final String html = renderedOutput.toString();
        assertEquals(expectedMessage, html);

    }

    /**
     * Test.
     */
    public void testRenderVelocityNullPath() {

        // ARRANGE
        final Page foo = new Page("foo");
        final String template = "$helper.path(null)";
        final String expectedMessage =
            "Invocation of method 'path' in  class ccc.commons.VelocityHelper "
            + "threw exception java.lang.IllegalArgumentException: Specified "
            + "value may not be NULL. at VelocityProcessor[line 1, column 9]";
        final StringWriter renderedOutput = new StringWriter();
        // ACT
        try {
            _vp.render(
                template,
                renderedOutput,
                Collections.<String, Object>singletonMap("resource", foo));
            fail("should throw CCCException");
        } catch (final CCCException e) {
            assertEquals(expectedMessage, e.getMessage());
        }

        // ASSERT
        final String html = renderedOutput.toString();
        assertEquals(expectedMessage, html);

    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _vp = new VelocityProcessor();
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _vp = null;
    }

    private VelocityProcessor _vp;
}
