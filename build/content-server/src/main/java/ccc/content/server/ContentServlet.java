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
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import ccc.commons.DBC;
import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.commons.Resources;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
 *
 * @author Civic Computing Ltd
 */
public final class ContentServlet extends HttpServlet {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -5743085540949007873L;
    private Registry _registry = new JNDI();

    /**
     * Constructor.
     *
     * @param registry The registry for this servlet.
     */
    public ContentServlet(final Registry registry) {
        DBC.require().notNull(registry);
        _registry = registry;
    }

    /**
     * Constructor.
     */
    public ContentServlet() { /* NO-OP */ }

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
     *        response content-type;    // Done
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
        configureCharacterEncoding(response);

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
            case PAGE:
                final Page page = resource.asPage();
                write(resp, page);
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
     * @param page The content to write to the response.
     * @throws IOException If writing to the response fails.
     */
    void write(final HttpServletResponse resp,
               final Page page) throws IOException {

        final String template = lookupTemplateForResource(page);
        final String html = render(page, template);
        resp.setContentType("text/html");
        resp.getWriter().write(html);
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

        final String template = lookupTemplateForResource(folder);
        final String html = render(folder, template);
        resp.setContentType("text/html");
        resp.getWriter().write(html);
    }

    /**
     * Accessor for the resource manager.
     *
     * @return A ResourceManager.
     */
    ResourceManager resourceManager() {
        return _registry.get("ResourceManagerEJB/local");
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

    /**
     * Locate an appropriate template for rendering a resource as XHTML.
     *
     * @param resource The resource that will be transformed by the template.
     * @return The name of the resource as a string.
     */
    String lookupTemplateForResource(final Resource resource) {

        switch (resource.type()) {
            case PAGE:
                return
                Resources.readIntoString(
                    getClass().getResource("default-content-template.txt"),
                    Charset.forName("ISO-8859-1"));
            case FOLDER:
                return
                Resources.readIntoString(
                    getClass().getResource("default-folder-template.txt"),
                    Charset.forName("ISO-8859-1"));
            default:
                throw new CCCException(
                    "Unsupported resource type: "+resource.type());
        }
    }

    /**
     * Render a resource with the specified template.
     *
     * @param resource The resource that will be rendered.
     * @param template The template used to render the resource.
     * @return The html rendering as a string.
     */
    String render(final Resource resource, final String template) {

        final StringWriter html = new StringWriter();

        final Properties velocityProperties = new Properties();
        velocityProperties.setProperty(
            RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityProperties.setProperty(
            "runtime.log.logsystem.log4j.logger",
            getClass().getName());

        try {
            final VelocityEngine ve = new VelocityEngine(velocityProperties);
            ve.init();
            final VelocityContext context = new VelocityContext();
            context.put("resource", resource);

            ve.evaluate(context, html, "????", template);

        } catch (final ParseErrorException e) {
            throw new CCCException(e);
        } catch (final MethodInvocationException e) {
            throw new CCCException(e);
        } catch (final ResourceNotFoundException e) {
            throw new CCCException(e);
        } catch (final IOException e) {
            throw new CCCException(e);
        } catch (final Exception e) {
            throw new CCCException(e);
        }

        return html.toString();
    }

    /**
     * Set the character encoding for a response.
     *
     * @param response The response for which we'll set the char encoding.
     *
     */
    void configureCharacterEncoding(final HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
    }
}
