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
package ccc.remoting.actions;

import java.io.IOException;
import java.security.Principal;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ccc.domain.EntityNotFoundException;
import ccc.persistence.DataRepository;
import ccc.persistence.DataRepositoryImpl;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.rendering.StatefulReader;
import ccc.rendering.StatefulReaderImpl;


/**
 * A servlet action that attaches ccc services to the request scope.
 *
 * @author Civic Computing Ltd.
 */
public class ReaderAction
    implements
        ServletAction {

    private static final Logger LOG = Logger.getLogger(ReaderAction.class);

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
        final EntityManager em =
            (EntityManager) req.getAttribute(SessionKeys.EM_KEY);

        final UserRepositoryImpl ul = new UserRepositoryImpl(em);
        final Principal p = req.getUserPrincipal();
        try {
            req.setAttribute(SessionKeys.CURRENT_USER, ul.loggedInUser(p));
        } catch (final EntityNotFoundException e) {
            LOG.warn("No user found for principal: "+p);
        }

        final LogEntryRepository al =
            new LogEntryRepositoryImpl(em);
        req.setAttribute(SessionKeys.AUDIT_KEY, al);

        final DataRepository dm =
            DataRepositoryImpl.onFileSystem(em);
        req.setAttribute(SessionKeys.DATA_KEY, dm);

        final StatefulReader sr =
            new StatefulReaderImpl(new ResourceRepositoryImpl(em), dm);
        req.setAttribute(RenderingKeys.READER_KEY, sr);

        _delegate.execute(req, resp);
    }
}
