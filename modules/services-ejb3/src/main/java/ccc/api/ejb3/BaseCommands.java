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
package ccc.api.ejb3;

import javax.ejb.EJBContext;

import org.apache.log4j.Logger;

import ccc.domain.CccCheckedException;
import ccc.domain.CommandFailedException;
import ccc.domain.User;
import ccc.persistence.UserLookup;
import ccc.persistence.jpa.JpaRepository;
import ccc.types.ID;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class BaseCommands extends ModelTranslation{


    UserLookup         _userLookup;
    JpaRepository            _bdao;
    static Logger LOG = Logger.getLogger(BaseCommands.class);

    protected User userForId(final ID userId) {
        final User u = _bdao.find(User.class, toUUID(userId));
        return u;
    }

    protected User loggedInUser(final EJBContext context) {
        return _userLookup.loggedInUser(context.getCallerPrincipal());
    }

    protected ID loggedInUserId(final EJBContext context) {
        return new ID(loggedInUser(context).id().toString());
    }

    protected CommandFailedException fail(final EJBContext context,
                                          final CccCheckedException e) {
        context.setRollbackOnly();  // CRITICAL
        final CommandFailedException cfe = e.toRemoteException();
        LOG.info(
            "Handled local exception: "+cfe.getFailure().getExceptionId(), e);
        return cfe;
    }

}
