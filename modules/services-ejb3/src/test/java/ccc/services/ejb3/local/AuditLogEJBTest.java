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
package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.UserManager;


/**
 * Tests for the {@link AuditLog} class.
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
        final AuditLog al = new AuditLogEJB(_em, _um);

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
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLog al = new AuditLogEJB(_em, _um);
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

    /**
     * Test.
     */
    public void testRecordCreate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLog al = new AuditLogEJB(_em, _um);
        final Page p = new Page("foo");

        // ACT
        al.recordCreate(p);

        // ASSERT
        verify(_em, _um);
        assertEquals(LogEntry.Action.CREATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Created.", le.getValue().summary());
    }

    /**
     * Test.
     */
    public void testRecordChangeTemplate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLog al = new AuditLogEJB(_em, _um);
        final Page p = new Page("foo");

        // ACT
        al.recordChangeTemplate(p);

        // ASSERT
        verify(_em, _um);
        assertEquals(LogEntry.Action.CHANGE_TEMPLATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Template changed.", le.getValue().summary());
    }

    /**
     * Test.
     */
    public void testRecordUpdate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLog al = new AuditLogEJB(_em, _um);
        final Page p = new Page("foo");

        // ACT
        al.recordUpdate(p);

        // ASSERT
        verify(_em, _um);
        assertEquals(LogEntry.Action.UPDATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Updated.", le.getValue().summary());
    }

    /**
     * Test.
     */
    public void testRecordMove() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLog al = new AuditLogEJB(_em, _um);
        final Page p = new Page("foo");
        final Folder f = new Folder("baz");
        f.add(p);

        // ACT
        al.recordMove(p);

        // ASSERT
        verify(_em, _um);
        assertEquals(LogEntry.Action.MOVE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Moved resource to parent: "+f.absolutePath()+".",
            le.getValue().summary());
    }

    /**
     * Test.
     */
    public void testRecordRename() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.persist(capture(le));
        replay(_em);

        expect(_um.loggedInUser()).andReturn(_actor);
        replay(_um);

        final AuditLog al = new AuditLogEJB(_em, _um);
        final Page p = new Page("foo");

        // ACT
        al.recordRename(p);

        // ASSERT
        verify(_em, _um);
        assertEquals(LogEntry.Action.RENAME, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Renamed resource to '"+p.name().toString()+"'.",
            le.getValue().summary());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _um = createStrictMock(UserManager.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _um = null;
    }


    private final User _actor = new User("actor");
    private EntityManager _em;
    private UserManager _um;
}
