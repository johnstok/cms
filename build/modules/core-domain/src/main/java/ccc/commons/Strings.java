/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.commons;

/**
 * Helper methods for strings.
 *
 * @author Civic Computing Ltd.
 */
public final class Strings {

    private Strings() { super(); }

    /**
     * NVL implementation.
     *
     * @param original The original string.
     * @param replacement The potential replacement.
     * @return If original is null return the replacement, otherwise return
     *     the original.
     */
    public static String nvl(final String original, final String replacement) {
        return (null != original) ? original : replacement;
    }

    /**
     * Remove a trailing character from a string.
     *
     * @param c The character to remove.
     * @param foo The string from which to remove.
     * @return The string minus the char.
     */
    public static String removeTrailing(final char c, final String foo) {
        return
            (foo.endsWith("" + c))
            ? foo.substring(0, foo.length() - 1)
            : foo;
    }
}
