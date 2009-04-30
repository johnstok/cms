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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.UserManager;
import ccc.services.ejb3.support.Dao;
import ccc.services.ejb3.support.QueryNames;


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
     */
    public void testLookup() {

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
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordIncludeInMainMenu(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.includeInMainMenu(_r.id(), true);

        // ASSERT
        verifyAll();
        assertEquals(true, _r.includeInMainMenu());

    }

    /**
     * Test.
     */
    public void testUpdateTags() {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUpdateTags(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.updateTags(_r.id(), "foo,bar");

        // ASSERT
        verifyAll();
        assertEquals(2, _r.tags().size());
        assertEquals("foo", _r.tags().get(0));
        assertEquals("bar", _r.tags().get(1));
    }

    /**
     * Test.
     */
    public void testResourceCanBeUnlockedByLockerNonadmin() {

        // ARRANGE
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnlock(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        _r.lock(_regularUser);

        // ACT
        _rdao.unlock(_r.id());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testResourceCannotBeUnlockedByNonlockerNonAdmin() {

        // ARRANGE
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        replayAll();

        _r.lock(_anotherUser);

        // ACT
        try {
            _rdao.unlock(_r.id());
            fail("Should fail.");

        // ASSERT
        } catch (final CCCException e) {
            assertEquals(
                "User not allowed to unlock this resource.",
                e.getMessage());
        }
        assertEquals(_anotherUser, _r.lockedBy());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testResourceCanBeUnlockedByNonlockerAdmin() {

        // ARRANGE
        expect(_users.loggedInUser()).andReturn(_adminUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnlock(eq(_r), eq(_adminUser), isA(Date.class));
        replayAll();

        _r.lock(_regularUser);

        // ACT
        _rdao.unlock(_r.id());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testUnlockedResourceCanBeLocked() {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        _al.recordLock(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.lock(_r.id());

        // ASSERT
        assertEquals(_regularUser, _r.lockedBy());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testLockedResourceCannotBeRelockedBySomeoneElse() {

        // ARRANGE
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        replayAll();
        _r.lock(_anotherUser);

        // ACT
        try {
            _rdao.lock(_r.id());
            fail("Lock should fail.");

        // ASSERT
        } catch (final CCCException e) {
            assertEquals("Resource is already locked.", e.getMessage());
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
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.list("resourcesLockedByUser", Resource.class, _regularUser))
            .andReturn(Collections.singletonList(_r));
        replayAll();

        // ACT
        final List<Resource> locked = _rdao.lockedByCurrentUser();

        // ASSERT
        verifyAll();
        assertNotNull("Shouldn't be null.", locked);
    }

    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final Template defaultTemplate =
            new Template("foo", "bar", "baz", "<fields/>");
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        _al.recordChangeTemplate(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.updateTemplateForResource(_r.id(), defaultTemplate);

        // ASSERT
        verifyAll();
        assertEquals(defaultTemplate, _r.template());
    }

    /**
     * Test.
     */
    public void testMove() {

        // ARRANGE
        final Folder oldParent = new Folder("old");
        final Folder newParent = new Folder("new");
        oldParent.add(_r);
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_dao.find(Folder.class, newParent.id())).andReturn(newParent);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        _al.recordMove(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.move(_r.id(), newParent.id());

        // ASSERT
        verifyAll();
        assertEquals(newParent, _r.parent());
    }

    /**
     * Test.
     */
    public void testRename() {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordRename(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.rename(_r.id(), "baz");

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
     */
    public void testCreateFailsWhenResourceExists() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder fooFolder = new Folder("foo");
        contentRoot.add(fooFolder);

        expect(_dao.find(Folder.class, contentRoot.id()))
            .andReturn(contentRoot);
        replayAll();


        // ACT
        try {
            _rdao.create(contentRoot.id(), _r);
            fail("Creation of duplicates should fail.");

        } catch (final CCCException e) {
            assertEquals(
                "Folder already contains a resource with name 'foo'.",
                e.getMessage());
        }


        // ASSERT
        verifyAll();
        assertEquals(1, contentRoot.size());
        assertSame(fooFolder, contentRoot.entries().get(0));
    }

    /**
     * Test.
     */
    public void testCreate() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);

        _al.recordCreate(eq(_r), eq(_regularUser), isA(Date.class));
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Folder.class, contentRoot.id()))
            .andReturn(contentRoot);
        _dao.create(_r);
        replayAll();


        // ACT
        _rdao.create(contentRoot.id(), _r);


        // ASSERT
        verifyAll();
        assertEquals(1, contentRoot.size());
        assertEquals(_r, contentRoot.entries().get(0));
    }

    /**
     * Test.
     */
    public void testPublish() {

        // ARRANGE
        _r.lock(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        _al.recordPublish(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.publish(_r.id());

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
     */
    public void testUnpublishWithUser() {

        // ARRANGE
        _r.lock(_regularUser);
        _r.publish(_regularUser);

        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        _al.recordUnpublish(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.unpublish(_r.id());

        // ASSERT
        verifyAll();
        assertEquals(null, _r.publishedBy());
    }


    /**
     * Test.
     */
    public void testUpdateMetadata() {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUpdateMetadata(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        _rdao.updateMetadata(_r.id(), props);

        // ASSERT
        verifyAll();
        assertEquals("example", _r.getMetadatum("bodyId"));
    }

    /**
     * Test.
     */
    public void testUpdateCache() {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUpdateCache(eq(_r), eq(_regularUser), isA(Date.class));
        replayAll();

        // ACT
        _rdao.updateCache(_r.id(), "1246");

        // ASSERT
        verifyAll();
        assertEquals(1246, _r.cache().time());
    }


    private void replayAll() {
        replay(_dao, _users, _al);
    }

    private void verifyAll() {
        verify(_dao, _users, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _users = createStrictMock(UserManager.class);
        _dao = createStrictMock(Dao.class);
        _al = createStrictMock(AuditLog.class);

        _rdao = new ResourceDaoImpl(_users, _al, _dao);
        _r = new Page("foo");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _users = null;
        _dao = null;
        _rdao = null;
        _r = null;
    }


    private UserManager _users;
    private Dao _dao;
    private AuditLog _al;
    private ResourceDaoImpl _rdao;
    private Resource _r;

    private final User _regularUser = new User("regular");
    private final User _anotherUser = new User("another");
    private final User _adminUser = new User("admin"){{
       addRole(CreatorRoles.ADMINISTRATOR);
    }};
}
