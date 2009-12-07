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
package ccc.rendering.velocity;

import static org.easymock.EasyMock.*;

import java.io.StringWriter;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;

import ccc.commons.Context;
import ccc.commons.Testing;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.rendering.TextProcessor;
import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.SearchEngine;
import ccc.rest.Security;
import ccc.rest.ServiceLocator;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


/**
 * Tests for the {@link VelocityProcessor} class.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessorTest extends TestCase {

    /**
     * Test.
     */
    public void testFileParsing() {

        // ARRANGE
        expect(_reader.fileContentsFromPath("/a/b/c", "UTF8"))
            .andReturn("#macro(foo)foo!#end");
        replay(_reader);
        final TextProcessor vp = new VelocityProcessor();
        final Context ctxt = new Context();
        ctxt.add("services", new TestServiceLocator());

        // ACT
        final String actual = vp.render("#parse('/a/b/c')\n#foo()", ctxt);

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
        final Context ctxt = new Context();
        ctxt.add("services",  new TestServiceLocator());

        // ACT
        final String actual = vp.render("#include('/a/b/c')\n#foo()", ctxt);

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
        final Context ctxt = new Context();
        ctxt.add("services", Testing.stub(ServiceLocator.class));
        // ACT
        _vp.render("foo", output, ctxt);

        // ASSERT
        assertEquals("foo", output.toString());
    }

    /**
     * Test.
     */
    public void testRenderToString() {

        // ARRANGE
        final Context ctxt = new Context();
        ctxt.add("services", Testing.stub(ServiceLocator.class));
        // ACT
        final String output = _vp.render("foo", ctxt);

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
        final Context ctxt = new Context();
        ctxt.add("resource", foo);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        final String html = _vp.render(template, ctxt);

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
            + "a name argumentVelocityProcessor[line 1, column 1]";
        final StringWriter renderedOutput = new StringWriter();
        final Context ctxt = new Context();
        ctxt.add("resource", foo);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        try {
            _vp.render(
                template,
                renderedOutput,
                ctxt);
            fail();


        // ASSERT
        } catch (final RuntimeException e) {
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
            + "class ccc.rendering.velocity.VelocityProcessorTest "
            + "threw exception java.lang.RuntimeException: Fail. "
            + "at VelocityProcessor[line 1, column 11]";
        final StringWriter renderedOutput = new StringWriter();
        final Context ctxt = new Context();
        ctxt.add("resource", this);
        ctxt.add("services", Testing.stub(ServiceLocator.class));

        // ACT
        try {
            _vp.render(
                template,
                renderedOutput,
                ctxt);

        // ASSERT
        } catch (final RuntimeException e) {
            assertTrue(e.getCause() instanceof MethodInvocationException);
            assertTrue(e.getCause().getMessage().startsWith(expectedMessage));
        }

    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _vp = new VelocityProcessor();
        _reader = createStrictMock(ResourcesExt.class);
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
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
    private ResourcesExt _reader;


    private class TestServiceLocator implements ServiceLocator {
            @Override
            public Actions getActions() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public Files getFiles() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public Folders getFolders() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public Pages getPages() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public Resources getResources() {
                return _reader;
            }

            @Override
            public Templates getTemplates() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public Users getUsers() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            @Override
            public SearchEngine getSearch() {

                throw new UnsupportedOperationException("Method not implemented.");
            }

            /** {@inheritDoc} */
            @Override
            public Aliases getAliases() {
                throw new UnsupportedOperationException("Method not implemented.");
            }

            /** {@inheritDoc} */
            @Override
            public Security getSecurity() {
                throw new UnsupportedOperationException("Method not implemented.");
            }

    }
}
