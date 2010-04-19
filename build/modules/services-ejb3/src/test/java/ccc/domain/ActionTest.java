/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import junit.framework.TestCase;
import ccc.api.exceptions.CCException;
import ccc.api.exceptions.UnlockedException;
import ccc.api.types.ActionStatus;
import ccc.api.types.FailureCode;


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
        assertEquals(ActionStatus.CANCELLED, a.getStatus());
    }

    /**
     * Test.
     */
    public void testFailAnAction() {

        // ARRANGE
        final Page p = new Page();
        final Action a = new Action();
        final CCException e = new UnlockedException(p.getId());

        // ACT
        a.fail(e.getFailure());

        // ASSERT
        assertEquals(ActionStatus.FAILED, a.getStatus());
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
        assertEquals(ActionStatus.SCHEDULED, a.getStatus());
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
        assertEquals(ActionStatus.COMPLETE, a.getStatus());
    }
}
