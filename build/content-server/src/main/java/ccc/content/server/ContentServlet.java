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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.jee.JNDI;
import ccc.domain.CCCException;
import ccc.domain.Content;
import ccc.domain.Folder;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ResourceManager;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
 *
 * @author Civic Computing Ltd
 */
public class ContentServlet extends HttpServlet {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -5743085540949007873L;



    /**
     * Get the content for the specified relative URI. This method reads the
     * value from {@link HttpServletRequest#getPathInfo()} and maps that to a
     * corresponding resource in CCC.
     *
     * TODO: Mime type setting
     * TODO: Locale setting:
     *  log [getLocale;
     *       Accept-Language
     *       session locale setting]
     * TODO: Character encoding:
     *  log: [request content-type;
     *        response content-type;
     *        Accept-Charset header;
     *        Accept-encoding header]
     *
     * TODO: Describe the mapping algorithm.
     * TODO: Handle bad resource paths - return 404?
     * TODO: Handle good path, no resource - return 404?
     * TODO: Markup escaping?
     * TODO: Handle standard errors -> converting to HTML.
     * TODO: Marshal CCCException to HTML.
     * TODO: How do we handle '/'? - return content root with folder template
     * TODO: Disable the default servlet.
     * TODO: Should be final but need to wait for resource injection...
     *
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void doSafeGet(final HttpServletRequest request,
                           final HttpServletResponse response)
                    throws IOException {

        disableCachingFor(response);

        final ResourcePath contentPath =
            new ResourcePath(request.getPathInfo());
        final Resource resource = resourceManager().lookup(contentPath);

        write(response, resource);
    }

    /**
     * Accepts any type of resource and routes it to the appropriate
     * type-specific write() method.
     */
    private void write(final HttpServletResponse resp,
                       final Resource resource) throws IOException {

        switch (resource.type()) {
            case CONTENT:
                final Content content = resource.asContent();
                write(resp, content);
                break;
            case FOLDER:
                final Folder folder = resource.asFolder();
                write(resp, folder);
                break;
            default:
                throw new CCCException("Unsupported resource type!");
        }
    }

    /**
     * Write content to the response.
     *
     * @param resp The response.
     * @param content The content to write to the response.
     * @throws IOException If writing to the response fails.
     */
    void write(final HttpServletResponse resp,
                       final Content content) throws IOException {

        resp.setContentType("text/html");
        final PrintWriter pw = resp.getWriter();

        pw.write("<H1>");
        pw.write(content.name().toString());
        pw.write("</H1>");

        for (final Map.Entry<String, Paragraph> paragraph
                : content.paragraphs().entrySet()) {
            pw.write("<H2>");
            pw.write(paragraph.getKey());
            pw.write("</H2>");
            pw.write("<P>");
            pw.write(paragraph.getValue().body());
            pw.write("</P>");
        }
    }

    /**
     * Write a folder to the response.
     *
     * @param resp The response.
     * @param folder The content to write to the response.
     * @throws IOException If writing to the response fails.
     */
    void write(final HttpServletResponse resp,
                       final Folder folder) throws IOException {

        resp.setContentType("text/html");
        final PrintWriter pw = resp.getWriter();
        pw.write("<H1>");
        pw.write(folder.name().toString());
        pw.write("</H1>");
        pw.write("<UL>");
        for (final Resource entry : folder.entries()) {
            pw.write("<LI>");
            if (entry.type() == ResourceType.FOLDER) {
                pw.write("<A href=\"");
                pw.write(entry.name().toString());
                pw.write("\">");
                pw.write(entry.name().toString());
                pw.write("</A>");
            } else {
                pw.write(entry.name().toString());
            }
            pw.write("</LI>");
        }
        pw.write("</UL>");
    }

    /**
     * Accessor for the resource manager.
     *
     * @return A ResourceManager.
     */
    ResourceManager resourceManager() {
        return JNDI.get("ResourceManagerEJB/local");
    }

    /* --------------------------------------------------------------------
     * Helper methods - will be refactored to an abstract base class in due
     * course.
     * --------------------------------------------------------------------
     */

    /**
     * Process a GET request.
     *
     * This method provides error handling for both committed and uncommitted
     * responses. It delegates to
     * {@link #doSafeGet(HttpServletRequest, HttpServletResponse)}
     * for implementation of business logic.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                               final HttpServletResponse response)
                        throws ServletException,
                               IOException {

        try {
            doSafeGet(request, response);

        } catch (final RuntimeException e) {
            if(response.isCommitted()) {
                /*
                 * Nothing we can do to rescue the response - the HTTP response
                 * code + headers has already been sent. Just log the error on
                 * the server.
                 */
                getServletContext().log(
                    "Error caught after response was committed.",
                    e);

            } else {
                getServletContext().log(
                    "Error caught on uncommited response"
                    + " - sending error message.",
                    e);
                request.setAttribute(SessionKeys.EXCEPTION_KEY, e);
                request
                    .getRequestDispatcher("/error")
                    .forward(request, response);
            }
        }
    }

    /**
     * Disable caching for the response.
     *
     * @param response The response that should not be cached.
     */
    void disableCachingFor(final HttpServletResponse response) {
        // TODO Add setting of 'Expires' header to some time in the past?
        response.setHeader("Pragma", "no-cache"); // Mostly useless
        response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
    }

}
