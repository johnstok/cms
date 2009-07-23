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
package ccc.content.velocity;

import java.io.StringWriter;
import java.util.Date;

import junit.framework.TestCase;
import ccc.api.Paragraph;
import ccc.commons.Context;
import ccc.commons.TextProcessor;
import ccc.domain.Page;
import ccc.domain.ResourceName;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;


/**
 * Tests for the {@link VelocityProcessor} class.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessorTest extends TestCase {
    private static final Context EMPTY_CONTEXT = new Context(null, null, null);


    /**
     * Test.
     */
    public void testMacroLoading() {

        // ARRANGE
        final TextProcessor vp = new VelocityProcessor();

        // ACT
        final String actual =
            vp.render("#hello()", EMPTY_CONTEXT);


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
        _vp.render("foo", output, EMPTY_CONTEXT);

        // ASSERT
        assertEquals("foo", output.toString());
    }

    /**
     * Test.
     */
    public void testRenderToString() {

        // ARRANGE

        // ACT
        final String output = _vp.render("foo", EMPTY_CONTEXT);

        // ASSERT
        assertEquals("foo", output);
    }

    /**
     * Test.
     */
    public void testRenderResource() {

        // ARRANGE
        final Page foo =
            new Page(
                new ResourceName("foo"),
                "foo",
                null,
                _rm,
                Paragraph.fromText("bar", "baz"));
        final String template = "Hello $resource.id()";

        // ACT
        final String html =
            _vp.render(
                template,
                new Context(null, foo, null));

        // ASSERT
        assertEquals("Hello "+foo.id(), html);
    }

    /**
     * Test.
     */
    public void testRenderVelocityError() {

        // ARRANGE
        final Page foo = new Page(new ResourceName("foo"), "foo", null, _rm);
        final String template = "#macro failthis #end";
        final String expectedMessage = "A macro declaration requires at least "
            + "a name argumentVelocityProcessor";
        final StringWriter renderedOutput = new StringWriter();

        // ACT
        _vp.render(
            template,
            renderedOutput,
            new Context(null, foo, null));

        // ASSERT
        final String html = renderedOutput.toString();
        assertTrue(html.startsWith(expectedMessage));
    }

    /**
     * Test.
     */
    public void testRenderVelocityBadPath() {

        // ARRANGE
        final Page foo = new Page(new ResourceName("foo"), "foo", null, _rm);
        final String template = "$helper.path(\"badpath\")";
        final String expectedMessage =
            "Invocation of method 'path' in  "
            +"class ccc.content.velocity.VelocityHelper "
            +"threw exception ccc.domain.CCCException: badpath does not match "
            +"the regular expression: (/[\\.\\-\\w]+)* at VelocityProcessor"
            +"[line 1, column 9]";
        final StringWriter renderedOutput = new StringWriter();

        // ACT
        _vp.render(
            template,
            renderedOutput,
            new Context(null, foo, null));

        // ASSERT
        final String html = renderedOutput.toString();
        assertEquals(expectedMessage, html);

    }

    /**
     * Test.
     */
    public void testRenderVelocityNullPath() {

        // ARRANGE
        final Page foo = new Page(new ResourceName("foo"), "foo", null, _rm);
        final String template = "$helper.path(null)";
        final String expectedMessage =
            "Invocation of method 'path' in  "
            +"class ccc.content.velocity.VelocityHelper "
            + "threw exception java.lang.IllegalArgumentException: Specified "
            + "value may not be NULL. at VelocityProcessor[line 1, column 9]";
        final StringWriter renderedOutput = new StringWriter();

        // ACT
        _vp.render(
            template,
            renderedOutput,
            new Context(null, foo, null));

        // ASSERT
        final String html = renderedOutput.toString();
        assertEquals(expectedMessage, html);

    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _vp = new VelocityProcessor();
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _vp = null;
    }

    private TextProcessor _vp;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
