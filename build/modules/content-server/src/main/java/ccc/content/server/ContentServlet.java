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

import static ccc.commons.Strings.*;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.Registry;
import ccc.commons.Resources;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Maybe;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
 *
 * @author Civic Computing Ltd
 */
public final class ContentServlet extends CCCServlet {


    private static final Template BUILT_IN_PAGE_TEMPLATE =
        new Template(
            "BUILT_IN_PAGE_TEMPLATE",
            "BUILT_IN_PAGE_TEMPLATE",
            Resources.readIntoString(
                ContentServlet.class.getResource("default-page-template.txt"),
                Charset.forName("UTF-8")));

    private static final Template BUILT_IN_FOLDER_TEMPLATE =
        new Template(
            "BUILT_IN_FOLDER_TEMPLATE",
            "BUILT_IN_FOLDER_TEMPLATE",
            Resources.readIntoString(
                ContentServlet.class.getResource("default-folder-template.txt"),
                Charset.forName("UTF-8")));

    /**
     * Constructor.
     *
     * @param registry The registry for this servlet.
     */
    public ContentServlet(final Registry registry) {
        super(registry);
    }

    /**
     * Constructor.
     */
    public ContentServlet() { super(); }

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
     * TODO: Add tests that NULL and '/' pathInfo is handled correctly.
     *
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void doSafeGet(final HttpServletRequest request,
                           final HttpServletResponse response)
                    throws IOException, ServletException {

        final String pathString =
            removeTrailing('/',
                nvl(request.getPathInfo(), "/"));
        final ResourcePath contentPath = new ResourcePath(pathString); // 400

        final Maybe<Resource> resource = contentManager().lookup(contentPath);
        if (resource.isDefined()) {
            handleResource(response, request, resource.get());
        } else {
            dispatchNotFound(request, response); // 404
        }
    }

    /**
     * Accepts any type of resource and routes it to the appropriate
     * type-specific write() method.
     *
     * @param req The request.
     * @param resp The response.
     * @param resource The resource to handle.
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    void handleResource(final HttpServletResponse resp,
                                final HttpServletRequest req,
                                final Resource resource)
                         throws IOException, ServletException {

        switch (resource.type()) {

            case ALIAS:
                final Alias alias = ((Alias) resource);
                handleResource(resp, req, alias.target());
                break;

            case PAGE:
                final Page page = ((Page) resource);
                write(resp, page);
                break;

            case FOLDER:
                final Folder folder = ((Folder) resource);


                if (folder.hasAliases()) {
                    String currentURI = req.getRequestURI();
                    if (!currentURI.endsWith("/")) {
                        currentURI = currentURI+"/";
                    }
                    resp.sendRedirect(currentURI + folder.firstAlias().name());
                } else if (folder.hasPages()) {
                    String currentURI = req.getRequestURI();
                    if (!currentURI.endsWith("/")) {
                        currentURI = currentURI+"/";
                    }
                    resp.sendRedirect(currentURI + folder.firstPage().name());
                } else {
                    dispatchNotFound(req, resp);
                }
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

        disableCachingFor(resp);
        configureCharacterEncoding(resp);
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

        disableCachingFor(resp);
        configureCharacterEncoding(resp);
        final String template = lookupTemplateForResource(folder);
        final String html = render(folder, template);
        resp.setContentType("text/html");
        resp.getWriter().write(html);
    }

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
                dispatchError(request, response, e);
            }
        }
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
                return resource.computeTemplate(BUILT_IN_PAGE_TEMPLATE).body();
            case FOLDER:
                return BUILT_IN_FOLDER_TEMPLATE.body();
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
    public String render(final Resource resource, final String template) {
        return new VelocityProcessor().render(resource,
                                              contentManager().lookupRoot(),
                                              template);
    }
}
