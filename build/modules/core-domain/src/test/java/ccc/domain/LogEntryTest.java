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
 * Tests for the {@link LogEntry} class.
 * TODO: Test that summary cannot be longer than 1024 chars.
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
        assertEquals("Moved resource to parent: "+f.id()+".", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.MOVE, le.action());
        assertEquals(p.toString(), le.detail());
    }

    /**
     * Test.
     */
    public void testCreateFactoryMethod() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));

        // ACT
        final LogEntry le = LogEntry.forCreate(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Created.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.CREATE, le.action());
        assertEquals(p.toString(), le.detail());
    }

    /**
     * Test.
     */
    public void testUpdateFactoryMethod() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));

        // ACT
        final LogEntry le = LogEntry.forUpdate(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Updated.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UPDATE, le.action());
        assertEquals(p.toString(), le.detail());
    }

    /**
     * Test.
     */
    public void testChangeTemplateFactoryMethod() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"));

        // ACT
        final LogEntry le = LogEntry.forTemplateChange(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Template changed.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.CHANGE_TEMPLATE, le.action());
        assertEquals(p.toString(), le.detail());
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
        assertEquals("Locked.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.LOCK, le.action());
        assertEquals(p.toString(), le.detail());
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
        assertEquals("Unlocked.", le.summary());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UNLOCK, le.action());
        assertEquals(p.toString(), le.detail());
    }

    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
}
