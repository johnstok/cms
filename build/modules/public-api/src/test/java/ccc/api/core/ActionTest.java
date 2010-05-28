/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.api.core;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.api.types.CommandType;


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
    public void testProperties() {

        // ARRANGE
        final UUID resourceId = UUID.randomUUID();
        final CommandType command = CommandType.RESOURCE_PUBLISH;
        final Date executeAfter = new Date();
        final Map<String, String> params = Collections.singletonMap("a", "b");

        // ACT
        final Action a = new Action(resourceId, command, executeAfter, params);

        // ASSERT
        assertEquals(resourceId, a.getResourceId());
        assertEquals(command, a.getCommand());
        assertEquals(executeAfter, a.getExecuteAfter());
        assertEquals(params, a.getParameters());
    }


    /**
     * Test.
     */
    public void testNoArgsConstructor() {

        // ARRANGE
        final Map<String, String> params = Collections.emptyMap();

        // ACT
        final Action a = new Action();

        // ASSERT
        assertNull(a.getResourceId());
        assertNull(a.getCommand());
        assertNull(a.getExecuteAfter());
        assertEquals(params, a.getParameters());
    }
}
