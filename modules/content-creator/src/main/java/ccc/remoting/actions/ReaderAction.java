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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.persistence.FileRepository;
import ccc.persistence.FileRepositoryImpl;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.LogEntryRepositoryImpl;
import ccc.persistence.Repository;
import ccc.persistence.ResourceRepositoryImpl;
import ccc.persistence.UserRepositoryImpl;
import ccc.persistence.jpa.FsCoreData;
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
        final Repository repository =
            (Repository) req.getAttribute(SessionKeys.DAO_KEY);

        final UserRepositoryImpl ul = new UserRepositoryImpl(repository);
        final Principal p = req.getUserPrincipal();
        req.setAttribute(SessionKeys.CURRENT_USER, ul.loggedInUser(p));

        final LogEntryRepository al = new LogEntryRepositoryImpl(repository); // TODO: Remove - not used.
        req.setAttribute(SessionKeys.AUDIT_KEY, al);

        final FileRepository dm = new FileRepositoryImpl(new FsCoreData(), repository);
        req.setAttribute(SessionKeys.DATA_KEY, dm);

        final StatefulReader sr =
            new StatefulReaderImpl(new ResourceRepositoryImpl(repository), dm);
        req.setAttribute(RenderingKeys.READER_KEY, sr);

        _delegate.execute(req, resp);
    }
}
