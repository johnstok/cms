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
import ccc.commons.Exceptions;
import ccc.rest.dto.AliasDelta;
import ccc.rest.exceptions.CycleDetectedException;
import ccc.serialization.Json;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


/**
 * Tests for the {@link Alias} class.
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
        final Alias a = new Alias();
        final Page p = new Page();
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
        final Alias a = new Alias();

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
        final Alias a = new Alias();

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
        final Alias a = new Alias();
        final Alias b = new Alias();
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
        final Alias a = new Alias();
        final Alias b = new Alias();
        final Alias c = new Alias();
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
        final Resource p = new Folder("foo");
        final Alias alias = new Alias("bar", p);

        // ACT
        final AliasDelta o = alias.createSnapshot();

        // ASSERT
        assertEquals(p.getId(), o.getTargetId());
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testCreateAliasWithTitle() throws CycleDetectedException {

        // ARRANGE
        final Page p = new Page();

        // ACT
        final Alias alias = new Alias("bar", p);

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
        final ResourceType t = new Alias().getType();

        // ASSERT
        assertEquals(ResourceType.ALIAS, t);
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testCreateAliasWithName() throws CycleDetectedException {

        // ARRANGE
        final Page p = new Page();

        // ACT
        final Alias alias = new Alias("bar", p);

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
            new Alias("foo", null);
            fail("Null should be rejected");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }

    }
}
