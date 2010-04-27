/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import junit.framework.TestCase;
import ccc.api.core.Alias;
import ccc.api.exceptions.CycleDetectedException;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.commons.Exceptions;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.json.JsonImpl;


/**
 * Tests for the {@link AliasEntity} class.
 *
 * @author Civic Computing Ltd.
 */
public class AliasTest extends TestCase {

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testConvertToJson() throws CycleDetectedException {

        // ARRANGE
        final Json json = new JsonImpl();
        final AliasEntity a = new AliasEntity();
        final PageEntity p = new PageEntity();
        a.target(p);

        // ACT
        a.toJson(json);

        // ASSERT
        assertEquals(p.getId(), json.getId(JsonKeys.TARGET_ID));
    }


    /**
     * Test.
     */
    public void testConvertToJsonWithMissingTarget() {

        // ARRANGE
        final Json json = new JsonImpl();
        final AliasEntity a = new AliasEntity();

        // ACT
        a.toJson(json);

        // ASSERT
        assertNull(json.getId(JsonKeys.TARGET_ID));
    }

    /**
     * Test.
     */
    public void testSelfCircularDependency() {

        // ARRANGE
        final AliasEntity a = new AliasEntity();

        // ACT
        try {
            a.target(a);
            fail();

        // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testDirectCircularDependency() throws CycleDetectedException {

        // ARRANGE
        final AliasEntity a = new AliasEntity();
        final AliasEntity b = new AliasEntity();
        a.target(b);

        // ACT
        try {
            b.target(a);
            fail();

        // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testIndirectCircularDependency() throws CycleDetectedException {

        // ARRANGE
        final AliasEntity a = new AliasEntity();
        final AliasEntity b = new AliasEntity();
        final AliasEntity c = new AliasEntity();
        a.target(b);
        b.target(c);

        // ACT
        try {
            c.target(a);
            fail();

        // ASSERT
        } catch (final CycleDetectedException e) {
            Exceptions.swallow(e);
        }
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testSnapshot() throws CycleDetectedException {

        // ARRANGE
        final ResourceEntity p = new FolderEntity("foo");
        final AliasEntity alias = new AliasEntity("bar", p);

        // ACT
        final Alias o = alias.createSnapshot();

        // ASSERT
        assertEquals(p.getId(), o.getTargetId());
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testCreateAliasWithTitle() throws CycleDetectedException {

        // ARRANGE
        final PageEntity p = new PageEntity();

        // ACT
        final AliasEntity alias = new AliasEntity("bar", p);

        // ASSERT
        assertEquals(p, alias.target());
        assertEquals("bar", alias.getTitle());
        assertEquals(new ResourceName("bar"), alias.getName());
    }

    /**
     * Test.
     */
    public void testTypeReturnsAlias() {

        // ACT
        final ResourceType t = new AliasEntity().getType();

        // ASSERT
        assertEquals(ResourceType.ALIAS, t);
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testCreateAliasWithName() throws CycleDetectedException {

        // ARRANGE
        final PageEntity p = new PageEntity();

        // ACT
        final AliasEntity alias = new AliasEntity("bar", p);

        // ASSERT
        assertEquals(p, alias.target());
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testNullTargetIsRejected() throws CycleDetectedException {

        // ACT
        try {
            new AliasEntity("foo", null);
            fail("Null should be rejected");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }

    }
}
