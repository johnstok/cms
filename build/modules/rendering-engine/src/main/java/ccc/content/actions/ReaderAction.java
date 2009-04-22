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

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.services.AuditLog;
import ccc.services.StatefulReader;
import ccc.services.ejb3.local.AuditLogEJB;
import ccc.services.ejb3.local.ResourceDaoImpl;
import ccc.services.ejb3.local.StatefulReaderEJB;
import ccc.services.ejb3.support.BaseDao;
import ccc.services.ejb3.support.Dao;


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
        final EntityManager em =
            (EntityManager) req.getAttribute(SessionKeys.PERSISTENCE_KEY);
        final AuditLog al = new AuditLogEJB(em);
        final Dao dao = new BaseDao(em);
        final StatefulReader sr =
            new StatefulReaderEJB(
                al,
                new ResourceDaoImpl(null, al, dao));
        req.setAttribute(RenderingKeys.READER_KEY, sr);

        _delegate.execute(req, resp);
    }
}
