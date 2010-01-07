/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.commons.Context;
import ccc.remoting.actions.SessionKeys;
import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.NotFoundException;
import ccc.rendering.Response;
import ccc.rendering.velocity.VelocityProcessor;
import ccc.rest.Actions;
import ccc.rest.Comments;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.SearchEngine;
import ccc.rest.Templates;
import ccc.rest.UnauthorizedException;
import ccc.rest.Users;
import ccc.rest.dto.UserDto;
import ccc.rest.extensions.FilesExt;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.rest.snapshots.ResourceSnapshot;
import ccc.types.ResourcePath;


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
    @EJB(name = Comments.NAME)     private transient Comments     _comments;

    private boolean       _respectVisibility = true;


    /** {@inheritDoc} */
    @Override
    public void init() {
        final ServletConfig cf = getServletConfig();
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

        final String contentPath = determineResourcePath(req);
        final boolean wc = req.getParameterMap().keySet().contains("wc");
        final Integer version = determineVersion(req);
        LOG.info("[wc="+wc+", v="+version+"]");

        final ResourceSnapshot resource = getSnapshot(contentPath, wc, version);

        if (null == resource) {
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        final Response r =
            new TmpRenderer(_files, _templates, _resources).render(resource);

        if (resource.isSecure()       // Don't cache secure pages.
            || !_respectVisibility) { // Don't cache previews or working copies.
            r.setExpiry(null);
        }

        final Context context = createContext(req, resp, resource);
        r.write(resp, context, new VelocityProcessor());
    }


    private Integer determineVersion(final HttpServletRequest req) {
        final String version = req.getParameter("v");
        if (null==version) {
            return null;
        }
        try {
            final Integer v = new Integer(version);
            if (v.intValue()<0) {
                throw new NotFoundException();
            }
            return v;

        } catch (final NumberFormatException e) {
            throw new NotFoundException();
        }
    }


    private String determineResourcePath(final HttpServletRequest request) {
        String pathString = request.getPathInfo();
        pathString = nvl(pathString, "/");
        pathString = removeTrailing('/', pathString);
        LOG.info("Resource path is "+pathString);

        if (ResourcePath.isValid(pathString)) { return pathString; }

        throw new NotFoundException();
    }


    private ResourceSnapshot getSnapshot(final String path,
                                         final boolean workingCopy,
                                         final Integer version) {
        try {
            if (_respectVisibility) {
                LOG.debug("Retrieving current revision.");
                return _resources.resourceForPathSecure(path);
            } else if (workingCopy) {
                LOG.debug("Retrieving working copy.");
                return _resources.workingCopyForPath(path);
            } else if (null==version) {
                LOG.debug("Retrieving current revision.");
                return _resources.resourceForPathSecure(path);
            } else {
                LOG.debug("Retrieving revision: "+version+".");
                return _resources.revisionForPath(path, version.intValue());
            }
        } catch (final RestException e) {
            throw new NotFoundException();
        } catch (final UnauthorizedException e) {
            throw new AuthenticationRequiredException(path);
        }
    }


    /** {@inheritDoc} */
    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }


    private Context createContext(final HttpServletRequest request,
                                  final HttpServletResponse response,
                                  final ResourceSnapshot rs) {
        final Context context = new Context();
        context.add("user", loggedInUser());
        context.add("request",  request);
        context.add("response", response);
        context.add("services", new RequestScopeServiceLocator(request));
        context.add("resource", rs);
        return context;
    }


    private UserDto loggedInUser() {
        return _users.loggedInUser();
    }


    private void bindServices(final HttpServletRequest req) {
        // TODO: Wrap the 'Ext' objects to remove access to their methods.
        req.setAttribute(SessionKeys.USERS_KEY,     _users);
        req.setAttribute(SessionKeys.FILES_KEY,     _files);
        req.setAttribute(SessionKeys.PAGES_KEY,     _pages);
        req.setAttribute(SessionKeys.RESOURCES_KEY, _resources);
        req.setAttribute(SessionKeys.FOLDERS_KEY,   _folders);
        req.setAttribute(SessionKeys.ACTIONS_KEY,   _actions);
        req.setAttribute(SessionKeys.SEARCH_KEY,    _search);
        req.setAttribute(SessionKeys.COMMENTS_KEY,  _comments);
    }
}
