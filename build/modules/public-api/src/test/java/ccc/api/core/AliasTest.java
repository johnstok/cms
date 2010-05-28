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

import java.util.UUID;

import junit.framework.TestCase;
import ccc.api.types.ResourceName;


/**
 * Tests for the {@link Alias} class.
 *
 * @author Civic Computing Ltd.
 */
public class AliasTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testProperties() {

        // ARRANGE
        final UUID targetId = UUID.randomUUID();
        final String targetPath = "/foo";
        final Alias a = new Alias();

        // ACT
        a.setTargetId(targetId);
        a.setTargetPath(targetPath);

        // ASSERT
        assertEquals(targetId, a.getTargetId());
        assertEquals(targetPath, a.getTargetPath());
    }

    /**
     * Test.
     */
    public void testLegacyConstructor1() {

        // ARRANGE
        final UUID targetId = UUID.randomUUID();

        // ACT
        final Alias a = new Alias(targetId);

        // ASSERT
        assertEquals(targetId, a.getTargetId());
        assertNull(a.getTargetPath());
    }

    /**
     * Test.
     */
    public void testLegacyConstructor2() {

        // ARRANGE
        final UUID targetId = UUID.randomUUID();
        final ResourceName name = new ResourceName("foo");

        // ACT
        final Alias a = new Alias(null, name, targetId);

        // ASSERT
        assertEquals(targetId, a.getTargetId());
        assertNull(a.getTargetPath());
        assertEquals(name, a.getName());
    }
}
