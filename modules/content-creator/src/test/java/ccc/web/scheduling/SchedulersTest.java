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
import ccc.commons.Testing;
import ccc.plugins.security.Sessions;


/**
 * Tests for the {@link Schedulers} class.
 *
 * @author Civic Computing Ltd.
 */
public class SchedulersTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testSetGetActionScheduler() {

        // ARRANGE
        final ActionScheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));

        // ACT
        Schedulers.setInstance(s);

        // ASSERT
        assertSame(s, Schedulers.getInstance());
    }

    /**
     * Test.
     */
    public void testSetActionSchedulerIgnoredIfAlreadySet() {

        // ARRANGE
        final ActionScheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));
        final ActionScheduler t =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));

        // ACT
        Schedulers.setInstance(s);
        Schedulers.setInstance(t);

        // ASSERT
        assertSame(s, Schedulers.getInstance());
    }

    /**
     * Test.
     */
    public void testClearActionScheduler() {

        // ARRANGE
        final ActionScheduler s =
            new ActionScheduler(
                Testing.dummy(Actions2.class), Testing.dummy(Sessions.class));
        Schedulers.setInstance(s);

        // ACT
        Schedulers.clearInstance();

        // ASSERT
        assertNull(Schedulers.getInstance());
    }

    /**
     * Test.
     */
    public void testClearActionSchedulerHandlesUnset() {

        // ARRANGE

        // ACT
        Schedulers.clearInstance();

        // ASSERT
        assertNull(Schedulers.getInstance());
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Schedulers.clearInstance();
    }


    /**
     * Test.
     */
    public void testPrivateConstructor() {

        // ARRANGE

        // ACT
        Testing.construct(Schedulers.class);

        // ASSERT

    }
}
