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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;
import ccc.domain.Content;
import ccc.domain.Folder;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;
import ccc.services.adaptors.ResourceManagerAdaptor;


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
     *
     * @throws IOException If there is an error writing to the response.
     */
    public void testContentRenderCorrectly() throws IOException {

        // ARRANGE
        final StringWriter output = new StringWriter();
        final Content content = new Content(new ResourceName("foo"));
        content.addParagraph("key1", new Paragraph("para1"));
        content.addParagraph("key2", new Paragraph("para2"));

        response.setContentType("text/html");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(response);

        // ACT
        new ContentServlet().write(response, content);

        // ASSERT
        verify(response);
        assertEquals(
            "<H1>foo</H1><H2>key1</H2><P>para1</P><H2>key2</H2><P>para2</P>",
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
        top.add(new Content(new ResourceName("child_b")));

        response.setContentType("text/html");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(response);

        // ACT
        new ContentServlet().write(response, top);

        // ASSERT
        verify(response);
        assertEquals(
            "<H1>top</H1>"
            + "<UL><LI><A href=\"child_a\">child_a</A></LI>"
            + "<LI>child_b</LI></UL>",
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
        final ContentServlet contentServlet = new ContentServlet() {

            /** serialVersionUID : long */
            private static final long serialVersionUID = 7146453466370673791L;

            /**
             * @see ccc.content.server.ContentServlet#resourceManager()
             */
            @Override
            protected ResourceManager resourceManager() {

                return new ResourceManagerAdaptor() {

                    /** @see ResourceManagerAdaptor#lookup(java.lang.String) */
                    @Override
                    public Resource lookup(final ResourcePath path) {
                        return
                            new Content(new ResourceName("name"))
                                .addParagraph("Header", new Paragraph("<br/>"));
                    }
                };
            }
        };

        // EXPECT
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        expect(request.getPathInfo()).andReturn("/foo");
        response.setContentType("text/html");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        contentServlet.doGet(request, response);

        // VERIFY
        verify(request, response);
        assertEquals(
            "<H1>name</H1><H2>Header</H2><P><br/></P>",
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
        final ContentServlet contentServlet = new ContentServlet() {

            /** serialVersionUID : long. */
            private static final long serialVersionUID = 2262124294292394015L;

            /**
             * @see ccc.content.server.ContentServlet#resourceManager()
             */
            @Override
            protected ResourceManager resourceManager() {

                return new ResourceManagerAdaptor() {

                    /** @see ResourceManagerAdaptor#lookup(java.lang.String) */
                    @Override
                    public Resource lookup(final ResourcePath path) {
                        return new Folder(new ResourceName("foo"));
                    }
                };
            }
        };

        // EXPECT
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        expect(request.getPathInfo()).andReturn("/foo");
        response.setContentType("text/html");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        contentServlet.doGet(request, response);

        // VERIFY
        verify(request, response);
        assertEquals("<H1>foo</H1><UL></UL>", output.toString());
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
