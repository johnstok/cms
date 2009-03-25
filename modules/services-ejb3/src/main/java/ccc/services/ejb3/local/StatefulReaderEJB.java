/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;
import ccc.services.StatefulReader;
import ccc.services.UserManager;


/**
 * EJB3 implementation of {@link StatefulReader}.
 *
 * @author Civic Computing Ltd
 */
@Stateful(name=StatefulReader.NAME)
@TransactionAttribute(REQUIRED)
@Local(StatefulReader.class)
public final class StatefulReaderEJB
    implements
        StatefulReader {

    @PersistenceContext(
        unitName = "ccc-persistence",
        type=PersistenceContextType.EXTENDED)
    @SuppressWarnings("unused")
    private EntityManager _em; // Required to insure method calls are stateful.

    @EJB(name=UserManager.NAME)  private UserManager  _users;
    @EJB(name=AuditLog.NAME)     private AuditLog     _log;
    @EJB(name=ResourceDao.NAME)  private ResourceDao  _resources;

    /** Constructor. */
    @SuppressWarnings("unused") public StatefulReaderEJB() { super(); }

    /**
     * Constructor.
     *
     * @param entityManager A JPA entity manager.
     * @param um The user manager used to assert security privileges.
     */
    StatefulReaderEJB(final EntityManager entityManager, final UserManager um) {
        _em = entityManager;
        _users = um;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Resource lookup(final String rootName, final ResourcePath path) {
        return _resources.lookup(rootName, path);
    }


    /** {@inheritDoc} */
    @Override
    public LogEntry lookup(final long index) {
        return _log.findEntryForIndex(index);
    }


    /** {@inheritDoc} */
    @Override
    public User loggedInUser() {
        return _users.loggedInUser();
    }
}
