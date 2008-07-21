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
public class ContentServletTest extends TestCase {

    private HttpServletResponse response;
    private HttpServletRequest  request;

    /**
     * Test.
     *
     * @throws IOException
     * @throws ServletException
     */
    public final void testDoGetHandlesContent() throws ServletException, IOException {

        // ARRANGE
        StringWriter output = new StringWriter();
        ContentServlet contentServlet = new ContentServlet() {

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
        expect(request.getPathInfo()).andReturn("/foo");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        contentServlet.doGet(request, response);

        // VERIFY
        verify(request, response);
        assertEquals("Content<br/>", output.toString());
    }

    /**
     * Test.
     *
     * @throws IOException
     * @throws ServletException
     */
    public final void testDoGetHandlesFolders() throws ServletException, IOException {

        // ARRANGE
        StringWriter output = new StringWriter();
        ContentServlet contentServlet = new ContentServlet() {

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
        expect(request.getPathInfo()).andReturn("/foo");
        expect(response.getWriter()).andReturn(new PrintWriter(output));
        replay(request, response);

        // ACT
        contentServlet.doGet(request, response);

        // VERIFY
        verify(request, response);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected final void setUp() throws Exception {

        super.setUp();
        response = createMock(HttpServletResponse.class);
        request = createMock(HttpServletRequest.class);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected final void tearDown() throws Exception {

        super.tearDown();
        response = null;
        request = null;
    }
}
