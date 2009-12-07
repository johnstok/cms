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
            new Setting(Name.DATABASE_VERSION, null);
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
            new Setting(Name.DATABASE_VERSION, null);
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
        final Name name = Name.DATABASE_VERSION;
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
        final Name name = Name.DATABASE_VERSION;
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
        final Setting s = new Setting(Name.DATABASE_VERSION, value);

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
