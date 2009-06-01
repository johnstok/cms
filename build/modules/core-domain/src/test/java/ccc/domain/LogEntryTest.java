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
import ccc.api.CommandType;
import ccc.api.MimeType;


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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_RENAME, le.action());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_MOVE, le.action());
        assertEquals("{\"parentId\":\""+f.id()+"\",\"path\":\"/bar/foo\"}",
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.PAGE_CREATE, le.action());
        assertEquals("{\"parentId\":null,\"title\":\"foo\","
            + "\"publishedBy\":null,"
            + "\"name\":\"foo\","
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
        assertEquals("Updated.", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.PAGE_UPDATE, le.action());
        final Snapshot s = new Snapshot();
        p.createSnapshot().toJson(s);
        assertEquals(s.getDetail(), le.detail());
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
            "<fields/>",
            MimeType.HTML);
        p.template(t);

        // ACT
        final LogEntry le = LogEntry.forTemplateChange(p, _actor, _happenedOn);

        // ASSERT
        assertEquals(p.id(), le.subjectId());
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_CHANGE_TEMPLATE, le.action());
        assertEquals("{\"templateId\":\""+t.id()+"\"}", le.detail());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_LOCK, le.action());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_UNLOCK, le.action());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_UPDATE_TAGS, le.action());
        assertEquals("{\"tags\":[\"foo\",\"bar\"]}", le.detail());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.FOLDER_UPDATE_SORT_ORDER, le.action());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_UPDATE_METADATA, le.action());
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
        assertEquals("", le.comment());
        assertEquals(_happenedOn, le.happenedOn());
        assertNull("Should be null", le.recordedOn());
        assertEquals(-1, le.index());
        assertEquals(_actor, le.actor());
        assertEquals(CommandType.RESOURCE_CHANGE_ROLES, le.action());
        assertEquals("{\"roles\":[\"sup\",\"zep\"]}", le.detail());
    }

    private final User _actor = new User("actor");
    private final Date _happenedOn = new Date();
}
