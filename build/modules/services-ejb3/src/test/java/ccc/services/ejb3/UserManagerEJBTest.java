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

    private EntityManager em;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        em = createStrictMock(EntityManager.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        em = null;
    }

    /**
     * Test.
     */
    public void testCreateUser() {

        // ARRANGE

        final User u = new User("fooDummy");
        em.persist(u);
        replay(em);

        final UserManagerEJB um = new UserManagerEJB(em);

        // ACT
        um.createUser(u);

        // ASSERT
        verify(em);

    }
}
