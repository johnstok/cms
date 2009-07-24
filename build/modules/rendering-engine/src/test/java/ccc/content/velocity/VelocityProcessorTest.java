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

import static org.easymock.EasyMock.*;

import java.io.StringWriter;
import java.util.Date;

import junit.framework.TestCase;
import ccc.api.Paragraph;
import ccc.domain.Page;
import ccc.domain.ResourceName;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.rendering.Context;
import ccc.rendering.TextProcessor;
import ccc.services.StatefulReader;


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
    public void testFileParsing() {

        // ARRANGE
        expect(_reader.fileContentsFromPath("/a/b/c", "UTF8"))
            .andReturn("#macro(foo)foo!#end");
        replay(_reader);
        final TextProcessor vp = new VelocityProcessor();

        // ACT
        final String actual =
            vp.render(
                "#parse('/a/b/c')\n#foo()", new Context(_reader, null, null));

        // ASSERT
        verify(_reader);
        assertEquals("foo!", actual);
    }


    /**
     * Test.
     */
    public void testFileInclude() {

        // ARRANGE
        expect(_reader.fileContentsFromPath("/a/b/c", "UTF8"))
            .andReturn("#macro(foo)foo!#end");
        replay(_reader);
        final TextProcessor vp = new VelocityProcessor();

        // ACT
        final String actual =
            vp.render(
                "#include('/a/b/c')\n#foo()", new Context(_reader, null, null));

        // ASSERT
        verify(_reader);
        assertEquals("#macro(foo)foo!#end#foo()", actual);
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
        _reader = createStrictMock(StatefulReader.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _reader = null;
        _vp = null;
    }

    private TextProcessor _vp;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
    private StatefulReader _reader;
}
