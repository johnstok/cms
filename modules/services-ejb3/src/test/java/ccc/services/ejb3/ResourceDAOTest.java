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
package ccc.services.ejb3;

import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.User;
import ccc.services.QueryManagerLocal;
import ccc.services.ResourceDAOLocal;
import ccc.services.UserManagerLocal;


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
public class ResourceDAOTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testResourceCanBeUnlockedByLockerNonadmin() {

        // ARRANGE
        expect(_users.loggedInUser()).andReturn(_regularUser);
        replay(_users, _qm);

        final ResourceDAOLocal rdao = new ResourceDAO(_users, _qm);

        _r.lock(_regularUser);

        // ACT
        rdao.unlock(_r);

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verify(_users, _qm);
    }

    /**
     * Test.
     */
    public void testResourceCannotBeUnlockedByNonlockerNonAdmin() {

        // ARRANGE
        expect(_users.loggedInUser()).andReturn(_regularUser);
        replay(_users, _qm);

        final ResourceDAOLocal rdao = new ResourceDAO(_users, _qm);

        _r.lock(_anotherUser);

        // ACT
        try {
            rdao.unlock(_r);
            fail("Should fail.");

        // ASSERT
        } catch (final CCCException e) {
            assertEquals(
                "User not allowed to unlock this resource.",
                e.getMessage());
        }
        assertEquals(_anotherUser, _r.lockedBy());
        verify(_users, _qm);
    }

    /**
     * Test.
     */
    public void testResourceCanBeUnlockedByNonlockerAdmin() {

        // ARRANGE
        expect(_users.loggedInUser()).andReturn(_adminUser);
        replay(_users, _qm);

        final ResourceDAOLocal rdao = new ResourceDAO(_users, _qm);

        _r.lock(_regularUser);

        // ACT
        rdao.unlock(_r);

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verify(_users, _qm);
    }

    /**
     * Test.
     */
    public void testUnlockedResourceCanBeLocked() {

        // ARRANGE
        expect(_qm.find(Resource.class, _r.id().toString())).andReturn(_r);
        expect(_users.loggedInUser()).andReturn(_regularUser);
        replay(_users, _qm);
        final ResourceDAOLocal rdao = new ResourceDAO(_users, _qm);

        // ACT
        rdao.lock(_r.id().toString());

        // ASSERT
        assertEquals(_regularUser, _r.lockedBy());
        verify(_users, _qm);
    }

    /**
     * Test.
     */
    public void testLockedResourceCannotBeRelockedBySomeoneElse() {

        // ARRANGE
        expect(_qm.find(Resource.class, _r.id().toString())).andReturn(_r);
        replay(_users, _qm);
        final ResourceDAOLocal rdao = new ResourceDAO(_users, _qm);
        _r.lock(_anotherUser);

        // ACT
        try {
            rdao.lock(_r.id().toString());
            fail("Lock should fail.");

        // ASSERT
        } catch (final CCCException e) {
            assertEquals("Resource is already locked.", e.getMessage());
        }
        verify(_users, _qm);
    }

    /**
     * Test.
     */
    public void testQueryAllLockedResources() {

        // ARRANGE
        final List<Resource> results = Collections.singletonList(_r);

        expect(_qm.list("lockedResources", Resource.class)).andReturn(results);
        replay(_users, _qm);

        final ResourceDAOLocal rdao = new ResourceDAO(_users, _qm);

        // ACT
        final List<Resource> locked = rdao.locked();

        // ASSERT
        assertNotNull("Shouldn't be null.", locked);
        verify(_users, _qm);
    }

    /**
     * Test.
     */
    public void testQueryResourcesLockedByUser() {

        // ARRANGE
        final List<Resource> results = Collections.singletonList(_r);

        expect(_users.loggedInUser()).andReturn(_regularUser);
        expect(_qm.list("resourcesLockedByUser", Resource.class, _regularUser))
            .andReturn(results);
        replay(_users, _qm);

        final ResourceDAOLocal rdao =
            new ResourceDAO(_users, _qm);

        // ACT
        final List<Resource> locked = rdao.lockedByCurrentUser();

        // ASSERT
        assertNotNull("Shouldn't be null.", locked);
        verify(_users, _qm);
    }


    private QueryManagerLocal _qm;
    private UserManagerLocal _users;
    private Resource _r;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _qm = createStrictMock(QueryManagerLocal.class);
        _users = createStrictMock(UserManagerLocal.class);
        _r = new Page("foo");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _qm = null;
        _users = null;
        _r = null;
    }

    private final User _regularUser = new User("regular");
    private final User _anotherUser = new User("another");
    private final User _adminUser = new User("admin"){{
       addRole(CreatorRoles.ADMINISTRATOR);
    }};
}
