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
package ccc.commons;

import java.io.Serializable;

import junit.framework.TestCase;
import ccc.api.types.NormalisingEncoder;
import ccc.api.types.Link.Encoder;


/**
 * Tests for the {@link Reflection} class.
 *
 * @author Civic Computing Ltd.
 */
public class ReflectionTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testIsClass() {

        // ARRANGE
        final Serializable s = new Serializable() { /* No Methods */ };

        // ACT
        final boolean isClass = Reflection.isClass("java.io.Serializable", s);

        // ASSERT
        assertTrue(isClass);
    }


    /**
     * Test.
     */
    public void testIsNotClass() {

        // ARRANGE
        final Serializable s = new Serializable() { /* No Methods */ };

        // ACT
        final boolean isClass = Reflection.isClass("java.lang.String", s);

        // ASSERT
        assertFalse(isClass);
    }


    /**
     * Test.
     */
    public void testIsNotMissingClass() {

        // ARRANGE
        final Serializable s = new Serializable() { /* No Methods */ };

        // ACT
        final boolean isClass = Reflection.isClass("doesn't.Exist", s);

        // ASSERT
        assertFalse(isClass);
    }


    /**
     * Test.
     */
    public void testCreateAndCast() {

        // ARRANGE

        // ACT
        final Encoder s =
            Reflection.construct(
                Encoder.class, "ccc.api.types.NormalisingEncoder");

        // ASSERT
        assertTrue(s instanceof NormalisingEncoder);
    }


    /**
     * Test.
     */
    public void testCreateAndCast2() {

        // ARRANGE

        // ACT
        final Encoder s =
            Reflection.construct(
                Encoder.class,
                "ccc.api.types.NormalisingEncoder",
                new Object[] {});

        // ASSERT
        assertTrue(s instanceof NormalisingEncoder);
    }


    /**
     * Test.
     */
    public void testCreateAndCast3() {

        // ARRANGE

        // ACT
        final Encoder s =
            Reflection.construct(
                Encoder.class,
                "ccc.api.types.NormalisingEncoder",
                new Class[] {},
                new Object[] {});

        // ASSERT
        assertTrue(s instanceof NormalisingEncoder);
    }


    /**
     * Test.
     */
    public void testInvoke() {

        // ARRANGE
        final Object o = new Object();

        // ACT
        final Object result =
            Reflection.invoke(
                String.class.getName(),
                "valueOf",
                null,
                new Object[] {o});

        // ASSERT
        assertEquals(o.toString(), result);
    }


    /**
     * Test.
     */
    public void testInvokeWithClass() {

        // ARRANGE

        // ACT
        final Object result =
            Reflection.invoke(
                String.class.getName(),
                "valueOf",
                null,
                new Object[] {Integer.valueOf(0)},
                new Class<?>[] {int.class});

        // ASSERT
        assertEquals("0", result);
    }


    /**
     * Test.
     */
    public void testPrivateConstructor() {

        // ARRANGE

        // ACT
        Testing.construct(Reflection.class);

        // ASSERT

    }
}
