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

import javax.persistence.EntityManager;

import junit.framework.TestCase;
import ccc.domain.Action;
import ccc.domain.LockMismatchException;
import ccc.domain.Page;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.api.ActionStatus;
import ccc.services.api.ActionType;


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
     * @throws LockMismatchException
     * @throws UnlockedException
     */
    public void testActionIsFailedIfMethodThrowsException()
    throws UnlockedException, LockMismatchException {

        // ARRANGE
        final Page p = new Page("foo");
        final User u = new User("user");
        replay(_em);
        final Action a =
            new Action(
                ActionType.RESOURCE_PUBLISH,
                new Date(),
                u,
                p,
                new Snapshot(),
                "",
                false);

        // ACT
        _ea.executeAction(a);

        // ASSERT
        verify(_em);
        assertEquals(ActionStatus.Failed, a.status());
        assertEquals(
            "Resource "+p.id()+" is Unlocked.",
            a.failure().getString("message"));
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _ea = new ActionExecutorEJB(_em);
        _ea.configureCoreData();
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _ea = null;
    }

    private ActionExecutorEJB _ea;
    private EntityManager _em;
}
