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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import ccc.domain.EntityNotFoundException;
import ccc.domain.User;
import ccc.rest.dto.UserCriteria;


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
    public Collection<User> listUsers(final UserCriteria uc,
        final int pageNo,
        final int pageSize) {

        final List<Object> params = new ArrayList<Object>();

        final StringBuffer query = new StringBuffer();
        query.append("select distinct u");
        createUserQuery(query, uc, params);

        return _repository.listDyn(
            query.toString(),
            User.class,
            pageNo,
            pageSize,
            params.toArray());
    }


    /** {@inheritDoc} */
    @Override
    public long countUsers(final UserCriteria uc) {
        final List<Object> params = new ArrayList<Object>();
        final StringBuffer query = new StringBuffer();
        query.append("select count(distinct u) ");
        createUserQuery(query, uc, params);
        return _repository.scalarLong(query.toString(), params.toArray());
//        return 20;
    }

    private void createUserQuery(final StringBuffer query,
                                 final UserCriteria uc,
                                 final List<Object> params) {

        query.append(
            " from ccc.domain.User as u left join u._roles as r");
        if (null!=uc.getEmail()) {
            query.append(" where lower(u._email._text) like lower(?)");
            params.add(uc.getEmail());
        }
        if (null!=uc.getUsername()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" lower(u._username._value) like lower(?)");
            params.add(uc.getUsername());
        }
        if (null!=uc.getGroups()) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" r._name like ?");
            params.add(uc.getGroups());
        }
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
