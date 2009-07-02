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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ccc.api.CommandType;
import ccc.api.Duration;
import ccc.api.MimeType;
import ccc.api.Paragraph;
import ccc.api.ResourceType;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.LockResourceCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.PublishCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UnlockResourceCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.InsufficientPrivilegesException;
import ccc.domain.LockMismatchException;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;
import ccc.services.QueryNames;


/**
 * Tests for the {@link ResourceDaoImpl} class.
 *
 * TODO: testQueryAllLockedResources cannot be called by non-admin?
 * TODO: Test lock(null) fails with illegal arg exception.
 * TODO: Test unlock(null) fails with illegal arg exception.
 * TODO: Test unlock() behaviour for an unlocked resource.
 * TODO: Test lock behaviour if called when by the user that already holds the
 *  lock.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testLookupReturnsNullIFRootIsMissing() {

        // ARRANGE
        expect(
            _dao.find(
                QueryNames.ROOT_BY_NAME,
                Folder.class,
                new ResourceName(PredefinedResourceNames.CONTENT)))
            .andReturn(null);
        replayAll();

        // ACT
        final Resource resource =
            _rdao.lookup(
                PredefinedResourceNames.CONTENT,
                new ResourcePath("/foo/bar"));

        // ASSERT
        verifyAll();
        assertNull("Should be null.", resource);
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testLookup() throws RemoteExceptionSupport {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");
        final Page bar =
            new Page(
                new ResourceName("bar"),
                "bar",
                null,
                _rm,
                Paragraph.fromText("default", "<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        expect(
            _dao.find(
                QueryNames.ROOT_BY_NAME,
                Folder.class,
                new ResourceName(PredefinedResourceNames.CONTENT)))
            .andReturn(contentRoot);
        replayAll();


        // ACT
        final Resource resource =
            _rdao.lookup(
                PredefinedResourceNames.CONTENT,
                new ResourcePath("/foo/bar"));


        verifyAll();
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.currentRevision().paragraphs().size());
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testIncludeInMainMenu()
    throws RemoteExceptionSupport {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new IncludeInMainMenuCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id(), true);

        // ASSERT
        verifyAll();
        assertEquals(true, _r.includeInMainMenu());

    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUpdateFullMetadata()
    throws RemoteExceptionSupport {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        new UpdateResourceMetadataCommand(_dao, _al).execute(
            _regularUser,
            new Date(),
            _r.id(),
            "newTitle",
            "newDesc",
            "foo,bar",
            props);

        // ASSERT
        verifyAll();
        assertEquals("example", _r.getMetadatum("bodyId"));
        assertEquals("newTitle", _r.title());
        assertEquals("newDesc", _r.description());
        assertTrue(_r.tags().contains("foo"));
        assertTrue(_r.tags().contains("bar"));
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testResourceCanBeUnlockedByLockerNonadmin()
    throws RemoteExceptionSupport {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        _r.lock(_regularUser);

        // ACT
        new UnlockResourceCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testResourceCannotBeUnlockedByNonlockerNonAdmin()
    throws RemoteExceptionSupport {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        replayAll();

        _r.lock(_anotherUser);

        // ACT
        try {
            new UnlockResourceCommand(_dao, _al).execute(
                _regularUser, new Date(), _r.id());
            fail("Should fail.");

        // ASSERT
        } catch (final InsufficientPrivilegesException e) {
            assertEquals(
                "User regular[] may not perform action: "
                +CommandType.RESOURCE_UNLOCK,
                e.getMessage());
        }
        assertEquals(_anotherUser, _r.lockedBy());
        verifyAll();
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testResourceCanBeUnlockedByNonlockerAdmin()
    throws RemoteExceptionSupport {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        _r.lock(_regularUser);

        // ACT
        new UnlockResourceCommand(_dao, _al).execute(
            _adminUser, new Date(), _r.id());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUnlockedResourceCanBeLocked()
    throws RemoteExceptionSupport {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new LockResourceCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id());

        // ASSERT
        assertEquals(_regularUser, _r.lockedBy());
        verifyAll();
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testLockedResourceCannotBeRelockedBySomeoneElse()
    throws RemoteExceptionSupport {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        replayAll();
        _r.lock(_anotherUser);

        // ACT
        try {
            new LockResourceCommand(_dao, _al).execute(
                _regularUser, new Date(), _r.id());
            fail("Lock should fail.");

        // ASSERT
        } catch (final LockMismatchException e) {
            assertEquals(_r, e.resource());
        }
        verifyAll();
    }

    /**
     * Test.
     */
    public void testQueryAllLockedResources() {

        // ARRANGE
        expect(_dao.list(QueryNames.LOCKED_RESOURCES, Resource.class))
            .andReturn(Collections.singletonList(_r));
        replayAll();

        // ACT
        final List<Resource> locked = _rdao.locked();

        // ASSERT
        assertNotNull("Shouldn't be null.", locked);
        verifyAll();
    }

    /**
     * Test.
     */
    public void testQueryResourcesLockedByUser() {

        // ARRANGE
        expect(_dao.list("resourcesLockedByUser", Resource.class, _regularUser))
            .andReturn(Collections.singletonList(_r));
        replayAll();

        // ACT
        final List<Resource> locked = _rdao.lockedByUser(_regularUser);

        // ASSERT
        verifyAll();
        assertNotNull("Shouldn't be null.", locked);
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testSetDefaultTemplate()
    throws RemoteExceptionSupport {

        // ARRANGE
        final Template defaultTemplate =
            new Template(
                "foo",
                "bar",
                "baz",
                "<fields/>",
                MimeType.HTML,
                new RevisionMetadata(
                    new Date(),
                    User.SYSTEM_USER,
                    true,
                    "Created."));
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id()))
            .andReturn(_r);
        expect(_dao.find(Template.class, defaultTemplate.id()))
            .andReturn(defaultTemplate);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ChangeTemplateForResourceCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id(), defaultTemplate.id());

        // ASSERT
        verifyAll();
        assertEquals(defaultTemplate, _r.template());
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testMove()
    throws RemoteExceptionSupport {

        // ARRANGE
        final Folder oldParent = new Folder("old");
        final Folder newParent = new Folder("new");
        oldParent.add(_r);
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_dao.find(Folder.class, newParent.id())).andReturn(newParent);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new MoveResourceCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id(), newParent.id());

        // ASSERT
        verifyAll();
        assertEquals(newParent, _r.parent());
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testRename()
    throws RemoteExceptionSupport {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new RenameResourceCommand(_dao, _al).rename(
            _regularUser, new Date(), _r.id(), "baz");

        // ASSERT
        verifyAll();
        assertEquals("baz", _r.name().toString());
    }

    /**
     * Test.
     */
    public void testFind() {

        // ARRANGE
        final Page bar =
            new Page(
                new ResourceName("bar"),
                "bar",
                null,
                _rm,
                Paragraph.fromText("default", "<H1>Default</H1>"));

        expect(_dao.find(Page.class, bar.id())).andReturn(bar);
        replayAll();


        // ACT
        final Resource resource =
            _rdao.find(Page.class, bar.id());


        // ASSERT
        verifyAll();
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.currentRevision().paragraphs().size());
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testPublish() throws RemoteExceptionSupport {

        // ARRANGE
        _r.lock(_regularUser);

        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new PublishCommand(_al).execute(new Date(), _regularUser, _r);

        // ASSERT
        verifyAll();
        assertEquals(_regularUser, _r.publishedBy());
    }

    /**
     * Test.
     * TODO: Broken.
     */
    public void testPublishWithUser() {
//
//        // ARRANGE
//        _r.lock(_regularUser);
//
//        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
//        expect(_users.loggedInUser()).andReturn(_regularUser);
//        expect(_users.find(_regularUser.id())).andReturn(_regularUser);
//        _al.recordPublish(eq(_r), eq(_regularUser), isA(Date.class));
//        replayAll();
//
//        // ACT
//        _rdao.publish(_r.id(), _regularUser.id(), new Date());
//
//        // ASSERT
//        verifyAll();
//        assertEquals(_regularUser, _r.publishedBy());
    }

    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUnpublishWithUser()
    throws RemoteExceptionSupport {

        // ARRANGE
        _r.lock(_regularUser);
        _r.publish(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UnpublishResourceCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id());

        // ASSERT
        verifyAll();
        assertEquals(null, _r.publishedBy());
    }


    /**
     * Test.
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUpdateCache()
    throws RemoteExceptionSupport {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateCachingCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id(), new Duration(0, 1, 2, 7));

        // ASSERT
        verifyAll();
        assertEquals(3727, _r.cache().time());
    }


    private void replayAll() {
        replay(_dao, _al);
    }

    private void verifyAll() {
        verify(_dao, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(Dao.class);
        _al = createStrictMock(AuditLog.class);
        _rdao = new ResourceDaoImpl(_dao);
        _r = new Page(new ResourceName("foo"), "foo", null, _rm);
        _parent = new Folder("parent");
        _parent.add(_r);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _parent = null;
        _r      = null;
        _rdao   = null;
        _al     = null;
        _dao    = null;
    }


    private Dao _dao;
    private AuditLog _al;
    private ResourceDaoImpl _rdao;
    private Resource _r;
    private Folder _parent;

    private final User _regularUser = new User("regular");
    private final User _anotherUser = new User("another");
    private final User _adminUser = new User("admin"){{
       addRole(CreatorRoles.ADMINISTRATOR);
    }};
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
