/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.HashSet;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;



/**
 * Tests for the action scheduler.
 *
 * @author Civic Computing Ltd.
 */
public class SearchEngineAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     */
    public void testFind() {

        // ARRANGE
        final String searchTerm = "veryunlikelysearchterm"+uid();
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary page   = tempPage(parent.getId(), null);

        final Resource metadata = new Resource();
        metadata.setTitle(searchTerm);
        metadata.setDescription(searchTerm);
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);
        getCommands().publish(page.getId());

        getSearch().index();

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 0);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testStartStopActionScheduler() {

        // ARRANGE

        // ACT
        final boolean startedAtFirst = getSearch().isRunning();

        getSearch().start();
        final boolean startedAfterStart = getSearch().isRunning();

        getSearch().stop();
        final boolean startedAfterStop = getSearch().isRunning();

        // ASSERT
        assertFalse(startedAtFirst);
        assertTrue(startedAfterStart);
        assertFalse(startedAfterStop);
    }


    /**
     * Test.
     */
    public void testStartSchedulerIsIdempotent() {

        // ARRANGE

        // ACT
        try {
            getSearch().start();
            getSearch().start();
            getSearch().start();
        } finally {
            getSearch().stop();
        }

        // ASSERT
    }
}
