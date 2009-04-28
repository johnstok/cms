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
package ccc.actions;

import junit.framework.TestCase;
import ccc.commons.Exceptions;


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
        assertEquals(Action.Status.Cancelled, a.status());
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
        assertEquals(Action.Status.Failed, a.status());
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
        assertEquals(Action.Status.Scheduled, a.status());
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
        assertEquals(Action.Status.Complete, a.status());
    }
}