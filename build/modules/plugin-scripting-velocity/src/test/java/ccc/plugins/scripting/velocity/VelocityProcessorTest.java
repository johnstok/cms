/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.scripting.velocity;

import static org.easymock.EasyMock.*;

import java.io.StringWriter;
import java.util.Collections;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;

import ccc.api.core.Page;
import ccc.api.synchronous.MemoryServiceLocator;
import ccc.api.synchronous.Resources;
import ccc.api.synchronous.ServiceLocator;
import ccc.api.types.Paragraph;
import ccc.commons.Testing;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.ProcessingException;
import ccc.plugins.scripting.Script;
import ccc.plugins.scripting.TextProcessor;


/**
 * Tests for the {@link VelocityProcessor} class.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessorTest extends TestCase {

    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFileParsing() throws Exception {

        // ARRANGE
        expect(_reader.fileContentsFromPath("/a/b/c", "UTF8"))
            .andReturn("#macro(foo)foo!#end");
        replay(_reader);
        final TextProcessor vp = new VelocityProcessor();
        final Context ctxt = new Context();
        ctxt.add("services", _sl);

        // ACT
        final String actual =
            vp.render(new Script("#parse('/a/b/c')\n#foo()", "test"), ctxt);

        // ASSERT
        verify(_reader);
        assertEquals("foo!", actual);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFileInclude() throws Exception {

        // ARRANGE
        expect(_reader.fileContentsFromPath("/a/b/c", "UTF8"))
            .andReturn("#macro(foo)foo!#end");
        replay(_reader);
        final TextProcessor vp = new VelocityProcessor();
        final Context ctxt = new Context();
        ctxt.add("services",  _sl);

        // ACT
        final String actual =
            vp.render(new Script("#include('/a/b/c')\n#foo()", "test"), ctxt);

        // ASSERT
        verify(_reader);
        assertEquals("#macro(foo)foo!#end#foo()", actual);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRenderToOutputStream() throws Exception {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final Context ctxt = new Context();
        ctxt.add("services", Testing.stub(ServiceLocator.class));
        // ACT
        _vp.render(new Script("foo", "test"), output, ctxt);

        // ASSERT
        assertEquals("foo", output.toString());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRenderToString() throws Exception {

        // ARRANGE
        final Context ctxt = new Context();
        ctxt.add("services", Testing.stub(ServiceLocator.class));
        // ACT
        final String output = _vp.render(new Script("foo", "test"), ctxt);

        // ASSERT
        assertEquals("foo", output);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testRenderResource() throws Exception {

        // ARRANGE
        final Page foo =
            new Page(
                null,
                "foo",
                null,
                "foo",
                "",
                false);
        foo.setParagraphs(
            Collections.singleton(Paragraph.fromText("bar", "baz")));
        final String template = "Hello $resource.getName()";
        final Context ctxt = new Context();
        ctxt.add("resource", foo);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(new Script(template, "test"), ctxt);

        // ASSERT
        assertEquals("Hello "+foo.getName(), html);
    }


    /**
     * Test.
     */
    public void testRenderVelocityError() {

        // ARRANGE
        final Page foo =
            new Page(
                null,
                "foo",
                null,
                "foo",
                "",
                false);
        foo.setParagraphs(
            Collections.singleton(Paragraph.fromText("bar", "baz")));
        final String template = "#macro failthis #end";
        final String expectedMessage = "A macro declaration requires at least "
            + "a name argumenttest[line 1, column 1]";
        final StringWriter renderedOutput = new StringWriter();
        final Context ctxt = new Context();
        ctxt.add("resource", foo);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        try {
            _vp.render(
                new Script(template, "test"),
                renderedOutput,
                ctxt);
            fail();


        // ASSERT
        } catch (final ProcessingException e) {
            assertEquals(
                "Error processing Velocity script 'test' "
                + "[line number 1, column number 1].",
                e.getMessage());
            assertTrue(e.getCause() instanceof ParseErrorException);
            assertTrue(e.getCause().getMessage().startsWith(expectedMessage));
        }

    }


    /**
     * Test.
     */
    public void testRenderVelocityBadPath() {

        // ARRANGE
        final String template = "$resource.failingMethod()";
        final String expectedMessage =
            "Invocation of method 'failingMethod' in  "
            + "class "+getClass().getName()+" "
            + "threw exception java.lang.RuntimeException: Fail. "
            + "at test[line 1, column 11]";
        final StringWriter renderedOutput = new StringWriter();
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        try {
            _vp.render(
                new Script(template, "test"),
                renderedOutput,
                ctxt);

        // ASSERT
        } catch (final ProcessingException e) {
            assertEquals(
                "Error processing Velocity script 'test' "
                + "[line number 1, column number 11].",
                e.getMessage());
            assertTrue(e.getCause() instanceof MethodInvocationException);
            assertTrue(e.getCause().getMessage().startsWith(expectedMessage));
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testVelocityEngineConvertsEnfToNull() throws Exception {

        // ARRANGE
        final String template = "$resource.list()";
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String result =
            _vp.render(
                new Script(template, "test"),
                ctxt);

        // ASSERT
        assertEquals("$resource.list()", result);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testSecurityPass() throws Exception {

        // ARRANGE
        final String template =
            "$uuid.fromString(\"f7627735-6276-45b1-a516-aa8a1f9f28f2\")";
        final String expectedMessage =
            "f7627735-6276-45b1-a516-aa8a1f9f28f2";
        final Context ctxt = new Context();
        ctxt.add("uuid", UUID.class);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(new Script(template, "test"), ctxt);

        // ASSERT
        assertEquals(expectedMessage, html);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testSecurityAllowsClassName() throws Exception {

        // ARRANGE
        final String template = "$resource.Class.Name";
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(new Script(template, "test"), ctxt);

        // ASSERT
        assertEquals(getClass().getName(), html);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testSecurityBlockedClassMethods() throws Exception {

        // ARRANGE
        final String template = "$resource.Class.Methods";
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(new Script(template, "test"), ctxt);

        // ASSERT
        assertEquals(template, html);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testSecurityBlockedClassClassLoader() throws Exception {

        // ARRANGE
        final String template = "$resource.Class.ClassLoader";
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(new Script(template, "test"), ctxt);

        // ASSERT
        assertEquals(template, html);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testSecurityBlockedNewInstance() throws Exception {

        // ARRANGE
        final String template =
            "$resource.Class.ClassLoader.loadClass('java.util.HashMap')"
            + ".newInstance().size()";
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(new Script(template, "test"), ctxt);

        // ASSERT
        assertEquals(template, html);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _vp = new VelocityProcessor();
        _reader = createStrictMock(Resources.class);
        _sl.setCommands(_reader);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _reader = null;
        _vp = null;
    }


    /**
     * Method that always fails.
     */
    public void failingMethod() {
        throw new RuntimeException("Fail.");
    }




    private TextProcessor _vp;
    private Resources _reader;
    private final MemoryServiceLocator _sl = new MemoryServiceLocator();
}
