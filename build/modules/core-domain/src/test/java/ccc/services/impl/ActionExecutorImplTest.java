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
package ccc.services.impl;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.api.ActionStatus;
import ccc.api.CommandFailedException;
import ccc.api.CommandType;
import ccc.api.Commands;
import ccc.api.Failure;
import ccc.api.ID;
import ccc.domain.Action;
import ccc.domain.Page;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.impl.ActionExecutorImpl;


/**
 * Tests for the {@link ActionExecutorImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorImplTest
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
        _ea = new ActionExecutorImpl(_commands);
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _ea = null;
        _commands = null;
    }

    private ActionExecutorImpl _ea;
    private Commands _commands;
}
