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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Strings {

    /**
     * TODO: Add a description of this method.
     *
     * @param original
     * @param replacement
     * @return
     */
    public static String nvl(final String original, final String replacement) {
        return (null != original) ? original : replacement;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param foo
     * @return
     */
    public static String removeTrailing(final char c, final String foo) {
        return
            (foo.endsWith("" + c))
            ? foo.substring(0, foo.length() - 1)
            : foo;
    }
}
