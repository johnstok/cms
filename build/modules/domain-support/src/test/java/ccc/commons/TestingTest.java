/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import junit.framework.TestCase;


/**
 * Tests for the {@link Testing} class.
 *
 * @author Civic Computing Ltd.
 */
public class TestingTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testDummyString() {

        // ARRANGE
        final int length = 257;

        // ACT
        final String actual = Testing.dummyString('a', length);

        // ASSERT
        assertEquals(length, actual.length());
        assertTrue("String contains invalid characters.", actual.matches("a*"));
    }


    /**
     * Test.
     */
    public void testDummyAnInterface() {

        // ARRANGE

        // ACT
        final Object o = Testing.dummy(Runnable.class);

        // ASSERT
        assertTrue(o instanceof Runnable);
    }


    /**
     * Test.
     */
    public void testCallingDummyMethodsCausesException() {

        // ARRANGE
        final Runnable r = Testing.dummy(Runnable.class);

        // ACT
        try {
            r.run();
            fail();

        // ASSERT
        } catch (final UnsupportedOperationException e) {

        }
    }


    /**
     * Test.
     */
    public void testStubAnInterface() {

        // ARRANGE

        // ACT
        final Object o = Testing.stub(Runnable.class);

        // ASSERT
        assertTrue(o instanceof Runnable);
    }


    /**
     * Test.
     */
    public void testCallingStubMethodsReturnVoid() {

        // ARRANGE
        final Runnable r = Testing.stub(Runnable.class);

        // ACT
        r.run();
    }


    /**
     * Test.
     */
    public void testPrivateConstrutor() {

        // ARRANGE

        // ACT
        Testing.construct(Testing.class);

        // ASSERT

    }
}
