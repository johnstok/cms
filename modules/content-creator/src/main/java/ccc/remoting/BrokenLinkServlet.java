/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import ccc.remoting.actions.ErrorHandlingAction;
import ccc.remoting.actions.FixLinkAction;
import ccc.remoting.actions.PersistenceAction;
import ccc.remoting.actions.ReadOnlyTxAction;
import ccc.remoting.actions.ReaderAction;
import ccc.remoting.actions.ServletAction;



/**
 * A servlet to redirect old CCC6 URLs to the corresponding CCC7 url.
 * <br>Supports links in the following forms:
 * <br>- /2334.html
 * <br>- /files/my%20file.txt
 *
 * @author Civic Computing Ltd.
 */
public final class BrokenLinkServlet
    extends
        HttpServlet {

    // TODO: How do we re-establish these fields following serialisation?
    @Resource        private transient UserTransaction      _utx;
    @PersistenceUnit private transient EntityManagerFactory _emf;


    /** {@inheritDoc} */
    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
                                          throws ServletException, IOException {
        final ServletAction action =
            new ErrorHandlingAction(
                new ReadOnlyTxAction(
                    new PersistenceAction(
                        new ReaderAction(
                            new FixLinkAction()),
                        _emf),
                    _utx),
                getServletContext()
            );

        action.execute(req, resp);
    }
}
