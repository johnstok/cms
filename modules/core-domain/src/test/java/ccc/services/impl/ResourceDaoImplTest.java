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
import ccc.commands.ChangeResourceTagsCommand;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.LockResourceCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.PublishCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UnlockResourceCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceMetadataRolesCommand;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.InsufficientPrivilegesException;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceExistsException;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.Template;
import ccc.domain.UnlockedException;
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
     * @throws ResourceExistsException
     */
    public void testLookup() throws ResourceExistsException {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");
        final Page bar =
            new Page("bar")
                .addParagraph(
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
        assertEquals(1, page.paragraphs().size());
    }

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testIncludeInMainMenu()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordIncludeInMainMenu(eq(_r), eq(_regularUser), isA(Date.class));
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
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUpdateTags()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUpdateTags(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        new ChangeResourceTagsCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id(), "foo,bar");

        // ASSERT
        verifyAll();
        assertEquals(2, _r.tags().size());
        assertEquals("foo", _r.tags().get(0));
        assertEquals("bar", _r.tags().get(1));
    }

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws InsufficientPrivilegesException
     */
    public void testResourceCanBeUnlockedByLockerNonadmin()
    throws LockMismatchException, InsufficientPrivilegesException, UnlockedException {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnlock(eq(_r), eq(_regularUser), isA(Date.class));
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
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testResourceCannotBeUnlockedByNonlockerNonAdmin()
    throws LockMismatchException, UnlockedException {

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
     * @throws LockMismatchException
     * @throws UnlockedException
     * @throws InsufficientPrivilegesException
     */
    public void testResourceCanBeUnlockedByNonlockerAdmin()
    throws LockMismatchException, InsufficientPrivilegesException, UnlockedException {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnlock(eq(_r), eq(_adminUser), isA(Date.class));
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
     * @throws LockMismatchException
     */
    public void testUnlockedResourceCanBeLocked() throws LockMismatchException {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordLock(eq(_r), eq(_regularUser), isA(Date.class));
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
     * @throws LockMismatchException
     */
    public void testLockedResourceCannotBeRelockedBySomeoneElse()
    throws LockMismatchException {

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
        expect(_dao.list("lockedResources", Resource.class))
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
        final List<Resource> locked = _rdao.lockedByCurrentUser(_regularUser);

        // ASSERT
        verifyAll();
        assertNotNull("Shouldn't be null.", locked);
    }

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testSetDefaultTemplate()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        final Template defaultTemplate =
            new Template("foo", "bar", "baz", "<fields/>", MimeType.HTML);
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id()))
            .andReturn(_r);
        expect(_dao.find(Template.class, defaultTemplate.id()))
            .andReturn(defaultTemplate);
        _al.recordChangeTemplate(eq(_r), eq(_regularUser), isA(Date.class));
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
     * @throws ResourceExistsException
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testMove()
    throws ResourceExistsException, LockMismatchException, UnlockedException {

        // ARRANGE
        final Folder oldParent = new Folder("old");
        final Folder newParent = new Folder("new");
        oldParent.add(_r);
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_dao.find(Folder.class, newParent.id())).andReturn(newParent);
        _al.recordMove(eq(_r), eq(_regularUser), isA(Date.class));
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
     * @throws ResourceExistsException
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testRename()
    throws UnlockedException, LockMismatchException, ResourceExistsException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordRename(eq(_r), eq(_regularUser), isA(Date.class));
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
            new Page("bar")
                .addParagraph(
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
        assertEquals(1, page.paragraphs().size());
    }

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testPublish() throws LockMismatchException, UnlockedException {

        // ARRANGE
        _r.lock(_regularUser);

        _al.recordPublish(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        new PublishCommand(_al).execute(new Date(), _regularUser, _r);

        // ASSERT
        verifyAll();
        assertEquals(_regularUser, _r.publishedBy());
    }

    /**
     * Test.
     * FIXME: Broken.
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
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUnpublishWithUser()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        _r.lock(_regularUser);
        _r.publish(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnpublish(eq(_r), eq(_regularUser), isA(Date.class));
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
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUpdateMetadata()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUpdateMetadata(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        new UpdateResourceMetadataRolesCommand(_dao, _al).execute(
            _regularUser, new Date(), _r.id(), props);

        // ASSERT
        verifyAll();
        assertEquals("example", _r.getMetadatum("bodyId"));
    }

    /**
     * Test.
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testUpdateCache()
    throws LockMismatchException, UnlockedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUpdateCache(eq(_r), eq(_regularUser), isA(Date.class));
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
        _r = new Page("foo");
        _parent = new Folder("parent");
        _parent.add(_r);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
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
}
