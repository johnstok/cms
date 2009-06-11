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
package ccc.services.impl;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.api.CommandType;
import ccc.domain.Folder;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


/**
 * Tests for the {@link AuditLog} class.
 *
 * @author Civic Computing Ltd.
 */
public class AuditLogImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRecordLockRejectsNull() {

        // ARRANGE
        replay(_em);
        final AuditLog al = new AuditLogImpl(_em);

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
     * @throws LockMismatchException If the resource is already locked.
     */
    public void testRecordLockPersistsLogEntry() throws LockMismatchException {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogImpl(_em);
        final Page p = new Page("foo");
        p.lock(_actor);

        // ACT
        al.recordLock(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.RESOURCE_LOCK, le.getValue().action());
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

        final AuditLog al = new AuditLogImpl(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordCreate(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.PAGE_CREATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
    }

    /**
     * Test.
     */
    public void testRecordChangeTemplate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogImpl(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordChangeTemplate(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(
            CommandType.RESOURCE_CHANGE_TEMPLATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
    }

    /**
     * Test.
     */
    public void testRecordUpdate() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogImpl(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordUpdate(p, _actor, _happenedOn, "Updated.", true);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.PAGE_UPDATE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
        assertEquals("Updated.", le.getValue().comment());
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the test fails.
     */
    public void testRecordMove() throws RemoteExceptionSupport {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogImpl(_em);
        final Page p = new Page("foo");
        final Folder f = new Folder("baz");
        f.add(p);

        // ACT
        al.recordMove(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.RESOURCE_MOVE, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
    }

    /**
     * Test.
     */
    public void testRecordRename() {

        // ARRANGE
        final Capture<LogEntry> le = new Capture<LogEntry>();
        _em.create(capture(le));
        replay(_em);

        final AuditLog al = new AuditLogImpl(_em);
        final Page p = new Page("foo");

        // ACT
        al.recordRename(p, _actor, _happenedOn);

        // ASSERT
        verify(_em);
        assertEquals(CommandType.RESOURCE_RENAME, le.getValue().action());
        assertEquals(p.id(), le.getValue().subjectId());
        assertEquals(_actor, le.getValue().actor());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _em = createStrictMock(Dao.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _em = null;
    }


    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
    private Dao _em;
}
