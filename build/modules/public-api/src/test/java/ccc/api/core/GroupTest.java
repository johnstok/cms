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
import java.util.Set;
import java.util.UUID;

import junit.framework.TestCase;


/**
 * Tests for the {@link Group} class.
 *
 * @author Civic Computing Ltd.
 */
public class GroupTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testProperties() {

        // ARRANGE
        final String name = "name";
        final UUID id = UUID.randomUUID();
        final Set<String> perms = Collections.singleton("perm");
        final Group g = new Group();

        // ACT
        g.setName(name);
        g.setId(id);
        g.setPermissions(perms);

        // ASSERT
        assertEquals(name, g.getName());
        assertEquals(id, g.getId());
        assertEquals(perms, g.getPermissions());
    }
}
