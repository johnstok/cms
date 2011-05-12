/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.api.types;


/**
 * A password value type.
 *
 * @author Civic Computing Ltd.
 */
public final class Password {

    private Password() { /* NO OP */ }

    private static final String STRONG_PW =
        "^(?=.{10,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$";

    private static final String VALID_CHARACTERS = "\\S*";

    /**
     * Test that a string is strong enough to be used as a password.
     *
     * @param pw The string to test.
     *
     * @return True if the string is strong enough, false otherwise.
     */
    public static boolean isStrong(final String pw) {
        return pw.matches(STRONG_PW);
    }

    /**
     * Test that a string has valid characters.
     *
     * @param pw The string to test.
     *
     * @return True if the string contains only valid chars, false otherwise.
     */
    public static boolean hasOnlyValidChars(final String pw) {
        return pw.matches(VALID_CHARACTERS);
    }
}
