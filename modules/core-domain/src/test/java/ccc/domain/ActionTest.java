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
import ccc.commons.Exceptions;
import ccc.domain.Action;
import ccc.services.api.ActionStatus;


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
        final Action a = new Action();
        final Exception e = new Exception("Outer", new Exception("Oops!"));

        // ACT
        a.fail(e);

        // ASSERT
        assertEquals(ActionStatus.Failed, a.status());
        assertEquals("Oops!", a.failure().getString("message"));
        assertEquals(
            Exceptions.stackTraceFor(e), a.failure().getString("stack"));
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
