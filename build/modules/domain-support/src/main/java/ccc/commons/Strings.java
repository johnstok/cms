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

import java.io.UnsupportedEncodingException;

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

    public static String hex(final char c,
                             final String enc)
                                           throws UnsupportedEncodingException {
        final StringBuilder buffer = new StringBuilder();
        final byte[] bytes = Character.toString(c).getBytes(enc);
        for (final byte b : bytes) {
            buffer.append("%");
            buffer.append(String.format("%02x", b));
        }
        return buffer.toString();
    }
}
