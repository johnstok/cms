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
import ccc.services.adaptors.ContentManagerAdaptor;


/**
 * Tests for the ContentServlet.
 *
 * @author Civic Computing Ltd
 */
public final class ContentServletTest extends TestCase {

    private HttpServletResponse response;
    private HttpServletRequest  request;

    /**
     * Test.
     */
    public void testRenderResource() {

        // ARRANGE
        final Page foo = new Page(new ResourceName("foo"));
        foo.addParagraph("bar", new Paragraph("baz"));
        final String template = "Hello $resource.id()";

        // ACT
        final String html = new ContentServlet().render(foo, template);

        // ASSERT
        assertEquals("Hello "+foo.id(), html);
    }

    /**
     * Test.
     */
    public void testLookupTemplateForContent() {

        // ARRANGE
        final Page foo = new Page(new ResourceName("foo"));

        // ACT
        final String templateName =
            new ContentServlet().lookupTemplateForResource(foo);

        // ASSERT
        assertEquals(
            Resources.readIntoString(
                getClass().getResource("default-content-template.txt"),
                Charset.forName("ISO-8859-1")),
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
     * @throws IOException If there is an error writing to the response.
     */
    public void testContentRenderCorrectly() throws IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final Page page = new Page(new ResourceName("foo"));
        page.addParagraph("key1", new Paragraph("para1"));
        page.addParagraph("key2", new Paragraph("para2"));

        response.setContentType("text/html");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(response);

        // ACT
        new ContentServlet().write(response, page);

        // ASSERT
        verify(response);
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
     * @throws IOException If there is an error writing to the response.
     */
    public void testFoldersRenderCorrectly() throws IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final Folder top = new Folder(new ResourceName("top"));
        top.add(new Folder(new ResourceName("child_a")));
        top.add(new Page(new ResourceName("child_b")));

        response.setContentType("text/html");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(response);

        // ACT
        new ContentServlet().write(response, top);

        // ASSERT
        verify(response);
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
     * @throws IOException If there is an error writing to the response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesContent() throws ServletException, IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    "ContentManagerEJB/local",
                new ContentManagerAdaptor() {

                    /** @see ContentManagerAdaptor#lookup(java.lang.String) */
                    @Override
                    public Resource lookup(final ResourcePath path) {
                        return
                            new Page(new ResourceName("name"))
                                .addParagraph("Header", new Paragraph("<br/>"));
                    }
                }));

        // EXPECT
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        expect(request.getPathInfo()).andReturn("/foo/");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        contentServlet.doGet(request, response);

        // VERIFY
        verify(request, response);
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
     * @throws IOException If there is an error writing to the response.
     * @throws ServletException If execution of the servlet fails.
     */
    public void testDoGetHandlesFolders() throws ServletException, IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final ContentServlet contentServlet =
            new ContentServlet(
                new MapRegistry(
                    "ContentManagerEJB/local",
                new ContentManagerAdaptor() {
                /** @see ContentManagerAdaptor#lookup(java.lang.String) */
                @Override
                public Resource lookup(final ResourcePath path) {

                    return new Folder(new ResourceName("foo"));
                }
            }));

        // EXPECT
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        expect(request.getPathInfo()).andReturn("/foo/");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        contentServlet.doGet(request, response);

        // VERIFY
        verify(request, response);
        assertEquals(
            "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\r\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head><title>Folder: foo</title></head>"
            + "<body>\r\n<h1>Folder: foo</h1>\r\n<ul>\r\n</ul>\r\n</body>"
            + "</html>", output.toString());
    }

    /**
     * Test.
     */
    public void testDisablementOfResponseCaching() {

        // ARRANGE
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        replay(response);

        // ACT
        new ContentServlet().disableCachingFor(response);

        // VERIFY
        verify(response);
    }

    /**
     * Test.
     */
    public void testCharacterEncodingIsSetToUtf8() {

        // ARRANGE
        response.setCharacterEncoding("UTF-8");
        replay(response);

        // ACT
        new ContentServlet().configureCharacterEncoding(response);

        // VERIFY
        verify(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        response = createMock(HttpServletResponse.class);
        request = createMock(HttpServletRequest.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        response = null;
        request = null;
    }
}
