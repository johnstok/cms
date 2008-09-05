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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.commons.MapRegistry;
import ccc.commons.Resources;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.services.adaptors.ContentManagerAdaptor;


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

    /**
     * Test.
     * TODO: move to core-domain.
     */
    public void testRenderResource() {

        // ARRANGE
        final Page foo = new Page(new ResourceName("foo"));
        foo.addParagraph("bar", new Paragraph("baz"));
        final String template = "Hello $resource.id()";

        // ACT
        final String html = new VelocityProcessor().render(foo, template);

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
                getClass().getResource("default-content-template.txt"),
                Charset.forName("ISO-8859-1"));
        final Template t =
            new Template(
                "foo",
                "bar",
                body);
        final Page foo = new Page(new ResourceName("foo"));
        foo.displayTemplateName(t);

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
        final Folder foo = new Folder(new ResourceName("foo"));

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
                getClass().getResource("default-content-template.txt"),
                Charset.forName("ISO-8859-1"));
        final Template t =
            new Template(
                "foo",
                "bar",
                body);
        final Page page = new Page(new ResourceName("foo"));
        page.displayTemplateName(t);
        page.addParagraph("key1", new Paragraph("para1"));
        page.addParagraph("key2", new Paragraph("para2"));

        new ContentServlet().disableCachingFor(_response);
        new ContentServlet().configureCharacterEncoding(_response);
        _response.setContentType("text/html");
        expect(_response.getWriter()).andReturn(new PrintWriter(output));
        replay(_response);

        // ACT
        new ContentServlet().write(_response, page);

        // ASSERT
        verify(_response);
        assertEquals(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head><title>foo</title></head>"
            + "<body>\r\n"
            + "<h1>foo</h1>\r\n"
            + "<h2>key1</h2><p>para1</p>\r\n"
            + "<h2>key2</h2><p>para2</p>\r\n"
            + "</body></html>",
            output.toString());
    }

    /**
     * Test.
     *
     * @throws IOException If there is an error writing to the _response.
     */
    public void testFoldersRenderCorrectly() throws IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final Folder top = new Folder(new ResourceName("top"));
        top.add(new Folder(new ResourceName("child_a")));
        top.add(new Page(new ResourceName("child_b")));

        new ContentServlet().disableCachingFor(_response);
        new ContentServlet().configureCharacterEncoding(_response);
        _response.setContentType("text/html");
        expect(_response.getWriter()).andReturn(new PrintWriter(output));
        replay(_response);

        // ACT
        new ContentServlet().write(_response, top);

        // ASSERT
        verify(_response);
        assertEquals(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head><title>Folder: top</title></head>"
            + "<body>\r\n<h1>Folder: top</h1>\r\n"
            + "<ul>\r\n<li><a href=\"child_a/\">child_a</a></li>\r\n"
            + "<li><a href=\"child_b/\">child_b</a></li>\r\n</ul>\r\n"
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
        final StringWriter output = new StringWriter();
        final String body =
            Resources.readIntoString(
                getClass().getResource("default-content-template.txt"),
                Charset.forName("ISO-8859-1"));
        final Template t =
            new Template(
                "foo",
                "bar",
                body);
        final Page p =
            new Page(new ResourceName("name"))
                .addParagraph("Header", new Paragraph("<br/>"));
        p.displayTemplateName(t);
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    "ContentManagerEJB/local",
                new ContentManagerAdaptor() {
                    /** {@inheritDoc} */
                    @Override @SuppressWarnings("unchecked")
                    public Resource lookup(final ResourcePath path) {
                        return p;
                    }
                }));

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
        verify(_request, _response);
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
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder baz = new Folder(new ResourceName("baz"));
        final Page bar = new Page(new ResourceName("bar"));
        foo.add(baz);
        foo.add(bar);

        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    "ContentManagerEJB/local",
                new ContentManagerAdaptor() {
                /** {@inheritDoc} */ @Override @SuppressWarnings("unchecked")
                public Folder lookup(final ResourcePath path) {
                    return foo;
                }
            }));

        // EXPECT
        _response.sendRedirect("foo/bar");
        expect(_request.getPathInfo()).andReturn("/foo");
        replay(_request, _response);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response);
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
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder baz = new Folder(new ResourceName("baz"));
        foo.add(baz);

        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    "ContentManagerEJB/local",
                    new ContentManagerAdaptor() {
                        /** {@inheritDoc} */
                        @Override @SuppressWarnings("unchecked")
                        public Resource lookup(final ResourcePath path) {
                            return foo;
                        }
                    }));

        // EXPECT
        expect(_request.getPathInfo()).andReturn("/foo");
        _response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        replay(_request, _response);

        // ACT
        contentServlet.doGet(_request, _response);

        // VERIFY
        verify(_request, _response);
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
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _response = createMock(HttpServletResponse.class);
        _request = createMock(HttpServletRequest.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _response = null;
        _request = null;
    }
}
