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

import java.util.TimerTask;

import junit.framework.TestCase;
import ccc.api.core.SearchEngine2;
import ccc.commons.Testing;
import ccc.plugins.security.Sessions;


/**
 * Tests for the {@link SearchTask} class.
 *
 * @author Civic Computing Ltd.
 */
public class SearchReindexTaskTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testRun() {

        // ARRANGE
        final SearchEngine2 search = mock(SearchEngine2.class);
        final TimerTask task =
            new SearchTask(search, Testing.stub(Sessions.class));

        // ACT
        task.run();

        // ASSERT
        verify(search).index();
    }

    /**
     * Test.
     */
    public void testRunHandlesExceptions() {

        // EXPECT
        final SearchEngine2 search = mock(SearchEngine2.class);
        doThrow(new RuntimeException()).when(search).index();

        // ARRANGE
        final TimerTask task =
            new SearchTask(search, Testing.stub(Sessions.class));

        // ACT
        task.run();

        // ASSERT
        verify(search).index();
    }
}
