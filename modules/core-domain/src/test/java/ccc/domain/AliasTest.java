/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
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
import ccc.api.AliasDelta;
import ccc.api.ID;
import ccc.api.ResourceType;
import ccc.commons.Exceptions;
import ccc.entities.ResourceName;


/**
 * Tests for the {@link Alias} class.
 *
 * @author Civic Computing Ltd.
 */
public class AliasTest extends TestCase {

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
        final DummyResource p = new DummyResource("foo");
        final Alias alias = new Alias("bar", p);

        // ACT
        final AliasDelta o = alias.createSnapshot();

        // ASSERT
        assertEquals("foo", o.getTargetName());
        assertEquals(new ID(p.id().toString()), o.getTargetId());
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testCreateAliasWithTitle() throws CycleDetectedException {

        // ARRANGE
        final DummyResource p = new DummyResource("foo");

        // ACT
        final Alias alias = new Alias("bar", p);

        // ASSERT
        assertEquals(p, alias.target());
        assertEquals("bar", alias.title());
        assertEquals(new ResourceName("bar"), alias.name());
    }

    /**
     * Test.
     */
    public void testTypeReturnsAlias() {

        // ACT
        final ResourceType t = new Alias().type();

        // ASSERT
        assertEquals(ResourceType.ALIAS, t);
    }

    /**
     * Test.
     * @throws CycleDetectedException If the test fails.
     */
    public void testCreateAliasWithName() throws CycleDetectedException {

        // ARRANGE
        final DummyResource p = new DummyResource("foo");

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
