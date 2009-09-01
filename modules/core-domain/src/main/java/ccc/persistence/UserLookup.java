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
package ccc.persistence;

import static ccc.persistence.QueryNames.*;

import java.security.Principal;

import ccc.domain.User;


/**
 * Helper class to look up CCC users.
 * FIXME: Merge into {@link UserRepositoryImpl}.
 *
 * @author Civic Computing Ltd.
 */
public final class UserLookup {
    private final Repository _repository;

    /**
     * Constructor.
     *
     * @param repository The DAO to use for lookups.
     */
    public UserLookup(final Repository repository) {
        _repository = repository;
    }


    /**
     * Look up a user from a JAAS principal.
     *
     * @param p The principal.
     * @return The corresponding CCC user.
     */
    public User loggedInUser(final Principal p) {
        if (null==p) {
            return null;
        }
        try {
            final String principalName = p.getName();
            final User user =
                _repository.find(USERS_WITH_USERNAME, User.class, principalName);
            return user;
        } catch (final IllegalStateException e) {
            return null;
        }
    }
}
