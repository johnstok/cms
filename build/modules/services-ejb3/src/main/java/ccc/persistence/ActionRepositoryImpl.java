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

import ccc.api.core.ActionCriteria;
import ccc.api.core.ActionSummary;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;
import ccc.domain.ActionEntity;


/**
 * A repository for action objects.
 *
 * @author Civic Computing Ltd.
 */
class ActionRepositoryImpl
    implements
        ActionRepository {

    private final Repository _repo;

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
    public ActionEntity find(final UUID actionId) {
        return _repo.find(ActionEntity.class, actionId);
    }


    /** {@inheritDoc} */
    @Override
    public void create(final ActionEntity action) {
        _repo.create(action);
    }


    /** {@inheritDoc} */
    @Override
    public List<ActionEntity> listActions(final ActionCriteria criteria,
                                  final String sort,
                                  final SortOrder sortOrder,
                                  final int pageNo,
                                  final int pageSize) {

        final StringBuilder query = new StringBuilder();
        final Map<String, Object> params = new HashMap<String, Object>();

        createQuery(query, criteria, params);
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
    public long countActions(final ActionCriteria criteria) {
        final Map<String, Object> params = new HashMap<String, Object>();
        final StringBuilder query = new StringBuilder("select count(*) ");
        createQuery(query, criteria, params);
        return _repo.scalarLong(query.toString(), params);
    }

    private void createQuery(final StringBuilder query,
                             final ActionCriteria ac,
                             final Map<String, Object> params) {
        query.append(" from ccc.domain.ActionEntity a ");
        if (ac.getStatus() != null) {
            query.append(" WHERE a._status=:status");
            params.put("status", ac.getStatus());
        }
        if (ac.getCommandType() != null) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" a._commandType=':commandType'");
            params.put("commandType", ac.getCommandType());
        }
        if (ac.getUsername() != null) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" lower(a._username) like lower(:username)");
            params.put("username", ac.getUsername());
        }
        if (ac.getFailureCode() != null) {
            query.append((params.size()>0) ? " and" : " where");
            query.append(" a._failureCode=:failureCode)");
            params.put("failureCode", ac.getFailureCode());
        }
    }


    private String mapSortColumn(final String sort) {
        if (ActionSummary.USERNAME.equals(sort)) {
            return "_actor";
        } else if (ActionSummary.TYPE.equals(sort)) {
            return "_type";
        } else if (ActionSummary.STATUS.equals(sort)) {
            return "_status";
        } else if (ActionSummary.FAILURE_CODE.equals(sort)) {
            return "_code";
        } else {
            return "_executeAfter";
        }
    }
}
