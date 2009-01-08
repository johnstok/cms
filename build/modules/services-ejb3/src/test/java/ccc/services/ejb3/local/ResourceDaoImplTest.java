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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.UserManager;


/**
 * TODO: Add Description for this type.
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
    public void testUpdateTags() {

        // ARRANGE
        expect(_em.find(Resource.class, _r.id())).andReturn(_r);
        replayAll();

        // ACT
        _rdao.updateTags(_r.id(), _r.version(), "foo,bar");

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
        expect(_em.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnlock(_r);
        replayAll();

        _r.lock(_regularUser);

        // ACT
        _rdao.unlock(_r.id(), _r.version());

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
        expect(_em.find(Resource.class, _r.id())).andReturn(_r);
        replayAll();

        _r.lock(_anotherUser);

        // ACT
        try {
            _rdao.unlock(_r.id(), _r.version());
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
        expect(_em.find(Resource.class, _r.id())).andReturn(_r);
        _al.recordUnlock(_r);
        replayAll();

        _r.lock(_regularUser);

        // ACT
        _rdao.unlock(_r.id(), _r.version());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testUnlockedResourceCanBeLocked() {

        // ARRANGE
        expect(_em.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        _al.recordLock(_r);
        replayAll();

        // ACT
        _rdao.lock(_r.id(), _r.version());

        // ASSERT
        assertEquals(_regularUser, _r.lockedBy());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testLockedResourceCannotBeRelockedBySomeoneElse() {

        // ARRANGE
        expect(_em.find(Resource.class, _r.id())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        replayAll();
        _r.lock(_anotherUser);

        // ACT
        try {
            _rdao.lock(_r.id(), _r.version());
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
        final List<Resource> results = Collections.singletonList(_r);

        expectList(results, "lockedResources");
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
        final List<Resource> queryResult = Collections.singletonList(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        expectList(queryResult, "resourcesLockedByUser", _regularUser);
        replayAll();

        // ACT
        final List<Resource> locked = _rdao.lockedByCurrentUser();

        // ASSERT
        verifyAll();
        assertNotNull("Shouldn't be null.", locked);
    }

    private void expectList(final List<Resource> queryResult,
                            final String queryName,
                            final Object... queryParam) {

        expect(_em.createNamedQuery(queryName)).andReturn(_q);
        for (int i=0; i<queryParam.length; i++) {
            expect(_q.setParameter((i+1), queryParam[i])).andReturn(_q);
        }
        expect(_q.getResultList()).andReturn(queryResult);
    }

    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final Folder root = new Folder(PredefinedResourceNames.CONTENT);
        final Template defaultTemplate =
            new Template("foo", "bar", "baz", "<fields/>");

        expect(_em.find(Resource.class, root.id())).andReturn(root);
        _al.recordChangeTemplate(root);
        replayAll();


        // ACT
        _rdao.updateTemplateForResource(root.id(), -1, defaultTemplate);


        // ASSERT
        verifyAll();
        assertEquals(
            defaultTemplate,
            root.template());
    }

    /**
     * Test.
     */
    public void testMove() {
        // ARRANGE
        final Folder oldParent = new Folder("old");
        final Folder newParent = new Folder("new");
        final Page resource = new Page("foo");
        oldParent.add(resource);

        expect(_em.find(Resource.class, resource.id())).andReturn(resource);
        expect(_em.find(Folder.class, newParent.id())).andReturn(newParent);
        _al.recordMove(resource);
        replayAll();

        // ACT
        _rdao.move(resource.id(), -1, newParent.id());

        // ASSERT
        verifyAll();
        assertEquals(newParent, resource.parent());
    }

    /**
     * Test.
     */
    public void testRename() {
        // ARRANGE
        final Page resource = new Page("foo");

        expect(_em.find(Resource.class, resource.id())).andReturn(resource);
        _al.recordRename(resource);
        replayAll();

        // ACT
        _rdao.rename(resource.id(), -1, "baz");

        // ASSERT
        verifyAll();
        assertEquals("baz", resource.name().toString());
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

        expect(_em.find(Page.class, bar.id())).andReturn(bar);
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

    private void replayAll() {
        replay(_em, _users, _q, _al);
    }

    private void verifyAll() {
        verify(_em, _users, _q, _al);
    }


    private UserManager _users;
    private Query _q;
    private EntityManager _em;
    private ResourceDaoImpl _rdao;
    private AuditLog _al;
    private Resource _r;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _users = createStrictMock(UserManager.class);
        _em = createStrictMock(EntityManager.class);
        _q = createStrictMock(Query.class);
        _al = createStrictMock(AuditLog.class);
        _rdao = new ResourceDaoImpl(_users, _al);
        _rdao._em = _em;
        _r = new Page("foo");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _users = null;
        _em = null;
        _rdao = null;
        _r = null;
        _q = null;
    }

    private final User _regularUser = new User("regular");
    private final User _anotherUser = new User("another");
    private final User _adminUser = new User("admin"){{
       addRole(CreatorRoles.ADMINISTRATOR);
    }};
}
