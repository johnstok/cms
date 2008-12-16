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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.commons.Registry;
import ccc.commons.Resources;
import ccc.commons.VelocityProcessor;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.Template;


/**
 * The ContentServlet class serves CCC content. Typically the servlet will be
 * mounted at the servlet path '/*' in the web.xml config file. Only the HTTP
 * GET method is currently supported.
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
 * @author Civic Computing Ltd
 */
public final class ContentServlet extends CCCServlet {

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
     * {@inheritDoc}
     */
    @Override
    protected void doSafeGet(final HttpServletRequest request,
                           final HttpServletResponse response)
        throws IOException, ServletException {

        final String pathString =
            removeTrailing('/', nvl(request.getPathInfo(), "/"));
        final ResourcePath contentPath = new ResourcePath(pathString); // 400

        final Resource resource = resourceReader().lookup(contentPath);
        if (resource != null
                && resource.isPublished()
                && resource.isVisible()) {
            handle(response, request, resource);
        } else {
            dispatchNotFound(request, response); // 404
        }
    }

    /**
     * Accepts any type of resource and routes it to the appropriate
     * type-specific handle() method.
     *
     * @param req The request.
     * @param resp The response.
     * @param resource The resource to handle.
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    void handle(final HttpServletResponse resp,
                final HttpServletRequest req,
                final Resource resource) throws IOException, ServletException {

        switch (resource.type()) {

            case ALIAS:
                final Alias alias = resource.as(Alias.class);
                handle(resp, req, alias.target());
                break;

            case PAGE:
                final Page page = resource.as(Page.class);
                handle(resp, req, page);
                break;

            case FILE:
                final File f = resource.as(File.class);
                handle(resp, req, f);
                break;

            case FOLDER:
                final Folder folder = resource.as(Folder.class);
                handle(resp, req, folder);
                break;

            default:
                throw new CCCException("Unsupported resource type!");
        }
    }

    /**
     * Render a {@link Folder} to the response.
     *
     * @param req The request.
     * @param resp The response.
     * @param folder The folder to handle.
     * @throws IOException From servlet API.
     * @throws ServletException From servlet API.
     */
    void handle(final HttpServletResponse resp,
                final HttpServletRequest req,
                final Folder folder)
        throws IOException, ServletException {

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
    }

    /**
     * Render a {@link Page} to the response.
     *
     * @param req The request.
     * @param resp The response.
     * @param page The page to handle.
     * @throws IOException From servlet API.
     */
    void handle(final HttpServletResponse resp,
                final HttpServletRequest req,
                final Page page) throws IOException {

        disableCachingFor(resp);
        configureCharacterEncoding(resp);
        final String template = lookupTemplateForResource(page);
        final String html = renderResourceWithTemplate(page, template);
        resp.setContentType("text/html");
        resp.getWriter().write(html);
    }

    /**
     * Render a {@link File} to the response.
     * TODO: Should we close streams on exception???
     *
     * @param req The request.
     * @param resp The response.
     * @param f The file to handle.
     * @throws IOException From servlet API.
     */
    void handle(final HttpServletResponse resp,
                final HttpServletRequest req,
                final File f) throws IOException {

        disableCachingFor(resp);

        resp.setHeader(
            "Content-Disposition",
            "inline; filename=\""+f.name()+"\"");
        resp.setHeader(
            "Content-Type",
            f.mimeType().toString());
        resp.setHeader(
            "Content-Description",
            f.description());
        resp.setHeader(
            "Content-Length",
            String.valueOf(f.size()));

        final ServletOutputStream os = resp.getOutputStream();
        dataManager().retrieve(f.fileData(), os);
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
    String renderResourceWithTemplate(final Resource resource,
                                      final String template) {
        // TODO: Refactor
        final Folder root =
            resourceReader().lookup(new ResourcePath(""))
                            .as(Folder.class);

        return new VelocityProcessor().render(resource,
                                              root,
                                              template);
    }

    private static final Template BUILT_IN_PAGE_TEMPLATE =
        new Template(
            "BUILT_IN_PAGE_TEMPLATE",
            "BUILT_IN_PAGE_TEMPLATE",
            Resources.readIntoString(
                ContentServlet.class.getResource("default-page-template.txt"),
                Charset.forName("UTF-8")),
            "<fields/>");

    private static final Template BUILT_IN_FOLDER_TEMPLATE =
        new Template(
            "BUILT_IN_FOLDER_TEMPLATE",
            "BUILT_IN_FOLDER_TEMPLATE",
            Resources.readIntoString(
                ContentServlet.class.getResource("default-folder-template.txt"),
                Charset.forName("UTF-8")),
            "<fields/>");
}
