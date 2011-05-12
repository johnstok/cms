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
package ccc.cli;

import java.math.BigDecimal;

import junit.framework.TestCase;


/**
 * A series of tests for the string to decimal converter
 *
 * @author Civic Computing Ltd.
 */
public class StringToDecConverterTest extends TestCase {

    private StringToDecConverterUtil _stdc;

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _stdc = new StringToDecConverterUtil();
/*        _stdc.setUsername("panos");
        _stdc.setPassword("panos");
        _stdc.setConnectionString(
            "jdbc:mysql://helios:3306/panos?characterEncoding=UTF-8"
        );*/
    }

    /**
     * Test.
     */
    public void testLargerPrecision() {
        assertEquals(null, _stdc.convert("1234892830291220"));
    }

    /**
     * Test.
     */
    public void testOrdinary() {
        assertEquals(new BigDecimal("1234567890123.212345"),
            _stdc.convert("1234567890123.212345"));
    }

    /**
     * Test.
     */
    public void testOnlyDecimal() {
        assertEquals(new BigDecimal("0.123456"), _stdc.convert("0.12345678"));
    }

    /**
     * Test.
     */
    public void testNegativeDecimal() {
        assertEquals(new BigDecimal("-12345678.123"),
            _stdc.convert("-12345678.123"));
    }

    /**
     * Test.
     */
    public void testCommas() {
        final long l = 1234567890123L;
        assertEquals(new BigDecimal(l),
            _stdc.convert("1,234,567,890,123"));
    }

    /**
     * Test.
     */
    public void testCommasWrongPlaces() {
        assertEquals(new BigDecimal("12312323.321"),
            _stdc.convert("1,23123,23.321"));
    }

    /**
     * Test.
     */
    public void testNonNumeric() {
        assertEquals(null, _stdc.convert("okyrantonis"));
    }

    /**
     * Test.
     */
    public void testMaxWithCommas() {
        assertEquals(new BigDecimal("9999999999999.999999"),
            _stdc.convert("9,999,999,999,999.999999"));
    }

    /**
     * Test.
     */
    public void testMinimum() {
        assertEquals(new BigDecimal("0000000000000.000001"),
            _stdc.convert("0000000000000.000001"));
    }

    /**
     * Test.
     */
    public void testNegativeMaxWithCommas() {
        assertEquals(new BigDecimal("-9999999999999.999999"),
            _stdc.convert("-9,999,999,999,999.999999"));
    }
    /**
     * Test.
     */
    public void testNegativeMinimum() {
        assertEquals(new BigDecimal("-0000000000000.000001"),
            _stdc.convert("-0000000000000.000001"));
    }

    /**
     * Test.
     */
    public void testSimpleScientificNot() {
        final long testlong = 90000000000L;
        assertEquals(testlong, _stdc.convert("9e10").longValue());
    }

    /**
     * Test.
     */
    public void testScientificDecimal() {
        final long testlong = 99990000000L;
        assertEquals(testlong, _stdc.convert("9.999E10").longValue());
    }

    /**
     * Test.
     */
    public void testScientificWithComma() {
        final long testlong = 1234200000L;
        assertEquals(testlong, _stdc.convert("12,342E5").longValue());
    }

    /**
     * Test.
     */
    public void testNegativeScientific() {
        final long testlong = -13123000;
        assertEquals(testlong, _stdc.convert("-13.123E6").longValue());
    }

    /**
     * Test.
     */
    public void testNegativeFloatScientificWithNegativeExp() {
        assertEquals(new BigDecimal("-0.000001"), _stdc.convert("-13.123E-7"));
    }

    /**
     * Test.
     */
    public void testScientificLargerPrecision() {
        assertEquals(null, _stdc.convert("999231E10"));
    }

    /**
     * Test.
     */
    public void testEmpty() {
        assertEquals(null, _stdc.convert(""));
    }

    /**
     * Test.
     */
    public void testNull() {
        assertEquals(null, _stdc.convert(null));
    }

    /**
     * Test.
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        final StringToDecConverterTest stdct = new StringToDecConverterTest();
        stdct.setUp();
    }
}
