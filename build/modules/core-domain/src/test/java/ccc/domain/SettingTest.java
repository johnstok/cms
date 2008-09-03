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
import ccc.domain.Setting.Name;


/**
 * Tests for the {@link Setting} class.
 *
 * @author Civic Computing Ltd
 */
public final class SettingTest extends TestCase {

    /**
     * Test.
     */
    public void testConstructorRejectsEmptyName() {

        // ARRANGE

        // ACT
        try {
            new Setting(Name.CONTENT_ROOT_FOLDER_ID, null);
            fail("Empty name should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified value may not be NULL.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testConstructorRejectsNullName() {

        // ARRANGE

        // ACT
        try {
            new Setting(null, null);
            fail("NULL name should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified value may not be NULL.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testConstructorRejectsNullValue() {

        // ARRANGE

        // ACT
        try {
            new Setting(Name.CONTENT_ROOT_FOLDER_ID, null);
            fail("NULL value should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified value may not be NULL.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testAccessors() {

        // ARRANGE
        final Name name = Name.CONTENT_ROOT_FOLDER_ID;
        final String value = "bar";

        // ACT
        final Setting s = new Setting(name, value);

        // ASSERT
        assertEquals(name, s.name());
        assertEquals(value, s.value());
    }

    /**
     * Test.
     */
    public void testValueMutator() {

        // ARRANGE
        final Name name = Name.CONTENT_ROOT_FOLDER_ID;
        final String value = "bar";
        final Setting s = new Setting(name, value);

        // ACT
        s.value("baz");

        // ASSERT
        assertEquals(name, s.name());
        assertEquals("baz", s.value());
    }

    /**
     * Test.
     */
    public void testValueMutatorRejectsNull() {

        // ARRANGE
        final String value = "bar";
        final Setting s = new Setting(Name.CONTENT_ROOT_FOLDER_ID, value);

        // ACT
        try {
            s.value(null);
            fail("NULL value should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals(
                "Specified value may not be NULL.",
                e.getMessage());
        }
    }
}
