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

import javax.persistence.EntityManager;

import junit.framework.TestCase;
import ccc.domain.User;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserManagerEJBTest extends TestCase {

    private EntityManager _em;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
    }

    /**
     * Test.
     */
    public void testCreateUser() {

        // ARRANGE

        final User u = new User("fooDummy");
        _em.persist(u);
        replay(_em);

        final UserManagerEJB um = new UserManagerEJB(_em);

        // ACT
        um.createUser(u);

        // ASSERT
        verify(_em);

    }
}
