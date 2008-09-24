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

package ccc.commons;

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
}
