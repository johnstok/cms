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

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import ccc.remoting.actions.ErrorHandlingAction;
import ccc.remoting.actions.LookupResourceAction;
import ccc.remoting.actions.PersistenceAction;
import ccc.remoting.actions.ReadOnlyTxAction;
import ccc.remoting.actions.ReadWriteTxAction;
import ccc.remoting.actions.ReaderAction;
import ccc.remoting.actions.RenderResourceAction;
import ccc.remoting.actions.ScriptedAction;
import ccc.remoting.actions.ServletAction;
import ccc.remoting.actions.SessionKeys;
import ccc.rest.Users;
import ccc.search.SearchEngine;


/**
 * The ContentServlet class serves CCC content.
 * Only the HTTP GET method is supported.
 *
 * @author Civic Computing Ltd.
 */
public class ContentServlet
    extends
        HttpServlet {

    @Resource                    private transient UserTransaction      _utx;
    @PersistenceUnit             private transient EntityManagerFactory _emf;

    @EJB(name=SearchEngine.NAME) private transient SearchEngine         _search;
    @EJB(name=Users.NAME)        private transient Users                _users;


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
                new ReadOnlyTxAction(
                    new PersistenceAction(
                        new ReaderAction(
                            new LookupResourceAction(
                                new RenderResourceAction(
                                    _respectVisibility,
                                    _search
                                ),
                                _rootName
                            )
                        ),
                        _emf
                    ),
                    _utx
                ),
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

        bindServices(req);

        final ServletAction action =
            new ErrorHandlingAction(
                new ReadWriteTxAction(
                    new PersistenceAction(
                        new ReaderAction(
                            new LookupResourceAction(
                                new ScriptedAction(),
                                _rootName
                            )
                        ),
                        _emf
                    ),
                    _utx
                ),
                getServletContext(),
                "/content/login?tg="
            );

        action.execute(req, resp);
    }


    private void bindServices(final HttpServletRequest req) {
        req.setAttribute(SessionKeys.USERS_KEY, _users);
    }
}
