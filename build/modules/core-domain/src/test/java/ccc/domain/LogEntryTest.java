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

import java.util.ArrayList;
import java.util.Collection;
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
        final Page p = new Page("foo");

        // ACT
        final LogEntry le = LogEntry.forRename(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Renamed resource to 'foo'.", le.comment());
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
        final Folder f = new Folder("bar");
        final Page p = new Page("foo");
        p.parent(f);

        // ACT
        final LogEntry le = LogEntry.forMove(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Moved resource to parent: "
            +f.absolutePath()+".", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.MOVE, le.action());
        assertEquals("{\"path\":\"/bar/foo\"}",
            le.detail());
    }

    /**
     * Test.
     */
    public void testCreateFactoryMethod() {

        // ARRANGE
        final Page p = new Page("foo");

        // ACT
        final LogEntry le = LogEntry.forCreate(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Created.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.CREATE, le.action());
        assertEquals("{\"title\":\"foo\","
            + "\"name\":\"foo\","
            + "\"path\":\"/foo\","
            + "\"paragraphs\":[]}", le.detail());
    }

    /**
     * Test.
     */
    public void testUpdateFactoryMethod() {

        // ARRANGE
        final Page p = new Page("foo");

        // ACT
        final LogEntry le =
            LogEntry.forUpdate(p, _actor, _happenedOn, "Updated.", true);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Updated.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UPDATE, le.action());
        assertEquals(p.createSnapshot().getDetail(), le.detail());
    }

    /**
     * Test.
     */
    public void testChangeTemplateFactoryMethod() {

        // ARRANGE
        final Page p = new Page("foo");
        final Template t = new Template(new ResourceName("newName"),
            "newTitle",
            "desc",
            "body",
            "<fields/>");
        p.template(t);

        // ACT
        final LogEntry le = LogEntry.forTemplateChange(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Template changed.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.CHANGE_TEMPLATE, le.action());
        assertEquals("{\"template\":\"newName\"}", le.detail());
    }

    /**
     * Test.
     */
    public void testLockFactoryMethod() {

        // ARRANGE
        final Folder f = new Folder("bar");
        final Page p = new Page("foo");
        p.parent(f);

        // ACT
        final LogEntry le = LogEntry.forLock(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Locked.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.LOCK, le.action());
        assertEquals("{\"lock\":\""+_actor.id().toString()+"\"}", le.detail());
    }

    /**
     * Test.
     */
    public void testUnlockFactoryMethod() {

        // ARRANGE
        final Folder f = new Folder("bar");
        final Page p = new Page("foo");
        p.parent(f);

        // ACT
        final LogEntry le = LogEntry.forUnlock(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Unlocked.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UNLOCK, le.action());
        assertEquals(
            "{\"unlock\":\""+_actor.id().toString()+"\"}",
            le.detail());
    }

    /**
     * Test.
     */
    public void testUpdateTagsFactoryMethod() {

        // ARRANGE
        final Page p = new Page("foo");
        p.tags("foo,bar");

        // ACT
        final LogEntry le = LogEntry.forUpdateTags(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Updated tags.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UPDATE_TAGS, le.action());
        assertEquals("{\"tags\":\"foo,bar\"}", le.detail());
    }

    /**
     * Test.
     */
    public void testUpdateSortOrderFactoryMethod() {

        // ARRANGE
        final Folder f = new Folder("foo");
        f.sortOrder(ResourceOrder.MANUAL);

        // ACT
        final LogEntry le = LogEntry.forUpdateSortOrder(f, _actor, _happenedOn);

        // ASSERT
        assertEquals(f.id(), le.subjectId());
        assertEquals(f.type(), le.subjectType());
        assertEquals("Updated sort order.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UPDATE_SORT_ORDER, le.action());
        assertEquals("{\"sortOrder\":\"MANUAL\"}", le.detail());
    }

    /**
     * Test.
     */
    public void testUpdateMetadataFactoryMethod() {

        // ARRANGE
        final Page p = new Page("foo");
        p.addMetadatum("bar", "zup");

        // ACT
        final LogEntry le = LogEntry.forUpdateMetadata(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Updated metadata.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.UPDATE_METADATA, le.action());
        assertEquals("{\"metadata\":{\"bar\":\"zup\"}}", le.detail());
    }

    /**
     * Test.
     */
    public void testChangeRolesFactoryMethod() {

        // ARRANGE
        final Page p = new Page("foo");
        final Collection<String> roles = new ArrayList<String>();
        roles.add("sup");
        roles.add("zep");
        p.roles(roles);

        // ACT
        final LogEntry le = LogEntry.forChangeRoles(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals(p.type(), le.subjectType());
        assertEquals("Roles changed.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(LogEntry.Action.CHANGE_ROLES, le.action());
        assertEquals("{\"roles\":\"sup,zep\"}", le.detail());
    }

    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
}
