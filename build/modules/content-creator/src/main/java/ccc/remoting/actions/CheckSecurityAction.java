/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ccc.domain.Resource;
import ccc.domain.User;
import ccc.rendering.AuthenticationRequiredException;
import ccc.types.Username;


/**
 * A servlet action that applies security privileges for the requests resource.
 *
 * @author Civic Computing Ltd.
 */
public class CheckSecurityAction
    extends
        AbstractServletAction {

    private final boolean       _applySecurity;


    /**
     * Constructor.
     *
     * @param applySecurity Should this instance apply security constraints.
     */
    public CheckSecurityAction(final boolean applySecurity) {
        _applySecurity = applySecurity;
    }


    /** {@inheritDoc} */
    @Override
    public void execute(final HttpServletRequest request,
                        final HttpServletResponse response) {
            final User currentUser = getCurrentUser(request);
            final Resource rs = getResource(request);

            if (!_applySecurity) { // Don't check security.
                return;
            }

            checkSecurity(rs, currentUser);
    }


    private void checkSecurity(final Resource r, final User user) {
        final User u = (null==user)
            ? new User(new Username("anonymous"), "password")
            : user;
        if (!r.isAccessibleTo(u)) {
            throw new AuthenticationRequiredException(
                // FIXME: Broken for /assets
                r.absolutePath().removeTop().toString());
        }
    }
}
