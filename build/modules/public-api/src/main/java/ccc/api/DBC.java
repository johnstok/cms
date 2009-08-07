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

package ccc.api;


/**
 * A helper class to support design-by-contract (DBC).
 *
 * @author Civic Computing Ltd
 */
public final class DBC {

    private DBC() { /* NO-OP */ }

    /**
     * Factory method.
     *
     * @return An instance of DBC.
     */
    public static DBC require() {

        return new DBC();
    }

    /**
     * Factory method.
     *
     * @return An instance of DBC.
     */
    public static DBC ensure() {

        return new DBC();
    }

    /**
     * Assert that the specified value is not null.
     *
     * @param object The object to test for NULL.
     */
    public void notNull(final Object object) {
        if (null==object) {
            throw new IllegalArgumentException(
                "Specified value may not be NULL."); //$NON-NLS-1$
        }
    }

    /**
     * Assert that the specified string is not empty. A string is considered
     * empty if either 1. It is NULL or 2. It has a length of 0.
     *
     * TODO: Rename to minLength and remove notNull test?
     *
     * @param string The string that may not be empty.
     */
    public void notEmpty(final String string) {
        notNull(string);
        if (string.trim().length() < 1) {
            throw new IllegalArgumentException(
                "Specified string must have length > 0."); //$NON-NLS-1$
        }
    }

    /**
     * Assert that the specified condition is false.
     *
     * @param condition The condition to check.
     */
    public void toBeFalse(final boolean condition) {
        if (condition) {
            throw new IllegalArgumentException(
                "Specified expression must be false.");
        }
    }

    /**
     * Assert that the specified condition is true.
     *
     * @param condition The condition to check.
     */
    public void toBeTrue(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException(
                "Specified expression must be true.");
        }
    }

    /**
     * Assert that the specified string is not longer than the specified length.
     *
     * @param stringToTest The string to test.
     * @param maximumLength The length it should not exceed.
     */
    public void maxLength(final String stringToTest, final int maximumLength) {
        if (stringToTest.length() > maximumLength) {
            throw new IllegalArgumentException(
                "Specified string exceeds max length of "+maximumLength+".");
        }
    }

    /**
     * Assert that the specified string contains at least the specified number
     * of characters.
     *
     * @param stringToTest The string to test.
     * @param minimumLength The minimal acceptable length.
     */
    public void minLength(final String stringToTest, final int minimumLength) {
        if (stringToTest.length() < minimumLength) {
            throw new IllegalArgumentException(
                "Specified string must have a min length of "
                +minimumLength+".");
        }
    }

    /**
     * Assert that the specified string matches with given regular expression.
     *
     * @param regex The regular expression.
     * @param stringToTest The string to test.
     */
    public void toMatch(final String regex, final String stringToTest) {
        if (!stringToTest.matches(regex)) {
            throw new IllegalArgumentException(
                "Specified string ("+stringToTest+") does not match "+regex);
        }
    }

    /**
     * Assert that the specified object is null.
     *
     * @param object The object to test.
     */
    public void toBeNull(final Object object) {
        if (null!=object) {
            throw new IllegalArgumentException(
                "Specified value must be NULL."); //$NON-NLS-1$
        }
    }

    /**
     * Assert that the specified string does not contain bracket < > characters.
     *
     * @param stringToTest The string to test.
     */
    public void containsNoBrackets(final String stringToTest) {
        if (stringToTest != null && !stringToTest.matches("[^<^>]*")) {
            throw new IllegalArgumentException(
            "String must not contain brackets.");
        }
    }


    /**
     * Assert that the specified value is less than the criteria.
     *
     * @param valueToTest The value to test.
     * @param maximum The maximum acceptable value.
     */
    public void maxValue(final long valueToTest, final long maximum) {
        if (valueToTest > maximum) {
            throw new IllegalArgumentException(
                "Specified value must be under "
                +maximum+".");
        }
    }


    /**
     * Assert that the specified value is greater than the criteria.
     *
     * @param criteria The lower bound.
     * @param value    The value to test.
     */
    public void greaterThan(final int criteria, final int value) {
        if (value<=criteria) {
            throw new IllegalArgumentException(
                "Specified value must be greater than "
                +criteria+".");
        }
    }


//    /**
//     * Assert that the specified decimal has a scale less than or equal to the
//     * specified scale.
//     *
//     * @param bd The big decimal to test.
//     * @param scale The maximum scale allowed.
//     */
//    public void maxScale(final BigDecimal bd, final int scale) {
//        try {
//            bd.setScale(scale);
//        } catch (final ArithmeticException e) {
//            throw new IllegalArgumentException("Scale is too large.");
//        }
//    }
//
//    /**
//     * Assert that the specified decimal has a precision less than or equal to
//     * the  specified precision.
//     *
//     * @param bd The big decimal to test.
//     * @param precision The maximum precision allowed
//     */
//    public void maxPrecision(final BigDecimal bd, final int precision) {
//        if (bd.precision()>precision) {
//            throw new IllegalArgumentException("Precision is too large.");
//        }
//    }
}
