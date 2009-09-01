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
package ccc.domain;

import junit.framework.TestCase;
import ccc.rest.CommandFailedException;
import ccc.types.ActionStatus;
import ccc.types.FailureCode;


/**
 * Tests for the {@link Action} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testCancelAnAction() {

        // ARRANGE
        final Action a = new Action();

        // ACT
        a.cancel();

        // ASSERT
        assertEquals(ActionStatus.Cancelled, a.status());
    }

    /**
     * Test.
     */
    public void testFailAnAction() {

        // ARRANGE
        final DummyResource p = new DummyResource("page");
        final Action a = new Action();
        final CommandFailedException e =
            new UnlockedException(p).toRemoteException();

        // ACT
        a.fail(e);

        // ASSERT
        assertEquals(ActionStatus.Failed, a.status());
        assertEquals(FailureCode.UNLOCKED, a.getCode());
    }

    /**
     * Test.
     */
    public void testDefaultStatusIsScheduled() {

        // ARRANGE

        // ACT
        final Action a = new Action();

        // ASSERT
        assertEquals(ActionStatus.Scheduled, a.status());
    }

    /**
     * Test.
     */
    public void testCompleteAnAction() {

        // ARRANGE
        final Action a = new Action();

        // ACT
        a.complete();

        // ASSERT
        assertEquals(ActionStatus.Complete, a.status());
    }
}
