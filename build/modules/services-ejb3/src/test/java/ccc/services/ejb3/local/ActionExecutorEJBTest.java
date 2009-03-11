/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.Date;

import junit.framework.TestCase;
import ccc.actions.Action;
import ccc.domain.Page;
import ccc.domain.Snapshot;
import ccc.domain.User;
import ccc.services.ResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorEJBTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testActionIsFailedIfMethodThrowsException() {

        // ARRANGE
        final Page p = new Page("foo");
        final User u = new User("user");
        expect(_rdao.publish(eq(p.id()), eq(u.id()), isA(Date.class)))
            .andThrow(new RuntimeException("Oops!"));
        replay(_rdao);
        final Action a =
            new Action(Action.Type.PUBLISH, new Date(), u, p, new Snapshot());

        // ACT
        _ea.executeAction(a);

        // ASSERT
        verify(_rdao);
        assertEquals(Action.Status.Failed, a.status());
        assertEquals("Oops!", a.failure().getString("message"));
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _rdao = createStrictMock(ResourceDao.class);
        _ea = new ActionExecutorEJB(_rdao);
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _rdao = null;
        _ea = null;
    }

    private ActionExecutorEJB _ea;
    private ResourceDao _rdao;
}
