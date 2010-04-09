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

package ccc.api.types;

import static ccc.api.types.DBC.*;
import junit.framework.TestCase;


/**
 * Tests for the DBC class.
 *
 * @author Civic Computing Ltd
 */
public final class DBCTest extends TestCase {

    /**
     * Test.
     */
    public void testToBeNull() {

        // ARRANGE

        // ACT
        require().toBeNull(null);
        try {
            require().toBeNull(new Object());
            fail();

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value must be NULL.", e.getMessage());
        }

    }

    /**
     * Test.
     */
    public void testMinLength() {

        // ACT
        require().minLength("a", 1);
        require().minLength("aa", 2);
        require().minLength(" a", 2);
        require().minLength("a ", 2);
        require().minLength("  ", 2);
        require().minLength("", 0);
        require().minLength(" ", 1);

        try {
            require().minLength("a", 2);
            fail("String should be rejected - too short.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have a min length of 2.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testMaxLength() {

        // ACT
        require().maxLength("a", 1);
        require().maxLength("aa", 2);
        require().maxLength(" a", 2);
        require().maxLength("a ", 2);
        require().maxLength("  ", 2);
        require().maxLength("", 0);
        require().maxLength(" ", 1);
        require().maxLength("", Integer.MAX_VALUE);

        try {
            require().maxLength("aa", 1);
            fail("String should be rejected - too long.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string exceeds max length of 1.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testToBeFalse() {

        // ACT
        require().toBeFalse(false);

        try {
            require().toBeFalse(true);
            fail("True conditions should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified expression must be false.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testToBeTrue() {

        // ACT
        require().toBeTrue(true);

        try {
            require().toBeTrue(false);
            fail("False conditions should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified expression must be true.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRequireNotNull() {

        // ACT
        require().notNull(new Object());

        try {
            require().notNull(null);
            fail("NULL should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRequireNotEmpty() {

        // ACT
        require().notEmpty("foo");

        try {
            require().notEmpty(null);
            fail("NULL should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }

        try {
            require().notEmpty("");
            fail("Zero length string should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }

        try {
            require().notEmpty("   ");
            fail("A string with only spaces should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified string must have length > 0.",
                e.getMessage());
        }
    }
    /**
     * Test.
     */
    public void testRequireNoBrackets() {

        // ACT
        require().containsNoBrackets("ok string");

        try {
            require().containsNoBrackets("no good < string >");
            fail("String containing brackets should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "String must not contain brackets: no good < string >",
                e.getMessage());
        }

        try {
            require().containsNoBrackets("< Bad string");
            fail("String containing brackets should be rejected.");
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "String must not contain brackets: < Bad string",
                e.getMessage());
        }

    }


    /**
     * Test.
     */
    public void testMaxValue() {

        // ARRANGE
        final long valueToTest = 9;
        final long maximum = 5;

        // ACT
        try {
            require().maxValue(valueToTest, maximum);
            fail("Value should not be longer than maxValue");

        // ASSSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value must be under "
                +maximum+".", e.getMessage());
        }
    }


    /**
     * Test.
     */
    public void testGreaterThan() {

        // ARRANGE
        final int value = 0;
        final int criteria = 5;

        // ACT
        try {
            require().greaterThan(criteria, value);

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value must be greater than "
                +criteria+".", e.getMessage());
        }
    }
}
