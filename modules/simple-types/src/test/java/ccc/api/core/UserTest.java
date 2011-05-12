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
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.api.types.Username;


/**
 * Tests for the {@link User} class.
 *
 * @author Civic Computing Ltd.
 */
public class UserTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testProperties() {

        // ARRANGE
        final String email = "abc@example.com";
        final Username uname = new Username("uname");
        final UUID id = UUID.randomUUID();
        final String name = "name";
        final String password = "pass";
        final Set<String> perms = Collections.singleton("perm");
        final Set<UUID> groups = Collections.singleton(UUID.randomUUID());
        final Map<String, String> metadata = Collections.singletonMap("a", "b");

        final User u = new User();

        // ACT
        u.setEmail(email);
        u.setId(id);
        u.setName(name);
        u.setPassword(password);
        u.setPermissions(perms);
        u.setGroups(groups);
        u.setMetadata(metadata);
        u.setUsername(uname);

        // ASSERT
        assertEquals(email, u.getEmail());
        assertEquals(id, u.getId());
        assertEquals("name", u.getName());
        assertEquals(password, u.getPassword());
        assertEquals(perms, u.getPermissions());
        assertTrue(u.hasPermission("perm"));
        assertEquals(groups, u.getGroups());
        assertEquals(metadata, u.getMetadata());
        assertEquals(uname, u.getUsername());
    }
}
