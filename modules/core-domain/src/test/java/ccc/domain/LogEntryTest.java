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
import ccc.types.Username;


/**
 * Tests for the {@link LogEntry} class.
 * TODO: Test that summary cannot be longer than 1024 chars.
 * FIXME: Commented out tests.
 *
 * @author Civic Computing Ltd.
 */
public class LogEntryTest
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
//    public void testRenameFactoryMethod() {
//
//        // ARRANGE
//        final Page p = new Page("foo");
//
//        // ACT
//        final LogEntry le = LogEntry.forRename(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_RENAME, le.action());
//    }

//    /**
//     * Test.
//     */
//    public void testMoveFactoryMethod() {
//
//        // ARRANGE
//        final Folder f = new Folder("bar");
//        final DummyResource p = new DummyResource("foo");
//        p.parent(f);
//
//        // ACT
//        final LogEntry le = LogEntry.forMove(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_MOVE, le.action());
//        assertEquals("{\"parentId\":\""+f.id()+"\",\"path\":\"/bar/foo\"}",
//            le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testCreateFactoryMethod() {
//
//        // ARRANGE
//        final DummyResource p = new DummyResource("foo");
//
//        // ACT
//        final LogEntry le = LogEntry.forCreate(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.FOLDER_CREATE, le.action());
//        assertEquals("{\"parentId\":null,"
//            + "\"publishedBy\":null,"
//            + "\"name\":\"foo\"}", le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testUpdateFactoryMethod() {
//
//        // ARRANGE
//        final DummyResource p = new DummyResource("foo");
//
//        // ACT
//        final LogEntry le =
//            LogEntry.forUpdate(p, _actor, _happenedOn, "Updated.", true);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.FOLDER_UPDATE, le.action());
//        final Snapshot s = new Snapshot();
//        p.createSnapshot().toJson(s);
//        assertEquals(s.getDetail(), le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testChangeTemplateFactoryMethod() {
//
//        // ARRANGE
//        final DummyResource p = new DummyResource("foo");
//        final Template t = new Template(new ResourceName("newName"),
//            "newTitle",
//            "desc",
//            "body",
//            "<fields/>",
//            MimeType.HTML,
//            _rm);
//        p.template(t);
//
//        // ACT
//        final LogEntry le = LogEntry.forTemplateChange(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_CHANGE_TEMPLATE, le.action());
//        assertEquals("{\"templateId\":\""+t.id()+"\"}", le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testLockFactoryMethod() {
//
//        // ARRANGE
//        final Folder f = new Folder("bar");
//        final DummyResource p = new DummyResource("foo");
//        p.parent(f);
//
//        // ACT
//        final LogEntry le = LogEntry.forLock(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_LOCK, le.action());
//        assertEquals("{\"lock\":\""+_actor.id().toString()+"\"}", le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testUnlockFactoryMethod() {
//
//        // ARRANGE
//        final Folder f = new Folder("bar");
//        final DummyResource p = new DummyResource("foo");
//        p.parent(f);
//
//        // ACT
//        final LogEntry le = LogEntry.forUnlock(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_UNLOCK, le.action());
//        assertEquals(
//            "{\"unlock\":\""+_actor.id().toString()+"\"}",
//            le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testUpdateMetadataFactoryMethod() {
//
//        // ARRANGE
//        final DummyResource p = new DummyResource("foo");
//        p.tags("foo,bar");
//        p.title("newTitle");
//        p.description("newDesc");
//        p.addMetadatum("bar", "zup");
//
//        // ACT
//        final LogEntry le =
//            LogEntry.forUpdateMetadata(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_UPDATE_METADATA, le.action());
//        assertEquals("{\"tags\":[\"foo\",\"bar\"],\"title\":\"newTitle\","
//        		+ "\"description\":\"newDesc\",\"metadata\":{\"bar\":\"zup\"}}",
//        		le.detail());
//    }

//    /**
//     * Test.
//     */
//    public void testUpdateSortOrderFactoryMethod() {
//
//        // ARRANGE
//        final Folder f = new Folder("foo");
//        f.sortOrder(ResourceOrder.MANUAL);
//
//        // ACT
//        final LogEntry le = LogEntry.forFolderUpdate(f, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(f.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.FOLDER_UPDATE, le.action());
//        assertEquals(
//            "{\"indexPageId\":null,\"sortOrder\":\"MANUAL\"}", le.detail());
//    }


//    /**
//     * Test.
//     */
//    public void testChangeRolesFactoryMethod() {
//
//        // ARRANGE
//        final Page p = new Page("foo");
//        final Collection<String> roles = new ArrayList<String>();
//        roles.add("sup");
//        roles.add("zep");
//        p.roles(roles);
//
//        // ACT
//        final LogEntry le = LogEntry.forChangeRoles(p, _actor, _happenedOn);
//
//        // ASSERT
//        assertEquals(p.id(), le.subjectId());
//        assertEquals(_happenedOn, le.happenedOn());
//        assertNull("Should be null", le.recordedOn());
//        assertEquals(-1, le.index());
//        assertEquals(_actor, le.actor());
//        assertEquals(CommandType.RESOURCE_CHANGE_ROLES, le.action());
//        assertEquals("{\"roles\":[\"sup\",\"zep\"]}", le.detail());
//    }

    private final User _actor = new User(new Username("actor"), "password");
    private final Date _happenedOn = new Date();
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
