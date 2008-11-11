/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static org.easymock.EasyMock.*;

import java.util.Date;

import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.User;
import ccc.services.AuditLogLocal;
import ccc.services.UserManagerLocal;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AuditLogEJBTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRecordLockRejectsNull() {

        // ARRANGE
        replay(_em, _um);
        final AuditLogLocal al = new AuditLogEJB(_em, _um);

        // ACT
        try {
            al.recordLock(null);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
        verify(_em, _um);
    }

    /**
     * Test.
     */
    public void testRecordLockPersistsLogEntry() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.flush();
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLogLocal al = new AuditLogEJB(_em, _um);
        final Page p = new Page("foo");
        p.lock(_actor);

        // ACT
        al.recordLock(p);

        // ASSERT
        verify(_em, _um);
        assertEquals(LogEntry.Action.LOCK, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());

    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _um = createStrictMock(UserManagerLocal.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _um = null;
    }


    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
    private EntityManager _em;
    private UserManagerLocal _um;
}
