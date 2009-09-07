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
package ccc.services.ejb3;

import java.util.UUID;

import javax.annotation.Resource;
import javax.ejb.EJBContext;

import org.apache.log4j.Logger;

import ccc.domain.CccCheckedException;
import ccc.domain.User;
import ccc.persistence.Repository;
import ccc.persistence.ResourceRepository;
import ccc.persistence.UserRepository;
import ccc.rest.CommandFailedException;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class BaseCommands
    extends
        ModelTranslation {

    private static Logger LOG = Logger.getLogger(BaseCommands.class);

    UserRepository     _users;
    Repository         _bdao;
    ResourceRepository _resources;
    @Resource protected EJBContext _context;


    protected User userForId(final UUID userId) {
        final User u = _bdao.find(User.class, userId);
        return u;
    }

    protected User loggedInUser(final EJBContext context) {
        return _users.loggedInUser(context.getCallerPrincipal());
    }

    protected UUID loggedInUserId(final EJBContext context) {
        return loggedInUser(context).id();
    }

    protected CommandFailedException fail(final EJBContext context,
                                          final CccCheckedException e) {
        context.setRollbackOnly();  // CRITICAL
        final CommandFailedException cfe = e.toRemoteException();
        LOG.info(
            "Handled local exception: "+cfe.getFailure().getExceptionId(), e);
        return cfe;
    }

    protected User currentUser() {
        return _users.loggedInUser(_context.getCallerPrincipal());
    }

    protected CommandFailedException fail(final CccCheckedException e) {
        return fail(_context, e);
    }

    protected UUID loggedInUserId() {
        return loggedInUserId(_context);
    }
}
