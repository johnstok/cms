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
package ccc.domain;

import java.util.Date;

import junit.framework.TestCase;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntryTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRenameFactoryMethod() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));

        // ACT
        final LogEntry le = LogEntry.forRename(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals(-1, le.subjectVersionAfterChange());
        assertEquals("Renamed resource to 'foo'.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.RENAME, le.action());
    }

    /**
     * Test.
     */
    public void testMoveFactoryMethod() {

        // ARRANGE
        final Folder f = new Folder(new ResourceName("bar"));
        final Page p = new Page(new ResourceName("foo"));
        p.parent(f);

        // ACT
        final LogEntry le = LogEntry.forMove(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals(-1, le.subjectVersionAfterChange());
        assertEquals("Moved resource to parent: "+f.id()+".", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.MOVE, le.action());
    }

    /**
     * Test.
     */
    public void testLockFactoryMethod() {

        // ARRANGE
        final Folder f = new Folder(new ResourceName("bar"));
        final Page p = new Page(new ResourceName("foo"));
        p.parent(f);

        // ACT
        final LogEntry le = LogEntry.forLock(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals(-1, le.subjectVersionAfterChange());
        assertEquals("Locked.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.LOCK, le.action());
    }

    /**
     * Test.
     */
    public void testUnlockFactoryMethod() {

        // ARRANGE
        final Folder f = new Folder(new ResourceName("bar"));
        final Page p = new Page(new ResourceName("foo"));
        p.parent(f);

        // ACT
        final LogEntry le = LogEntry.forUnlock(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals(-1, le.subjectVersionAfterChange());
        assertEquals("Unlocked.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UNLOCK, le.action());
    }

    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
}
