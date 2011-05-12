/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.api.types;

import junit.framework.TestCase;


/**
 * Tests for the {@link Duration} class.
 *
 * @author Civic Computing Ltd.
 */
public class DurationTest extends TestCase {

    private static final int SECS_IN_A_MINUTE = 60;
    private static final int SECS_IN_A_HOUR = 60*SECS_IN_A_MINUTE;
    private static final int SECS_IN_A_DAY = 24*SECS_IN_A_HOUR;
    private static final int ONE_SECOND = 1;
    private static final int TWO_SECONDS = 2;

    /**
     * Test.
     */
    public void testDurationEquality() {

        // ARRANGE
        final Duration d = new Duration(ONE_SECOND);

        // ACT

        // ASSERT
        assertTrue(d.equals(d));
        assertTrue(d.equals(new Duration(ONE_SECOND)));
        assertFalse(d.equals(new Duration(TWO_SECONDS)));
        assertFalse(d.equals(new Object()));
        assertFalse(d.equals(null));
    }


    /**
     * Test.
     */
    public void testTimeConstructor() {

        // ARRANGE
        final Duration duration = new Duration(ONE_SECOND);

        // ACT
        final long time = duration.time();

        // ASSERT
        assertEquals(ONE_SECOND, time);
    }

    /**
     * Test.
     */
    public void testFieldConstructor() {

        // ARRANGE
        final Duration duration = new Duration(3, 3, 3, 3);

        // ACT
        final long seconds = duration.secondField();
        final long minutes = duration.minuteField();
        final long hours = duration.hourField();
        final long days = duration.dayField();

        // ASSERT
        assertEquals(3, seconds);
        assertEquals(3, minutes);
        assertEquals(3, hours);
        assertEquals(3, days);
    }



    /**
     * Test.
     */
    public void testTimeFieldAccessor() {

        // ARRANGE
        // 33d 6h 15m 7s
        final Duration duration =
            new Duration(
                3*SECS_IN_A_DAY
                + 3*SECS_IN_A_HOUR
                + 3*SECS_IN_A_MINUTE
                + 3);

        // ACT
        final long seconds = duration.secondField();
        final long minutes = duration.minuteField();
        final long hours = duration.hourField();
        final long days = duration.dayField();

        // ASSERT
        assertEquals(3, seconds);
        assertEquals(3, minutes);
        assertEquals(3, hours);
        assertEquals(3, days);
    }

    /**
     * Test.
     */
    public void testOneDayTimeFieldAccessor() {

        // ARRANGE
        // 1d 0h 0m 0s
        final Duration duration = new Duration(SECS_IN_A_DAY);

        // ACT
        final long seconds = duration.secondField();
        final long minutes = duration.minuteField();
        final long hours = duration.hourField();
        final long days = duration.dayField();

        // ASSERT
        assertEquals(0, seconds);
        assertEquals(0, minutes);
        assertEquals(0, hours);
        assertEquals(1, days);
    }

    /**
     * Test.
     */
    public void testDefaultConstructor() {

        // ARRANGE
        // 0d 0h 0m 0s
        final Duration duration = new Duration();

        // ACT
        final long seconds = duration.secondField();
        final long minutes = duration.minuteField();
        final long hours = duration.hourField();
        final long days = duration.dayField();

        // ASSERT
        assertEquals(0, seconds);
        assertEquals(0, minutes);
        assertEquals(0, hours);
        assertEquals(0, days);
    }
}
