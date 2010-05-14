/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.io.IOException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.api.core.Actions;
import ccc.api.core.Aliases;
import ccc.api.core.Comments;
import ccc.api.core.Files;
import ccc.api.core.Folders;
import ccc.api.core.Groups;
import ccc.api.core.Pages;
import ccc.api.core.Resources;
import ccc.api.core.SearchEngine;
import ccc.api.core.Security;
import ccc.api.core.ServiceLocator;
import ccc.api.core.Templates;
import ccc.api.core.Users;
import ccc.api.jaxrs.ActionsImpl;
import ccc.api.jaxrs.CommentsImpl;
import ccc.api.jaxrs.FilesImpl;
import ccc.api.jaxrs.FoldersImpl;
import ccc.api.jaxrs.GroupsImpl;
import ccc.api.jaxrs.PagesImpl;
import ccc.api.jaxrs.ResourcesImpl;
import ccc.api.jaxrs.SearchImpl;
import ccc.api.jaxrs.TemplatesImpl;
import ccc.api.jaxrs.UsersImpl;
import ccc.plugins.mail.Mailer;
import ccc.plugins.mail.javamail.JavaMailMailer;



/**
 * Abstract servlet action providing helper methods.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractCCCServlet
    extends
        HttpServlet
    implements
        ServiceLocator {

    private static final Logger LOG =
        Logger.getLogger(AbstractCCCServlet.class);

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


    /**
     * Dispatch to the 'not found' URI.
     *
     * @param request The request.
     * @param response The response.
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchNotFound(final HttpServletRequest request,
                                    final HttpServletResponse response)
                                          throws ServletException, IOException {
        LOG.info("Forwarding to /notfound for: " + requestPath(request));
        request.getRequestDispatcher("/notfound").forward(request, response);
    }


    /**
     * Dispatch to the error handler.
     *
     * @param request The request.
     * @param response The response.
     * @param e The exception we encountered
     * @throws ServletException From servlet API.
     * @throws IOException From servlet API.
     */
    protected void dispatchError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final Exception e)
                                          throws ServletException, IOException {
        request.setAttribute(SessionKeys.EXCEPTION_KEY, e);
        request.getRequestDispatcher("/error").forward(request, response);
    }


    /**
     * Send a redirect to the client.
     *
     * @param request The incoming request.
     * @param response The outgoing response.
     * @param relUri The relative URI to redirect to.
     * @throws IOException Servlet API can throw an {@link IOException}.
     */
    protected void dispatchRedirect(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final String relUri) throws IOException {
        final String target = request.getContextPath()+relUri;
        LOG.info("Redirecting to " + target + " from " + requestPath(request));
        response.sendRedirect(target);
    }


    private String requestPath(final HttpServletRequest request) {
        final String context = request.getContextPath();
        final String servlet = request.getServletPath();
        final String info    = request.getPathInfo();
        final String query   = request.getQueryString();
        return
            context
            + servlet
            + ((null==info) ? "" : info)
            + ((null==query) ? "" : query);
    }


    /**
     * Retrieves the exception that this servlet should report.
     * Guarantees to return an exception and never NULL.
     *
     * @param request The request that the exception will be retrieved from.
     * @return The exception that should be reported.
     */
    protected Exception getException(final HttpServletRequest request) {
        final Object o = request.getAttribute(SessionKeys.EXCEPTION_KEY);
        if (null==o) {
            return new RuntimeException(
                "No exception was found at the expected location: "
                +SessionKeys.EXCEPTION_KEY);
        } else if (!(o instanceof Exception)) {
            return new RuntimeException(
                "Object at location: "
                +SessionKeys.EXCEPTION_KEY
                +" was not an exception.");
        } else {
            return Exception.class.cast(o);
        }
    }


    /**
     * Accessor.
     *
     * @return The mail implementation for this servlet.
     */
    protected Mailer getMailer() {
        return new JavaMailMailer(_mail);
    }


    /** {@inheritDoc} */
    @Override
    public Actions getActions() {
        return new ActionsImpl(_actions);
    }


    /** {@inheritDoc} */
    @Override
    public Aliases getAliases() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Comments getComments() {
        return new CommentsImpl(_comments);
    }


    /** {@inheritDoc} */
    @Override
    public Files getFiles() {
        return new FilesImpl(_files);
    }


    /** {@inheritDoc} */
    @Override
    public Folders getFolders() {
        return new FoldersImpl(_folders);
    }


    /** {@inheritDoc} */
    @Override
    public Groups getGroups() {
        return new GroupsImpl(_groups);
    }


    /** {@inheritDoc} */
    @Override
    public Pages getPages() {
        return new PagesImpl(_pages);
    }


    /** {@inheritDoc} */
    @Override
    public Resources getResources() {
        return new ResourcesImpl(_resources);
    }


    /** {@inheritDoc} */
    @Override
    public SearchEngine getSearch() {
        return new SearchImpl(_search);
    }


    /** {@inheritDoc} */
    @Override
    public Security getSecurity() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() {
        return new TemplatesImpl(_templates);
    }


    /** {@inheritDoc} */
    @Override
    public Users getUsers() {
        return new UsersImpl(_users);
    }
}
