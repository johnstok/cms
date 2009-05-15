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
package ccc.services.ejb3;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.Action;
import ccc.domain.Page;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.api.ActionStatus;
import ccc.services.api.CommandFailedException;
import ccc.services.api.CommandType;
import ccc.services.api.Commands;
import ccc.services.api.Failure;
import ccc.services.api.ID;
import ccc.services.impl.ActionExecutorEJB;


/**
 * Tests for the {@link ActionExecutorEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorEJBTest
    extends
        TestCase {

    /**
     * Test.
     * @throws CommandFailedException From the Commands API.
     */
    public void testActionIsFailedIfMethodThrowsException()
                                                 throws CommandFailedException {

        // ARRANGE
        final Page p = new Page("foo");
        final User u = new User("user");
        final Action a =
            new Action(
                CommandType.RESOURCE_PUBLISH,
                new Date(),
                u,
                p,
                new Snapshot(),
                "",
                false);

        _commands.publish(
            eq(new ID(p.id().toString())),
            eq(new ID(u.id().toString())),
            isA(Date.class));
        expectLastCall().andThrow(new UnlockedException(p).toRemoteException());
        replay(_commands);

        // ACT
        _ea.executeAction(a);

        // ASSERT
        verify(_commands);
        assertEquals(ActionStatus.Failed, a.status());
        assertEquals(
            Failure.UNLOCKED,
            a.failure().getCode());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _commands = createStrictMock(Commands.class);
        _ea = new ActionExecutorEJB(_commands);
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _ea = null;
        _commands = null;
    }

    private ActionExecutorEJB _ea;
    private Commands _commands;
}
