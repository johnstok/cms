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

import junit.framework.TestCase;
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
            vp.render(null, "#hello()", null);


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
        _vp.render(null, "foo", output, null);

        // ASSERT
        assertEquals("foo", output.toString());
    }

    /**
     * Test.
     */
    public void testRenderToString() {

        // ARRANGE

        // ACT
        final String output = _vp.render(null, "foo", null);

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
        final String html = _vp.render(foo, template, null);

        // ASSERT
        assertEquals("Hello "+foo.id(), html);
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
