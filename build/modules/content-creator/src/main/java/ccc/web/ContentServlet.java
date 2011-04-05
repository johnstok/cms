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

package ccc.web;

import static ccc.commons.Strings.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.api.core.Failure;
import ccc.api.core.ObjectFactory;
import ccc.api.core.User;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.synchronous.MemoryServiceLocator;
import ccc.api.synchronous.ServiceLocator;
import ccc.api.types.ResourcePath;
import ccc.commons.EnumTools;
import ccc.commons.Environment;
import ccc.commons.HTTP;
import ccc.commons.TaxonomyTools;
import ccc.plugins.PluginFactory;
import ccc.plugins.s11n.Serializers;
import ccc.plugins.scripting.Context;
import ccc.web.rendering.AuthenticationRequiredException;
import ccc.web.rendering.NotFoundException;
import ccc.web.rendering.Response;


/**
 * The ContentServlet class serves CCC content.
 *
 * @author Civic Computing Ltd.
 */
public class ContentServlet
    extends
        AbstractCCCServlet {
    private static final Logger LOG = Logger.getLogger(ContentServlet.class);

    private final Random _random = new Random();

    private boolean _respectVisibility = true;
    private boolean _usePreviewTemplate = false;
    private String _domain = "localhost";


    /** {@inheritDoc} */
    @Override
    public void init() {
        final ServletConfig cf = getServletConfig();
        if ("false".equals(cf.getInitParameter("respect_visibility"))) {
            _respectVisibility = false;
        } else {
            _respectVisibility = true;
        }
        if ("true".equals(cf.getInitParameter("preview_template"))) {
        	_usePreviewTemplate = true;
        } else {
        	_usePreviewTemplate = false;
        }
        final String domain =
            getServletContext().getInitParameter("ccc.web.domain");
        if (null!=domain && domain.trim().length()>0) { _domain = domain; }
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

        final ccc.api.core.Resource resource =
            getSnapshot(contentPath, wc, version);

        if (null == resource) {
            LOG.warn("No resource for path "+contentPath);
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            LOG.warn("Resource at path "+contentPath+" isn't published.");
            throw new NotFoundException();
        }
        TmpRenderer tr =
        	new TmpRenderer(getFiles(), getTemplates(), getResources());
        
        if (_usePreviewTemplate) {
            tr.setTemplate(req.getParameter("hiddenbody"));
        }
        Response r = tr.render(resource);

        if (resource.isSecure()       // Don't cache secure pages.
            || !_respectVisibility) { // Don't cache previews or working copies.
            r.dontCache();
        }

        final Context context = createContext(req, resp, resource);
        r.write(resp, context, new PluginFactory().createTemplating());
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


    private ccc.api.core.Resource getSnapshot(final String path,
                                              final boolean workingCopy,
                                              final Integer version) {
        try {
            if (_respectVisibility) {
                LOG.debug("Retrieving current revision.");
                return getResources().resourceForPathSecure(path);
            } else if (workingCopy) {
                LOG.debug("Retrieving working copy.");
                return getResources().workingCopyForPath(path);
            } else if (null==version) {
                LOG.debug("Retrieving current revision.");
                return getResources().resourceForPathSecure(path);
            } else {
                LOG.debug("Retrieving revision: "+version+".");
                return getResources().revisionForPath(path, version.intValue());
            }
        } catch (final UnauthorizedException e) {
            LOG.warn(e.getMessage());
            throw new AuthenticationRequiredException(path);
        } catch (final CCException e) {
            final Serializers sFactory = new PluginFactory().serializers();
            final String data =
                sFactory.create(Failure.class).write(e.getFailure());
            LOG.warn(
                "Exception retrieving path " + path
                + " wc=" + workingCopy
                + ", v=" + version
                + " " + data);
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
                                  final ccc.api.core.Resource rs) {
        final Context context = new Context();

        context.add("user",        loggedInUser());
        context.add("request",     request);
        context.add("response",    response);
        context.add("services",    createServiceLocator());
        context.add("resource",    rs);
        context.add("mail",        getMailer());
        context.add("http",        HTTP.class);
        context.add("random",      _random);
        context.add("math",        Math.class);
        context.add("collections", Collections.class);
        context.add("calendar",    Calendar.class);
        context.add("html",        new PluginFactory().html());
        context.add("uuid",        UUID.class);
        context.add("enums",       new EnumTools());
        context.add("hostname",    Environment.getHostname());
        context.add("domain",      _domain);
        context.add("apiTypes",    ObjectFactory.class);
        context.add("taxonomy",    new TaxonomyTools());
        context.add("multipart",   new PluginFactory().createMultipart());
        return context;
    }


    private ServiceLocator createServiceLocator() {
        final MemoryServiceLocator sl = new MemoryServiceLocator();
        sl.setUserCommands(getUsers());
        sl.setFolderCommands(getFolders());
        sl.setFiles(getFiles());
        sl.setPageCommands(getPages());
        sl.setCommands(getResources());
        sl.setActions(getActions());
        sl.setSearch(getSearch());
        sl.setComments(getComments());
        sl.setGroups(getGroups());
        sl.setTemplates(getTemplates());
        return sl;
    }


    private User loggedInUser() {
        return getUsers().retrieveCurrent();
    }
}
