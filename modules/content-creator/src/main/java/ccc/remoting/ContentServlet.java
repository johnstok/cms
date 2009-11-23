/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.remoting;

import static ccc.commons.Strings.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.script.ScriptException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.commons.ScriptRunner;
import ccc.remoting.actions.SessionKeys;
import ccc.rendering.Context;
import ccc.rendering.NotFoundException;
import ccc.rendering.Response;
import ccc.rendering.velocity.VelocityProcessor;
import ccc.rest.Actions;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.extensions.FilesExt;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.search.SearchEngine;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


/**
 * The ContentServlet class serves CCC content.
 *
 * @author Civic Computing Ltd.
 */
public class ContentServlet
    extends
        HttpServlet {
    private static final Logger LOG = Logger.getLogger(ContentServlet.class);

    @EJB(name = SearchEngine.NAME) private transient SearchEngine _search;
    @EJB(name = Users.NAME)        private transient Users        _users;
    @EJB(name = Pages.NAME)        private transient PagesExt     _pages;
    @EJB(name = Folders.NAME)      private transient FoldersExt   _folders;
    @EJB(name = Files.NAME)        private transient FilesExt     _files;
    @EJB(name = Resources.NAME)    private transient ResourcesExt _resources;
    @EJB(name = Actions.NAME)      private transient Actions      _actions;
    @EJB(name = Templates.NAME)    private transient Templates    _templates;


    private String        _rootName          = null;
    private boolean       _respectVisibility = true;


    /** {@inheritDoc} */
    @Override
    public void init() {
        final ServletConfig cf = getServletConfig();
        _rootName = cf.getInitParameter("root_name");
        if ("false".equals(cf.getInitParameter("respect_visibility"))) {
            _respectVisibility = false;
        } else {
            _respectVisibility = true;
        }
    }


    /**
     * Get the content for the specified relative URI. This method reads the
     * value from {@link HttpServletRequest#getPathInfo()} and maps that to a
     * corresponding resource in CCC.
     *
     * {@inheritDoc}
     */
    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp) throws IOException {

        LOG.info(
            "Serving content for: "
            + req.getContextPath()
            + req.getServletPath()
            + req.getPathInfo());

        bindServices(req);

        final ResourcePath contentPath = determineResourcePath(req);
        final ResourceSnapshot resource = getSnapshot(req, contentPath);

        if (resource == null) {
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        if (ResourceType.FILE==resource.getType()) {

            final FileDto f = (FileDto) resource;

            if (f.isText() && f.isExecutable()) {
                invokeScript(req, resp, f);

            } else {
                renderResource(req, resp, resource);
            }
        } else {
            renderResource(req, resp, resource);
        }
    }


    /**
     * Determine the ResourcePath from a request's pathInfo.
     *
     * @param request The HTTP request.
     * @return The corresponding resource path.
     */
    public ResourcePath determineResourcePath(
                                             final HttpServletRequest request) {
        String pathString = request.getPathInfo();
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);
        LOG.info(
            "Resource path is /"+_rootName+pathString);

        if (ResourcePath.isValid(pathString)) {
            return new ResourcePath(pathString);
        }

        throw new NotFoundException();
    }


    // RenderResourceAction
    private void renderResource(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final ResourceSnapshot rs) throws IOException {

        final Context context = new Context();
        try {
            context.add("user", _users.loggedInUser());
        } catch (final RestException e) {
            throw new RuntimeException(e);
        }
        context.add("request",  request);
        context.add("response", response);
        context.add("services", new RequestScopeServiceLocator(request));
        context.add("resource", rs);

        final Response r = new TmpRenderer(_templates, _resources).render(rs);

        if (rs.isSecure()) { // Dont'cache secure pages.
            r.setExpiry(null);
        }

        r.write(response, context, new VelocityProcessor());
    }


    @SuppressWarnings("unchecked")
    private ResourceSnapshot getSnapshot(final HttpServletRequest request,
                                         final ResourcePath contentPath) {

        final Map<String, String[]> parameters = request.getParameterMap();

        try {
            if (_respectVisibility) {
                return _resources.resourceForPathSecure(
                    "/"+_rootName+contentPath);
            } else if (parameters.keySet().contains("wc")) {
                return _resources.workingCopyForPath(
                    "/"+_rootName+contentPath);
            } else if (parameters.keySet().contains("v")) {
                return _resources.revisionForPath(
                    "/"+_rootName+contentPath, request.getParameter("v"));
            } else {
                return _resources.resourceForPathSecure(
                    "/"+_rootName+contentPath);
            }
        } catch (final RestException e) {
            throw new NotFoundException();
        }
    }


    private void disableCaching(final HttpServletResponse resp) {
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-store, must-revalidate, max-age=0");
        resp.setDateHeader("Expires", new Date(0).getTime());
    }


    private void invokeScript(final HttpServletRequest req,
                              final HttpServletResponse resp,
                              final FileDto f) {

        try {
            final TextFileDelta tf = _files.get(f.getId());
            invokeScript(req, resp, tf.getContent());
        } catch (final RestException e) {
            // FIXME: How to handle this?
            throw new RuntimeException(e);
        }
    }


    private void invokeScript(final HttpServletRequest req,
                              final HttpServletResponse resp,
                              final String script) {
        try {
            disableCaching(resp);

            final Map<String, Object> context = new HashMap<String, Object>();
            context.put("request",  req);
            context.put("response", resp);
            context.put("services", new RequestScopeServiceLocator(req));
            context.put("user", _users.loggedInUser());

            new ScriptRunner().eval(script, context, resp.getWriter());

        } catch (final ScriptException e) {
            throw new RuntimeException("Error invoking script.", e);
        } catch (final IOException e) {
            throw new RuntimeException("Error invoking script.", e);
        } catch (final RestException e) {
            throw new RuntimeException("Error invoking script.", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
                                          throws ServletException, IOException {
        doGet(req, resp);
    }


    private void bindServices(final HttpServletRequest req) {
        // FIXME: Wrap the 'Ext' objects to remove access to their methods.
        req.setAttribute(SessionKeys.USERS_KEY,     _users);
        req.setAttribute(SessionKeys.FILES_KEY,     _files);
        req.setAttribute(SessionKeys.PAGES_KEY,     _pages);
        req.setAttribute(SessionKeys.RESOURCES_KEY, _resources);
        req.setAttribute(SessionKeys.FOLDERS_KEY,   _folders);
        req.setAttribute(SessionKeys.ACTIONS_KEY,   _actions);
        req.setAttribute(SessionKeys.SEARCH_KEY,    _search);
    }
}
