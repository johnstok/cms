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
import ccc.domain.Page;
import ccc.domain.Snapshot;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.services.api.ActionStatus;
import ccc.services.api.CommandType;
import ccc.services.api.CommandFailedException;
import ccc.services.api.Commands;
import ccc.services.api.Failure;
import ccc.services.api.ID;


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
     * @throws CommandFailedException
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
        replay(_em, _commands);

        // ACT
        _ea.executeAction(a);

        // ASSERT
        verify(_em, _commands);
        assertEquals(ActionStatus.Failed, a.status());
        assertEquals(
            Failure.UNLOCKED,
            a.failure().getCode());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _commands = createStrictMock(Commands.class);
        _ea = new ActionExecutorEJB(_em, _commands);
        _ea.configureCoreData();
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _ea = null;
        _commands = null;
    }

    private ActionExecutorEJB _ea;
    private EntityManager _em;
    private Commands _commands;
}
