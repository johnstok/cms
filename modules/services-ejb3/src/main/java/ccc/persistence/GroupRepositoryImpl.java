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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.api.exceptions.EntityNotFoundException;
import ccc.domain.Group;
import ccc.types.DBC;


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
    public void create(final Group g) {
        _repo.create(g);
    }


    /** {@inheritDoc} */
    @Override
    public Group find(final UUID groupId) throws EntityNotFoundException {
        return _repo.find(Group.class, groupId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<Group> list(final String name) {
        final StringBuffer query = new StringBuffer("from ccc.domain.Group g");
        final List<Object> params = new ArrayList<Object>();

        if (null!=name) {
            query.append(" where g._name = ?");
            params.add(name);
        }

        return _repo.listDyn(
            query.toString(),
            Group.class,
            1,
            100,
            params.toArray());
    }
}
