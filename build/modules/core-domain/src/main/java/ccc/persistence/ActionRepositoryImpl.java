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
package ccc.persistence;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import ccc.domain.Action;
import ccc.domain.EntityNotFoundException;
import ccc.types.DBC;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ActionRepositoryImpl
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
    public List<Action> latest(final Date since) {
        return _repo.list(QueryNames.LATEST_ACTION, Action.class, since);
    }


    /** {@inheritDoc} */
    @Override
    public List<Action> pending() {
        return _repo.list(QueryNames.PENDING, Action.class);
    }


    /** {@inheritDoc} */
    @Override
    public List<Action> completed() {
        return _repo.list(QueryNames.EXECUTED, Action.class);
    }


    /** {@inheritDoc} */
    @Override
    public Action find(final UUID actionId) throws EntityNotFoundException {
        return _repo.find(Action.class, actionId);
    }


    /** {@inheritDoc} */
    @Override
    public void create(final Action action) {
        _repo.create(action);
    }
}
