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

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.remoting.actions.CheckSecurityAction;
import ccc.remoting.actions.ErrorHandlingAction;
import ccc.remoting.actions.LookupResourceAction;
import ccc.remoting.actions.ReaderAction;
import ccc.remoting.actions.RenderResourceAction;
import ccc.remoting.actions.SerialAction;
import ccc.remoting.actions.ServletAction;
import ccc.remoting.actions.SessionKeys;
import ccc.rest.Actions;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.Users;
import ccc.rest.extensions.FilesExt;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.search.SearchEngine;


/**
 * The ContentServlet class serves CCC content.
 *
 * @author Civic Computing Ltd.
 */
public class ContentServlet
    extends
        HttpServlet {

    @EJB(name = SearchEngine.NAME) private transient SearchEngine _search;
    @EJB(name = Users.NAME)        private transient Users        _users;
    @EJB(name = Pages.NAME)        private transient PagesExt     _pages;
    @EJB(name = Folders.NAME)      private transient FoldersExt   _folders;
    @EJB(name = Files.NAME)        private transient FilesExt     _files;
    @EJB(name = Resources.NAME)    private transient ResourcesExt _resources;
    @EJB(name = Actions.NAME)      private transient Actions      _actions;


    private String _rootName           = null;
    private boolean _respectVisibility = true;


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
                         final HttpServletResponse resp)
                                          throws IOException, ServletException {

        bindServices(req);

        final ServletAction action =
            new ErrorHandlingAction(
                    new SerialAction(
                        new ReaderAction(),
                        new LookupResourceAction(_rootName),
                        new CheckSecurityAction(_respectVisibility),
                        new RenderResourceAction(_respectVisibility, _search)),
                getServletContext(),
                "/content/login?tg="
            );

        action.execute(req, resp);
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