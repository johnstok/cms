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
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.Repository;


/**
 * Tests for the {@link LogEntryRepository} class.
 * FIXME: Commented out tests.
 *
 * @author Civic Computing Ltd.
 */
public class AuditLogImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testPass() {
        // TODO: Refactor tests.
    }

//    /**
//     * Test.
//     */
//    public void testRecordLockRejectsNull() {
//
//        // ARRANGE
//        replay(_em);
//        final AuditLog al = new AuditLogImpl(_em);
//
//        // ACT
//        try {
//            al.recordLock(null, _actor, _happenedOn);
//            fail("NULL should be rejected.");
//
//        // ASSERT
//        } catch (final IllegalArgumentException e) {
//            assertEquals("Specified value may not be NULL.", e.getMessage());
//        }
//        verify(_em);
//    }

//    /**
//     * Test.
//     * @throws LockMismatchException If the resource is already locked.
//     */
//    public void testRecordLockPersistsLogEntry() throws LockMismatchException {
//
//        // ARRANGE
//        final Capture<LogEntry> le = new Capture<LogEntry>();
//        _em.create(capture(le));
//        replay(_em);
//
//        final AuditLog al = new AuditLogImpl(_em);
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//        p.lock(_actor);
//
//        // ACT
//        al.recordLock(p, _actor, _happenedOn);
//
//        // ASSERT
//        verify(_em);
//        assertEquals(CommandType.RESOURCE_LOCK, le.getValue().action());
//        assertEquals(p.id(), le.getValue().subjectId());
//        assertEquals(_actor, le.getValue().actor());
//
//    }

//    /**
//     * Test.
//     */
//    public void testRecordCreate() {
//
//        // ARRANGE
//        final Capture<LogEntry> le = new Capture<LogEntry>();
//        _em.create(capture(le));
//        replay(_em);
//
//        final AuditLog al = new AuditLogImpl(_em);
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//
//        // ACT
//        al.recordCreate(p, _actor, _happenedOn);
//
//        // ASSERT
//        verify(_em);
//        assertEquals(CommandType.PAGE_CREATE, le.getValue().action());
//        assertEquals(p.id(), le.getValue().subjectId());
//        assertEquals(_actor, le.getValue().actor());
//    }

//    /**
//     * Test.
//     */
//    public void testRecordChangeTemplate() {
//
//        // ARRANGE
//        final Capture<LogEntry> le = new Capture<LogEntry>();
//        _em.create(capture(le));
//        replay(_em);
//
//        final AuditLog al = new AuditLogImpl(_em);
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//
//        // ACT
//        al.recordChangeTemplate(p, _actor, _happenedOn);
//
//        // ASSERT
//        verify(_em);
//        assertEquals(
//            CommandType.RESOURCE_CHANGE_TEMPLATE, le.getValue().action());
//        assertEquals(p.id(), le.getValue().subjectId());
//        assertEquals(_actor, le.getValue().actor());
//    }

//    /**
//     * Test.
//     */
//    public void testRecordUpdate() {
//
//        // ARRANGE
//        final Capture<LogEntry> le = new Capture<LogEntry>();
//        _em.create(capture(le));
//        replay(_em);
//
//        final AuditLog al = new AuditLogImpl(_em);
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//
//        // ACT
//        al.recordUpdate(p, _actor, _happenedOn, "Updated.", true);
//
//        // ASSERT
//        verify(_em);
//        assertEquals(CommandType.PAGE_UPDATE, le.getValue().action());
//        assertEquals(p.id(), le.getValue().subjectId());
//        assertEquals(_actor, le.getValue().actor());
//    }

//    /**
//     * Test.
//     * @throws RemoteExceptionSupport If the test fails.
//     */
//    public void testRecordMove() throws RemoteExceptionSupport {
//
//        // ARRANGE
//        final Capture<LogEntry> le = new Capture<LogEntry>();
//        _em.create(capture(le));
//        replay(_em);
//
//        final AuditLog al = new AuditLogImpl(_em);
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//        final Folder f = new Folder("baz");
//        f.add(p);
//
//        // ACT
//        al.recordMove(p, _actor, _happenedOn);
//
//        // ASSERT
//        verify(_em);
//        assertEquals(CommandType.RESOURCE_MOVE, le.getValue().action());
//        assertEquals(p.id(), le.getValue().subjectId());
//        assertEquals(_actor, le.getValue().actor());
//    }

//    /**
//     * Test.
//     */
//    public void testRecordRename() {
//
//        // ARRANGE
//        final Capture<LogEntry> le = new Capture<LogEntry>();
//        _em.create(capture(le));
//        replay(_em);
//
//        final AuditLog al = new AuditLogImpl(_em);
//        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
//
//        // ACT
//        al.recordRename(p, _actor, _happenedOn);
//
//        // ASSERT
//        verify(_em);
//        assertEquals(CommandType.RESOURCE_RENAME, le.getValue().action());
//        assertEquals(p.id(), le.getValue().subjectId());
//        assertEquals(_actor, le.getValue().actor());
//    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _em = createStrictMock(Repository.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _em = null;
    }


    private final User _actor = new User("actor", "password");
    private final Date _happenedOn = new Date();
    private Repository _em;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
