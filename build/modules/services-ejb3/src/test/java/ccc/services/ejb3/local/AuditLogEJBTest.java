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

import java.util.Date;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.Dao;


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
        replay(_em);
        final AuditLog al = new AuditLogEJB(_em);

        // ACT
        try {
            al.recordLock(null, _actor, _happenedOn);
            fail("NULL should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
        verify(_em);
    }

    /**
     * Test.
     */
    public void testRecordLockPersistsLogEntry() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogEJB(_em);
        final Page p = new Page("foo");
        p.lock(_actor);

        // ACT
        al.recordLock(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
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
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogEJB(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordCreate(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(LogEntry.Action.CREATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Created.", le.getValue().comment());
    }

    /**
     * Test.
     */
    public void testRecordChangeTemplate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogEJB(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordChangeTemplate(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(LogEntry.Action.CHANGE_TEMPLATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Template changed.", le.getValue().comment());
    }

    /**
     * Test.
     */
    public void testRecordUpdate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogEJB(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordUpdate(p, _actor, _happenedOn, "Updated.", true);

        // ASSERT
        verify(_em);
        assertEquals(LogEntry.Action.UPDATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Updated.", le.getValue().comment());
    }

    /**
     * Test.
     */
    public void testRecordMove() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogEJB(_em);
        final Page p = new Page("foo");
        final Folder f = new Folder("baz");
        f.add(p);

        // ACT
        al.recordMove(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(LogEntry.Action.MOVE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Moved resource to parent: "+f.absolutePath()+".",
            le.getValue().comment());
    }

    /**
     * Test.
     */
    public void testRecordRename() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogEJB(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordRename(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(LogEntry.Action.RENAME, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Renamed resource to '"+p.name().toString()+"'.",
            le.getValue().comment());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(Dao.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
    }


    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
    private Dao _em;
}
