/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;

import junit.framework.TestCase;


/**
 * Tests for the {@link Duration} class.
 *
 * @author Civic Computing Ltd.
 */
public class DurationTest extends TestCase {


    /**
     * Test.
     */
    public void testTimeConstructor() {

        // ARRANGE
        final Duration duration = new Duration(1000);

        // ACT
        final long time = duration.time();

        // ASSERT
        assertEquals(1000, time);
    }

    /**
     * Test.
     */
    public void testFieldConstructor() {

        // ARRANGE
        final Duration duration = new Duration(4, 2, 7, 31);

        // ACT
        final long seconds = duration.secondField();
        final long minutes = duration.minuteField();
        final long hours = duration.hourField();
        final long days = duration.dayField();

        // ASSERT
        assertEquals(31, seconds);
        assertEquals(7, minutes);
        assertEquals(2, hours);
        assertEquals(4, days);
    }



    /**
     * Test.
     */
    public void testTimeFieldAccessor() {

        // ARRANGE
        // 33d 6h 15m 7s
        final Duration duration =
            new Duration(33*86400+6*3600+15*60+59);

        // ACT
        final long seconds = duration.secondField();
        final long minutes = duration.minuteField();
        final long hours = duration.hourField();
        final long days = duration.dayField();

        // ASSERT
        assertEquals(59, seconds);
        assertEquals(15, minutes);
        assertEquals(6, hours);
        assertEquals(33, days);
    }

    /**
     * Test.
     */
    public void testOneDayTimeFieldAccessor() {

        // ARRANGE
        // 1d 0h 0m 0s
        final Duration duration = new Duration(86400);

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
