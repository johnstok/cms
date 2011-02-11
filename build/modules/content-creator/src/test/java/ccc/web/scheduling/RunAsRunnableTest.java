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

import static org.mockito.Mockito.*;
import junit.framework.TestCase;
import ccc.commons.Exceptions;
import ccc.plugins.security.Sessions;


/**
 * Tests for the {@link RunAsRunnable} class.
 *
 * @author Civic Computing Ltd.
 */
public class RunAsRunnableTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRolePushedAndPoppedWhenDelegateSucceeds() {

        // EXPECT
        final Runnable delegate = mock(Runnable.class);
        final Sessions session = mock(Sessions.class);

        // ARRANGE
        final Runnable runAs = new RunAsRunnable(session, "foo", delegate);

        // ACT
        runAs.run();

        // ASSERT
        verify(session).pushRunAsRole("foo");
        verify(session).popRunAsRole();

    }


    /**
     * Test.
     */
    public void testRolePushedAndPoppedWhenDelegateFails() {

        // EXPECT
        final Runnable delegate = mock(Runnable.class);
        doThrow(new RuntimeException()).when(delegate).run();
        final Sessions session = mock(Sessions.class);

        // ARRANGE
        final Runnable runAs = new RunAsRunnable(session, "foo", delegate);

        // ACT
        try {
            runAs.run();
        } catch (final RuntimeException e) {
            Exceptions.swallow(e);
        }

        // ASSERT
        verify(session).pushRunAsRole("foo");
        verify(session).popRunAsRole();

    }
}
