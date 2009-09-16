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
package ccc.contentcreator.validation;

import ccc.types.Password;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;


/**
 * Simplified, synchronous validation library.
 *
 * @author Civic Computing Ltd.
 */
public final class Validations2 {

    private Validations2() { super(); }


    /**
     * Validates that a string is not empty.
     *
     * @param name The string to validate.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean notEmpty(final String name) {
        return null != name
               && !name.trim().equals("");
    }

    /**
     * Validates that string is a valid template definition.
     *
     * @param definition The string to validate.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean notValidXML(final String definition) {
        try {
            final Document d = XMLParser.parse(definition);
            final NodeList l = d.getElementsByTagName("option");
            for (int n=0; n<l.getLength(); n++) {
                final NamedNodeMap al = l.item(n).getAttributes();
                final Node value = al.getNamedItem("value");
                if (value != null
                    && value.getNodeValue().indexOf(',') != -1) {
                    return false;
                }
            }
            return true;
        } catch (final DOMParseException e) {
            return false;
        }
    }

    /**
     * Validates resource name. Fails if name contains spaces etc.
     *
     * @param name Resource name.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean notValidResourceName(final String name) {
        return name.matches(Validations.VALID_CHARACTERS);
    }

    /**
     * Validates user name. Fails if name contains spaces etc.
     *
     * @param name User name.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean notValidUserName(final String name) {
        return name.matches(Validations.VALID_USERNAME_CHARACTERS);
    }

    /**
     * Validates email.
     *
     * @param email The email.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean notValidEmail(final String email) {
        return email.matches(Validations.VALID_EMAIL);
    }


    /**
     * Validates that input is not too short.
     *
     * @param input The string to validate.
     * @param min The minimum length of the String.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean minLength(final String input, final int min) {
        return null != input
               && input.length() >= min;
    }

    /**
     * Validates text so it does not contain bracket < > characters.
     *
     * @param text The text
     * @return The Validator
     */
    public static boolean noBrackets(final String text) {
        return text != null
               && !text.matches(Validations.NO_BRACKETS);
    }

    /**
     * Validate that two strings match.
     *
     * @param pw1 The password to check.
     * @param pw2 The password to check.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean match(final String pw1, final String pw2) {
        return pw1 != null
               && pw1.equals(pw2);
    }

    /**
     * Validate that a string is strong enough to be a password.
     *
     * @param pw The password to check.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean passwordStrength(final String pw) {
        return pw != null
               && pw.trim().length() >= Validations.MIN_PASSWORD_LENGTH
               && Password.isStrong(pw);
    }

    /**
     * Validate that a string contains only numeric characters (0-9).
     *
     * @param input The field to validate.
     *
     * @return True if the input is valid, false otherwise.
     */
    public static boolean emptyOrNumber(final String input) {
        return null != input
               && !input.trim().equals("")
               && !input.matches("^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$");
    }
}
