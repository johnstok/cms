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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.persistence.jpa.FsCoreData;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.DataManager;
import ccc.services.StatefulReader;
import ccc.services.UserLookup;
import ccc.services.impl.AuditLogEJB;
import ccc.services.impl.DataManagerEJB;
import ccc.services.impl.ResourceDaoImpl;
import ccc.services.impl.StatefulReaderEJB;


/**
 * TODO: Add Description for this type.
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
     * @param delegate
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
        req.setAttribute(
            SessionKeys.CURRENT_USER,
            ul.loggedInUser(req.getUserPrincipal()));

        final AuditLog al = new AuditLogEJB(dao);
        req.setAttribute(SessionKeys.AUDIT_KEY, al);

        final DataManager dm = new DataManagerEJB(new FsCoreData(), dao);
        req.setAttribute(SessionKeys.DATA_KEY, dm);

        final StatefulReader sr =
            new StatefulReaderEJB(
                al,
                new ResourceDaoImpl(dao));
        req.setAttribute(RenderingKeys.READER_KEY, sr);

        _delegate.execute(req, resp);
    }
}
