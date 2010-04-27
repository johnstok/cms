/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.api.types.DBC;
import ccc.domain.GroupEntity;


/**
 * JPA implementation of a group repository.
 *
 * @author Civic Computing Ltd.
 */
public class GroupRepositoryImpl
    implements
        GroupRepository {

    private Repository _repo;


    /**
     * Constructor.
     *
     * @param repo The repository used to perform queries.
     */
    public GroupRepositoryImpl(final Repository repo) {
        DBC.require().notNull(repo);
        _repo = repo;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public GroupRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /** {@inheritDoc} */
    @Override
    public void create(final GroupEntity g) {
        _repo.create(g);
    }


    /** {@inheritDoc} */
    @Override
    public GroupEntity find(final UUID groupId) {
        return _repo.find(GroupEntity.class, groupId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<GroupEntity> list(final String name) {
        final StringBuffer query = new StringBuffer();
        query.append("from ");
        query.append(GroupEntity.class.getName());
        query.append(" g");

        final Map<String, Object> params = new HashMap<String, Object>();

        if (null!=name) {
            query.append(" where g._name = :name");
            params.put("name", name);
        }

        return _repo.listDyn(
            query.toString(),
            GroupEntity.class,
            1,
            100,
            params);
    }
}
