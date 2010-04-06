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

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.mail.Session;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.api.client1.MemoryServiceLocator;
import ccc.commons.Context;
import ccc.mail.JavaMailMailer;
import ccc.mail.Mailer;
import ccc.rendering.AuthenticationRequiredException;
import ccc.rendering.NotFoundException;
import ccc.rendering.Response;
import ccc.rendering.velocity.VelocityProcessor;
import ccc.rest.Actions;
import ccc.rest.Comments;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Groups;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.SearchEngine;
import ccc.rest.ServiceLocator;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.dto.ResourceSnapshot;
import ccc.rest.dto.UserDto;
import ccc.rest.exceptions.RestException;
import ccc.rest.exceptions.UnauthorizedException;
import ccc.rest.impl.FoldersImpl;
import ccc.rest.impl.UsersImpl;
import ccc.serialization.JsonImpl;
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
    @EJB(name = Pages.NAME)        private transient Pages        _pages;
    @EJB(name = Folders.NAME)      private transient Folders      _folders;
    @EJB(name = Files.NAME)        private transient Files        _files;
    @EJB(name = Resources.NAME)    private transient Resources    _resources;
    @EJB(name = Actions.NAME)      private transient Actions      _actions;
    @EJB(name = Templates.NAME)    private transient Templates    _templates;
    @EJB(name = Comments.NAME)     private transient Comments     _comments;
    @EJB(name = Groups.NAME)       private transient Groups       _groups;
    @Resource(name = Mailer.NAME)  private transient Session      _mail;

    private boolean _respectVisibility = true;


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

        final String contentPath = determineResourcePath(req);
        final boolean wc = req.getParameterMap().keySet().contains("wc");
        final Integer version = determineVersion(req);
        LOG.debug("[wc="+wc+", v="+version+"]");

        final ResourceSnapshot resource = getSnapshot(contentPath, wc, version);

        if (null == resource) {
            LOG.warn("No resource for path "+contentPath);
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            LOG.warn("Resource at path "+contentPath+" isn't published.");
            throw new NotFoundException();
        }

        final Response r =
            new TmpRenderer(_files, _templates, _resources).render(resource);

        if (resource.isSecure()       // Don't cache secure pages.
            || !_respectVisibility) { // Don't cache previews or working copies.
            r.dontCache();
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
        LOG.debug("Resource path is "+pathString);

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
        } catch (final UnauthorizedException e) {
            LOG.warn(e.getMessage());
            throw new AuthenticationRequiredException(path);
        } catch (final RestException e) {
            LOG.warn(
                "Exception retrieving path " + path
                + " wc=" + workingCopy
                + ", v=" + version
                + " " + new JsonImpl(e.getFailure()));
            throw new NotFoundException();
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
        context.add("services", createServiceLocator());
        context.add("resource", rs);
        context.add("mail", new JavaMailMailer(_mail));
        return context;
    }


    private ServiceLocator createServiceLocator() {
        final MemoryServiceLocator sl = new MemoryServiceLocator();
        sl.setUserCommands(UsersImpl.decorate(_users));
        sl.setFolderCommands(FoldersImpl.decorate(_folders));
        sl.setFiles(_files);
        sl.setPageCommands(_pages);
        sl.setCommands(_resources);
        sl.setActions(_actions);
        sl.setSearch(_search);
        sl.setComments(_comments);
        sl.setGroups(_groups);
        return sl;
    }


    private UserDto loggedInUser() {
        return _users.loggedInUser();
    }
}
