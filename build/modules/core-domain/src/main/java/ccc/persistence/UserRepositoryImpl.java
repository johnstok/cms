/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ccc.domain.EntityNotFoundException;
import ccc.domain.User;


/**
 * A repository for user objects.
 *
 * @author Civic Computing Ltd.
 */
class UserRepositoryImpl implements UserRepository {

    private Repository _repository;
    private EntityManager _emm;


    /**
     * Constructor.
     *
     * @param repository The ResourceDao used for CRUD operations, etc.
     */
    public UserRepositoryImpl(final Repository repository) {
        _repository = repository;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public UserRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
        _emm = em;
    }


    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsers() {
        return _repository.uniquify(USERS, User.class);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithUsername(final String username) {
        final String searchParam =
            (null==username) ? "" : username.toLowerCase(Locale.US);
        return _repository.list(USERS_WITH_USERNAME, User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<User> listUsersWithEmail(final String email) {
        final String searchParam =
            (null==email) ? "" : email.toLowerCase(Locale.US);
        return _repository.list(USERS_WITH_EMAIL, User.class, searchParam);
    }

    /** {@inheritDoc} */
    @Override
    // FIXME: Is this method searching by permission or group name?
    public Collection<User> listUsersWithRole(final String role) {
        return _repository.uniquify(USERS_WITH_ROLE, User.class, role);
    }

    /** {@inheritDoc} */
    @Override
    public boolean usernameExists(final String username) {
        return _repository.exists(
            USER_WITH_MATCHING_USERNAME,
            User.class,
            username);
    }

    /** {@inheritDoc} */
    @Override
    public User find(final UUID userId) throws EntityNotFoundException {
        return _repository.find(User.class, userId);
    }

    /** {@inheritDoc} */
    @Override
    public User loggedInUser(final Principal p) throws EntityNotFoundException {
        if (null==p) {
            return null;
        }
        try {
            final String principalName = p.getName();
            final User user = _repository.find(
                USER_WITH_MATCHING_USERNAME, User.class, principalName);
            return user;
        } catch (final IllegalStateException e) {
            return null;
        }
    }


    /** {@inheritDoc} */
    @Override
    public void create(final User user) {
        _repository.create(user);
    }


    /** {@inheritDoc} */
    @Override
    public User userByLegacyId(final String legacyId)
    throws EntityNotFoundException {
        return _repository.find(USERS_WITH_LEGACY_ID, User.class, legacyId);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public List<String> listMetadataValuesWithKey(final String key) {
        // TODO move to JpaRepository?
        // http://stackoverflow.com/questions/1572980/
        // how-to-query-distinct-values-from-map-with-given-key-in-hibernate
        final Query q = _emm.createNativeQuery(
          "select distinct datum_value from user_metadata where datum_key = ?");
        q.setParameter(1, key);
        return q.getResultList();
    }

}
