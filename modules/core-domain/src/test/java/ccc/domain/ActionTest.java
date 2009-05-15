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
import ccc.api.ActionStatus;
import ccc.api.Failure;


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
        final Page p = new Page("page");
        final Action a = new Action();
        final UnlockedException e = new UnlockedException(p);

        // ACT
        a.fail(e.toRemoteException());

        // ASSERT
        assertEquals(ActionStatus.Failed, a.status());
        assertEquals(Failure.UNLOCKED, a.failure().getCode());
        assertEquals(e.getUUID().toString(), a.failure().getExceptionId());
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
