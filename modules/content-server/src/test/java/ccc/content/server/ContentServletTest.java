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

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.commons.Resources;
import ccc.commons.VelocityProcessor;
import ccc.domain.Alias;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.ServiceNames;
import ccc.services.StatefulReader;

/**
 * Tests for the ContentServlet.
 * TODO: test redirect
 * TODO: test pathInfo = null
 * TODO: test pathInfo = '/'
 *
 * @author Civic Computing Ltd
 */
public final class ContentServletTest extends TestCase {

    private HttpServletResponse _response;
    private HttpServletRequest  _request;
    private StatefulReader _cm;
    private Resource _root = new Folder("_root");

    /**
     * Test.
     *
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    public void testHandleResourceHandlesAlias()
        throws IOException, ServletException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final ContentServlet cs =
            new ContentServlet(
                new MapRegistry(
                    ServiceNames.STATEFUL_READER,
                    _cm));
        final Template t = new Template("foo", "bar", "baz", "<fields/>");
        final Page p = new Page("bar");
        p.template(t);
        final Alias a = new Alias("foo", p);

        expect(_cm.lookup(new ResourcePath(""))).andReturn(_root);
        cs.disableCachingFor(_response);
        cs.configureCharacterEncoding(_response);
        _response.setContentType("text/html");
        expect(_response.getWriter()).andReturn(new PrintWriter(output));

        replay(_response, _request, _cm);

        // ACT
        cs.handle(_response, _request, a);

        // ASSERT
        verify(_response, _request, _cm);

    }

    /**
     * Test.
     * TODO: move to core-domain.
     */
    public void testRenderResource() {

        // ARRANGE
        final Page foo = new Page("foo");
        foo.addParagraph(Paragraph.fromText("bar", "baz"));
        final String template = "Hello $resource.id()";

        // ACT
        final String html = new VelocityProcessor().render(foo, null, template);

        // ASSERT
        assertEquals("Hello "+foo.id(), html);
    }

    /**
     * Test.
     */
    public void testLookupTemplateForContent() {

        // ARRANGE
        final String body =
            Resources.readIntoString(
                getClass().getResource("default-page-template.txt"),
                Charset.forName("ISO-8859-1"));
        final Template t =
            new Template(
                "foo",
                "bar",
                body,
                "<fields/>");
        final Page foo = new Page("foo");
        foo.template(t);

        // ACT
        final String templateName =
            new ContentServlet().lookupTemplateForResource(foo);

        // ASSERT
        assertEquals(
            t.body(),
            templateName);
    }

    /**
     * Test.
     */
    public void testLookupTemplateForFolder() {

        // ARRANGE
        final Folder foo = new Folder("foo");

        // ACT
        final String templateName =
            new ContentServlet().lookupTemplateForResource(foo);

        // ASSERT
        assertEquals(
            Resources.readIntoString(
                getClass().getResource("default-folder-template.txt"),
                Charset.forName("ISO-8859-1")),
            templateName);
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     */
    public void testContentRenderCorrectly() throws IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final String body =
            Resources.readIntoString(
                getClass().getResource("default-page-template.txt"),
                Charset.forName("ISO-8859-1"));
        final Template t =
            new Template(
                "foo",
                "bar",
                body,
                "<fields/>");
        final Page page = new Page("foo");
        page.template(t);
        page.addParagraph(Paragraph.fromText("key1", "para1"));
        page.addParagraph(Paragraph.fromText("key2", "para2"));

        new ContentServlet().disableCachingFor(_response);
        new ContentServlet().configureCharacterEncoding(_response);
        _response.setContentType("text/html");
        expect(_response.getWriter()).andReturn(new PrintWriter(output));
        replay(_response, _request);

        expect(_cm.lookup(new ResourcePath(""))).andReturn(_root);
        replay(_cm);

        // ACT
        new ContentServlet(
            new MapRegistry(
                ServiceNames.STATEFUL_READER,
                _cm)
            ).handle(_response, _request, page);

        // ASSERT
        verify(_response, _request, _cm);
        assertEquals(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head><title>foo</title></head>"
            + "<body>\r\n"
            + "<h1>foo</h1>\r\n"
            + "<h2>key2</h2><p>para2</p>\r\n"
            + "<h2>key1</h2><p>para1</p>\r\n"
            + "</body></html>",
            output.toString());
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesContent() throws ServletException, IOException {

        // ARRANGE
        final User u = new User("user");
        final StringWriter output = new StringWriter();
        final String body =
            Resources.readIntoString(
                getClass().getResource("default-page-template.txt"),
                Charset.forName("ISO-8859-1"));
        final Template t = new Template("foo", "bar", body, "<fields/>");
        final Page p =
            new Page("name")
                .addParagraph(Paragraph.fromText("Header", "<br/>"));
        p.publish(u);
        p.template(t);

        expect(_cm.lookup(new ResourcePath("/foo"))).andReturn(p);
        expect(_cm.lookup(new ResourcePath(""))).andReturn(_root);
        replay(_cm);

        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    ServiceNames.STATEFUL_READER,
                    _cm));

        // EXPECT
        new ContentServlet().disableCachingFor(_response);
        new ContentServlet().configureCharacterEncoding(_response);
        _response.setContentType("text/html");
        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_response.getWriter()).andReturn(new PrintWriter(output));
        replay(_request, _response);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response, _cm);
        assertEquals(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head><title>name</title></head>"
            + "<body>\r\n<h1>name</h1>\r\n<h2>Header</h2><p><br/></p>\r\n"
            + "</body></html>",
            output.toString());
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesFolderWithPages() throws ServletException,
                                                         IOException {

        // ARRANGE
        final User u = new User("user");
        final Folder foo = new Folder("foo");
        foo.publish(u);
        final Folder baz = new Folder("baz");
        baz.publish(u);
        final Page bar = new Page("bar");
        bar.publish(u);
        foo.add(baz);
        foo.add(bar);

        expect(_cm.lookup(new ResourcePath("/foo"))).andReturn(foo);
        replay(_cm);

        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    ServiceNames.STATEFUL_READER,
                    _cm));

        // EXPECT
        _response.sendRedirect("/content/foo/bar");
        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_request.getRequestURI()).andReturn("/content/foo");
        replay(_request, _response);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response, _cm);
        assertEquals("", output.toString());
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesFolderWithPagesTrailingSlash()
        throws ServletException, IOException {

        // ARRANGE
        final User u = new User("user");
        final Folder foo = new Folder("foo");
        foo.publish(u);
        final Folder baz = new Folder("baz");
        baz.publish(u);
        final Page bar = new Page("bar");
        bar.publish(u);
        foo.add(baz);
        foo.add(bar);

        expect(_cm.lookup(new ResourcePath("/foo"))).andReturn(foo);
        replay(_cm);

        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    ServiceNames.STATEFUL_READER,
                    _cm));

        // EXPECT
        _response.sendRedirect("/content/foo/bar");
        expect(_request.getPathInfo()).andReturn("/foo/");
        expect(_request.getRequestURI()).andReturn("/content/foo/");
        replay(_request, _response);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response, _cm);
        assertEquals("", output.toString());
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesFolderWithoutPages() throws ServletException,
                                                            IOException {

        // ARRANGE
        final RequestDispatcher rd = createStrictMock(RequestDispatcher.class);
        rd.forward(_request, _response);

        final Folder foo = new Folder("foo");
        final Folder baz = new Folder("baz");
        foo.add(baz);

        expect(_cm.lookup(new ResourcePath("/foo"))).andReturn(foo);
        replay(_cm);

        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    ServiceNames.STATEFUL_READER,
                    _cm
                ));

        // EXPECT
        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_request.getRequestDispatcher("/notfound")).andReturn(rd);
        replay(_request, _response, rd);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response, rd, _cm);
        assertEquals("", output.toString());
    }

    /**
     * Test.
     */
    public void testDisablementOfResponseCaching() {

        // ARRANGE
        _response.setHeader("Pragma", "no-cache");   // non-spec, but supported
        _response.setHeader(
            "Cache-Control",
            "private, must-revalidate, max-age=0"); // equivalent to 'no-cache'
        _response.setHeader("Expires", "0");
        replay(_response);

        // ACT
        new ContentServlet().disableCachingFor(_response);

        // VERIFY
        verify(_response);
    }

    /**
     * Test.
     */
    public void testCharacterEncodingIsSetToUtf8() {

        // ARRANGE
        _response.setCharacterEncoding("UTF-8");
        replay(_response);

        // ACT
        new ContentServlet().configureCharacterEncoding(_response);

        // VERIFY
        verify(_response);
    }


    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesUnpublishedContent() throws ServletException,
                                                            IOException {

        // ARRANGE
        final RequestDispatcher rd = createStrictMock(RequestDispatcher.class);
        rd.forward(_request, _response);

        final StringWriter output = new StringWriter();
        final Page p = new Page("name");

        expect(_cm.lookup(new ResourcePath("/foo"))).andReturn(p);
        replay(_cm);

        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    ServiceNames.STATEFUL_READER,
                    _cm));

        // EXPECT
        expect(_request.getPathInfo()).andReturn("/foo");
        expect(_request.getRequestDispatcher("/notfound")).andReturn(rd);
        replay(_request, _response, rd);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response, rd, _cm);
        assertEquals("", output.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _response = createStrictMock(HttpServletResponse.class);
        _request = createStrictMock(HttpServletRequest.class);
        _cm = createStrictMock(StatefulReader.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _response = null;
        _request = null;
        _cm = null;
    }
}
