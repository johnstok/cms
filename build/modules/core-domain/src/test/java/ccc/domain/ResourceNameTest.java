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

package ccc.domain;

import static ccc.domain.ResourceName.*;
import junit.framework.TestCase;

/**
 * Tests for the {@link ResourceName} class.
 *
 * TODO: Test max length of 256 chars.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceNameTest extends TestCase {

    private static final String LOWERCASE_ALPHABET =
        "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_ALPHABET =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS             =
        "0123456789";
    private static final String UNDERSCORE         =
        "_";
    private static final String PERIOD         =
        ".";
    private static final String WORD_CHARACTERS    =
        LOWERCASE_ALPHABET + UPPERCASE_ALPHABET + DIGITS + UNDERSCORE;

    /**
     * Test.
     */
    public void testWordCharactersAreAccepted() {

        // ACT
        new ResourceName(LOWERCASE_ALPHABET);
        new ResourceName(UPPERCASE_ALPHABET);
        new ResourceName(DIGITS);
        new ResourceName(UNDERSCORE);
        new ResourceName(WORD_CHARACTERS);
        new ResourceName(PERIOD);
    }

    /**
     * Test.
     */
    public void testZeroLengthStringIsRejected() {

        // ARRANGE
        final String zeroLength = "";

        // ACT
        try {
            new ResourceName(zeroLength);
            fail("ResourceName failed to reject a zero length string.");
        } catch (final RuntimeException e) {

            // ASSERT
            assertEquals(
                "A resource name must be longer than zero characters.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testNullIsRejected() {

        // ACT
        try {
            new ResourceName(null);
            fail("ResourceName failed to reject a NULL string.");
        } catch (final RuntimeException e) {

            // ASSERT
            assertEquals("A resource name may not be NULL.", e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testToStringReturnsStringRepresentation() {

        // ARRANGE
        final ResourceName name = new ResourceName(WORD_CHARACTERS);

        // ACT
        final String stringRepresentation = name.toString();

        // ASSERT
        assertEquals(WORD_CHARACTERS, stringRepresentation);
    }

    /**
     * Test.
     */
    public void testSpaceIsRejected() {

        // ARRANGE
        final String whitespace = " ";

        // ACT
        try {
            new ResourceName(whitespace);
            fail("ResourceName failed to reject whitespace.");
        } catch (final RuntimeException e) {

            // ASSERT
            assertEquals(
                "  does not match the java.util.regex pattern '"
                    + VALID_CHARACTERS+"'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testTildeIsRejected() {

        // ARRANGE
        final String tilde = "~";

        // ACT
        try {
            new ResourceName(tilde);
            fail("ResourceName failed to reject tilde.");
        } catch (final RuntimeException e) {

            // ASSERT
            assertEquals(
                "~ does not match the java.util.regex pattern '"
                    + VALID_CHARACTERS+"'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testDashIsRejected() {

        // ARRANGE
        final String dash = "-";

        // ACT
        try {
            new ResourceName(dash);
            fail("ResourceName failed to reject dash.");
        } catch (final RuntimeException e) {

            // ASSERT
            assertEquals(
                "- does not match the java.util.regex pattern '"
                    + VALID_CHARACTERS+"'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testBackslashIsRejected() {

        // ARRANGE
        final String backslash = "\\";

        // ACT
        try {
            new ResourceName(backslash);
            fail("ResourceName failed to reject backslash.");
        } catch (final RuntimeException e) {

            // ASSERT
            assertEquals(
                "\\ does not match the java.util.regex pattern '"
                    + VALID_CHARACTERS+"'.",
                e.getMessage());
        }
    }

    /**
     * Test.
     */
    public void testRfc3986ReservedCharactersAreRejected() {

        // ARRANGE
        final String[] reservedChars =
            new String[] {"!", "*", "'", "(", ")", ";", ":", "@", "&", "=",
                          "+", "$", ",", "/", "?", "%", "#", "[", "]"
                         };

        for (final String reservedChar : reservedChars) {
            // ACT
            try {
                new ResourceName(reservedChar);
                fail("ResourceName failed to reject reserved.");
            } catch (final RuntimeException e) {

                // ASSERT
                assertEquals(
                    reservedChar
                        + " does not match the java.util.regex pattern '"
                    + VALID_CHARACTERS+"'.",
                    e.getMessage());
            }
        }
    }

    /**
     * Test.
     */
    public void testEquals() {

        // ARRANGE
        final ResourceName foo = new ResourceName("fooBAR_123");
        final ResourceName bar = new ResourceName("fooBAR_123");

        // ASSERT
        assertEquals(foo, bar);
        assertEquals(bar, foo);
    }

    /**
     * Test.
     */
    public void testHashCode() {

        // ARRANGE
        final ResourceName foo = new ResourceName("fooBAR_123");
        final ResourceName bar = new ResourceName("fooBAR_123");

        // ASSERT
        assertEquals(foo.hashCode(), bar.hashCode());
    }

    /**
     * Test.
     */
    public void testEscapeMethod() {

        // ARRANGE
        final String invalidCharacters = "!*'();:@&=+$,/\\?%#[]foo BAR_123.~-";
        final String expectedName = "____________________foo_BAR_123.__";

        // ACT
        final ResourceName validCharacters =
            ResourceName.escape(invalidCharacters);

        // ASSERT
        assertEquals(new ResourceName(expectedName), validCharacters);
    }
}
