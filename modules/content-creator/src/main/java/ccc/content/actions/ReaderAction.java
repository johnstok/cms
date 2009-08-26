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
package ccc.content.actions;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.persistence.jpa.FsCoreData;
import ccc.rendering.StatefulReader;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.UserLookup;
import ccc.services.impl.AuditLogImpl;
import ccc.services.impl.DataManagerImpl;
import ccc.services.impl.ResourceDaoImpl;
import ccc.services.impl.StatefulReaderImpl;


/**
 * A servlet action that attaches ccc services to the request scope.
 *
 * @author Civic Computing Ltd.
 */
public class ReaderAction
    implements
        ServletAction {

    private final ServletAction _delegate;

    /**
     * Constructor.
     *
     * @param delegate The action that will be called next in the chain.
     */
    public ReaderAction(final ServletAction delegate) {
        _delegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest req,
                        final HttpServletResponse resp) throws ServletException,
                                                               IOException {
        final Dao dao =
            (Dao) req.getAttribute(SessionKeys.DAO_KEY);

        final UserLookup ul = new UserLookup(dao);
        final Principal p = req.getUserPrincipal();
        req.setAttribute(SessionKeys.CURRENT_USER, ul.loggedInUser(p));

        final AuditLog al = new AuditLogImpl(dao); // TODO: Remove - not used.
        req.setAttribute(SessionKeys.AUDIT_KEY, al);

        final DataManager dm = new DataManagerImpl(new FsCoreData(), dao);
        req.setAttribute(SessionKeys.DATA_KEY, dm);

        final StatefulReader sr =
            new StatefulReaderImpl(new ResourceDaoImpl(dao), dm);
        req.setAttribute(RenderingKeys.READER_KEY, sr);

        _delegate.execute(req, resp);
    }
}
