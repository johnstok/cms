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

package ccc.client.validation;

import java.util.List;
import java.util.Map;

import ccc.api.types.EmailAddress;
import ccc.api.types.Password;
import ccc.client.core.I18n;
import ccc.client.core.Window;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;

/**
 * Base implementation of non platform-specific validations.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractValidations {

    /** MIN_PASSWORD_LENGTH : int. */
    public static final int MIN_PASSWORD_LENGTH = 10;
    /** VALID_CHARACTERS : String. */
    public static final String VALID_CHARACTERS = "[\\.\\-\\w]+";
    /** VALID_USERNAME_CHARACTERS : String. */
    public static final String VALID_USERNAME_CHARACTERS = "[\\w]+";
    /** VALID_PASSWORD_CHARACTERS : String. */
    public static final String VALID_PASSWORD_CHARACTERS = "[\\S]+";
    /** NO_BRACKETS : String. */
    public static final String NO_BRACKETS = "[^<^>]*";
    /** VALID_URL : String. */
    public static final String VALID_URL = "(http://|https://)([a-zA-Z0-9]+\\."
            + "[a-zA-Z0-9\\-]+|[a-zA-Z0-9\\-]+)\\.[a-zA-Z0-9\\.]{2,9}(/"
            + "[a-zA-Z0-9\\.\\?=/#%&\\+-]+|/|)";
    /** UI_CONSTANTS : UIConstants. */
    protected static final UIConstants UI_CONSTANTS = I18n.UI_CONSTANTS;
    /** UI_MESSAGES : UIMessages. */
    protected static final UIMessages UI_MESSAGES = I18n.UI_MESSAGES;


    /**
     * Create a new error reporter.
     *
     * @param window The window that will report the error.
     *
     * @return The error reporter for the specified window.
     */
    public ErrorReporter reportErrors(final Window window) {
        return new ErrorReporter() {
            public void report(final List<String> errors) {
                window.alert(errors.toString());
            }
        };
    }


    /**
     * Validates that value is not empty.
     *
     * @param value The string to validate.
     * @param label The label of the field to validate.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String notEmpty(final String value, final String label) {
        if(null == value || value.trim().equals("")) {
            return label + " " + UI_CONSTANTS.cannotBeEmpty();
        }
        return null;
    }


    /**
     * Validates resource name. Fails if name contains spaces etc.
     *
     * @param value The value to test.
     * @param label The field label.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String notValidResourceName(final String value, final String label) {
        if(!value.matches(VALID_CHARACTERS)) {
            return label + " " + UI_CONSTANTS.isNotValidResourceName();
        }
        return null;
    }


    /**
     * Validates user name. Fails if name contains spaces etc.
     *
     * @param value The value to test.
     * @param label The field label.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String notValidUserName(final String value,
                                             final String label) {
        if(!value.matches(VALID_USERNAME_CHARACTERS)) {
            return label + " " + UI_CONSTANTS.isNotValidUserName();
        }
        return null;
    }


    /**
     * Validates email.
     *
     * @param value The value to test.
     * @param label The field label.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String notValidEmail(final String value, final String label) {
        if(!EmailAddress.isValidText(value)) {
            return label + " "+UI_CONSTANTS.isNotValid();
        }
        return null;
    }


    /**
     * Validates that input is not too short.
     *
     * @param value The value to test.
     * @param label The field label.
     * @param min The minimum length of the string.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String minLength(final String value,
                            final String label,
                            final int min) {
        if(null == value || value.length() < min) {
            return label + " "+UI_CONSTANTS.isTooShort();
        }
        return null;
    }


    /**
     * Validates text so it does not contain bracket < > characters.
     *
     * @param value The value to test.
     * @param label The field label.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String noBrackets(final String value, final String label) {
        if(value != null && !value.matches(NO_BRACKETS)) {
            return label + " "+UI_CONSTANTS.mustNotContainBrackets();
        }
        return null;
    }


    /**
     * Factory method for password matching validators.
     *
     * @param pw1 The password to check.
     * @param pw2 The password to check.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String matchingPasswords(final String pw1, final String pw2) {
        if (pw1 != null && !pw1.equals(pw2)) {
            return UI_CONSTANTS.passwordsDidNotMatch();
        }
        return null;
    }


    /**
     * Factory method for password strength validators.
     *
     * @param pw The password to check.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String passwordStrength(final String pw) {
        if (pw == null || pw.length() < MIN_PASSWORD_LENGTH) {
            return UI_CONSTANTS.passwordTooShort();
        } else if (!Password.isStrong(pw)) {
            return UI_CONSTANTS.passwordTooWeak();
        }
        return null;
    }


    /**
     * Create a validator that checks a field contains only numeric characters
     * (0-9).
     *
     * @param value The value to test.
     * @param label The field label.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String emptyOrNumber(final String value, final String label) {
        if(null != value
           && !value.trim().equals("")
           && !value.matches("^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$")) {
            return label + " "+UI_CONSTANTS.isNotValid();
        }
        return null;
    }


    /**
     * Factory method for metadata validators.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public String validateMetadataValues(final Map<String, String> data) {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> datum : data.entrySet()) {
            if (null==datum.getKey()
                || datum.getKey().trim().length() < 1) {
                sb.append(UI_CONSTANTS.noEmptyKeysAllowed());
            }
            if (null==datum.getValue()
                || datum.getValue().trim().length() < 1) {
                sb.append(UI_CONSTANTS.noEmptyValuesAllowed());
            }
            if (!datum.getKey().matches("[^<^>]*")) {
                sb.append(
                    UI_CONSTANTS.keysMustNotContainBrackets());
            }
            if (!datum.getValue().matches("[^<^>]*")) {
                sb.append(
                    UI_CONSTANTS.valuesMustNotContainBrackets());
            }
        }
        if (sb.length() > 0) {
            return sb.toString();
        }
        return null;
    }


    /**
     * Validates that xml is valid.
     *
     * @param definition The string to validate.
     *
     * @return The error message as a string or NULL if the value is valid.
     */
    public abstract String notValidXML(final String definition);
}