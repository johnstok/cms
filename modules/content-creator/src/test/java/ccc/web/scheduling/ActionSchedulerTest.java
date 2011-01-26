/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.web.scheduling;

import junit.framework.TestCase;
import ccc.api.core.Actions2;
import ccc.api.core.Scheduler;
import ccc.commons.Testing;
import ccc.plugins.security.Sessions;


/**
 * Tests for the {@link ActionScheduler} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionSchedulerTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testSchedulerIsStoppedByDefault() {

        // ARRANGE
        final Scheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));

        // ACT
        final boolean startedOnInit = s.isRunning();

        // ASSERT
        assertFalse(startedOnInit);
    }


    /**
     * Test.
     */
    public void testStartStop() {

        // ARRANGE
        final Scheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));

        // ACT
        s.start();
        final boolean startedAfterStart = s.isRunning();

        s.stop();
        final boolean startedAfterStop = s.isRunning();

        // ASSERT
        assertTrue(startedAfterStart);
        assertFalse(startedAfterStop);
    }


    /**
     * Test.
     */
    public void testCancelledSchedulersCantBeStarted() {

        // ARRANGE
        final ActionScheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));
        s.cancel();

        // ACT
        try {
            s.start();

        // ASSERT
        } catch (final IllegalStateException e) {
            assertEquals("Timer already cancelled.", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testCancelIsIdempotent() {

        // ARRANGE
        final Scheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));

        // ACT
        s.stop();
        s.stop();

        // ASSERT
    }


    /**
     * Test.
     */
    public void testStartIsIdempotent() {

        // ARRANGE
        final Scheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));

        // ACT
        s.start();
        s.start();

        // ASSERT
    }
}
