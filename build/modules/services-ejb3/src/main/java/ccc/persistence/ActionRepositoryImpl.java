/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.api.core.Action;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;
import ccc.domain.ActionEntity;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
class ActionRepositoryImpl
    implements
        ActionRepository {

    private Repository _repo;

    /**
     * Constructor.
     *
     * @param repo The repository used to perform queries.
     */
    public ActionRepositoryImpl(final Repository repo) {
        DBC.require().notNull(repo);
        _repo = repo;
    }


    /**
     * Constructor.
     *
     * @param em The JPA entity manager for this repository.
     */
    public ActionRepositoryImpl(final EntityManager em) {
        this(new JpaRepository(em));
    }


    /** {@inheritDoc} */
    @Override
    public List<ActionEntity> latest(final Date since) {
        return _repo.list(QueryNames.LATEST_ACTION, ActionEntity.class, since);
    }


    /** {@inheritDoc} */
    @Override
    public ActionEntity find(final UUID actionId) throws EntityNotFoundException {
        return _repo.find(ActionEntity.class, actionId);
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ActionEntity action) {
        _repo.create(action);
    }


    /** {@inheritDoc} */
    @Override
    public List<ActionEntity> completed(final String sort,
                                  final SortOrder sortOrder,
                                  final int pageNo,
                                  final int pageSize) {

        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();

        query.append("from ccc.domain.ActionEntity a WHERE a._status!='SCHEDULED'");
        query.append(" order by a.");
        query.append(mapSortColumn(sort));
        query.append(" ");
        query.append(sortOrder.name());

        return
            _repo.listDyn(
                query.toString(),
                ActionEntity.class,
                pageNo,
                pageSize,
                params);
    }


    /** {@inheritDoc} */
    @Override
    public long countCompleted() {
        final Map<String, Object> params = new HashMap<String, Object>();
        final String query = "select count(*) from ccc.domain.ActionEntity a "
        		+ " WHERE a._status!='SCHEDULED'";
        return _repo.scalarLong(query, params);
    }


    /** {@inheritDoc} */
    @Override
    public long countPending() {
        final Map<String, Object> params = new HashMap<String, Object>();
        final String query = "SELECT count(*) FROM ccc.domain.ActionEntity a "
                + " WHERE a._status='SCHEDULED'";
        return _repo.scalarLong(query, params);
    }


    /** {@inheritDoc} */
    @Override
    public List<ActionEntity> pending(final String sort,
                                      final SortOrder sortOrder,
                                      final int pageNo,
                                      final int pageSize) {

        final StringBuffer query = new StringBuffer();
        final Map<String, Object> params = new HashMap<String, Object>();

        query.append("FROM ccc.domain.ActionEntity a ");
        query.append("WHERE a._status='SCHEDULED'");
        query.append(" ORDER BY a.");
        query.append(mapSortColumn(sort));
        query.append(" ");
        query.append(sortOrder.name());

        return
            _repo.listDyn(
                query.toString(),
                ActionEntity.class,
                pageNo,
                pageSize,
                params);
    }

    private String mapSortColumn(final String sort) {
        try {
            final Action.Property p = Action.Property.valueOf(sort);
            switch (p) {
                case USERNAME:
                    return "_actor";
                case LOCALISED_TYPE:
                    return "_type";
                case LOCALISED_STATUS:
                    return "_status";
                case FAILURE_CODE:
                    return "_code";
                default:
                    return "_executeAfter";
            }
        } catch (final IllegalArgumentException e) {
            return "_executeAfter";
        }
    }
}
