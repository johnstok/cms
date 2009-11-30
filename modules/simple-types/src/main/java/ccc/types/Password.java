/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;


/**
 * A password value type.
 *
 * @author Civic Computing Ltd.
 */
public final class Password {

    private Password() { /* NO OP */ }

    private static final String STRONG_PW =
        "^(?=.{10,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$";

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
}
