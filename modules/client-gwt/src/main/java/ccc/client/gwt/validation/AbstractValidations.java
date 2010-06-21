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

package ccc.client.gwt.validation;

import java.util.List;

import ccc.api.core.Page;
import ccc.api.core.ResourceSummary;
import ccc.api.types.EmailAddress;
import ccc.api.types.Password;
import ccc.api.types.ResourceName;
import ccc.client.core.I18n;
import ccc.client.core.Response;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.remoting.ResourceNameExistsAction;
import ccc.client.gwt.remoting.ValidateFieldAction;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;
import ccc.client.validation.ErrorReporter;
import ccc.client.validation.Validate;
import ccc.client.validation.Validator;

import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AbstractValidations {

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
    static final UIMessages UI_MESSAGES = I18n.UI_MESSAGES;

    /**
     * Create a new error reporter.
     *
     * @return The error reporter for
     */
    public static ErrorReporter reportErrors() {
        return new ErrorReporter() {
            public void report(final List<String> errors) {
                ContentCreator.WINDOW.alert(errors.toString());
            }
        };
    }

    /**
     * Validates that name is not empty.
     *
     * @param name The field to validate.
     * @return The Validator
     */
    public static Validator notEmpty(final TextField<String> name) {
        return notEmpty(name.getValue(), name.getFieldLabel());
    }

    /**
     * Validates that value is not empty.
     *
     * @param value The string to validate.
     * @param label The label of the field to validate.
     * @return The Validator
     */
    public static Validator notEmpty(final String value, final String label) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(null == value || value.trim().equals("")) {
                    validate.addMessage(
                        label
                        + UI_CONSTANTS.cannotBeEmpty()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Validates resource name. Fails if name contains spaces etc.
     *
     * @param name Resource name
     * @return The Validator
     */
    public static Validator notValidResourceName(final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(!name.getValue().matches(VALID_CHARACTERS)) {
                    validate.addMessage(
                        name.getFieldLabel()
                        + " "+UI_CONSTANTS.isNotValidResourceName()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Validates user name. Fails if name contains spaces etc.
     *
     * @param name User name
     * @return The Validator
     */
    public static Validator notValidUserName(final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(!name.getValue().matches(VALID_USERNAME_CHARACTERS)) {
                    validate.addMessage(
                        name.getFieldLabel()
                        + " "+UI_CONSTANTS.isNotValidUserName()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Checks that the folder does not contain given resource name.
     *
     * @param folder FolderDTO to check
     * @param name Resource name
     * @return The Validator
     */
    public static Validator uniqueResourceName(final ResourceSummaryModelData folder, final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                // FIXME: Conversion to type ResourceName can fail.
                new ResourceNameExistsAction(folder.getDelegate(),
                                             new ResourceName(name.getValue())){
                    @Override protected void execute(final boolean nameExists) {
                        if (nameExists) {
                            validate.addMessage(
                                UI_MESSAGES.nameExistsInFolder(name.getValue())
                            );
                        }
                        validate.next();
                    }
                }.execute();
            }
        };
    }

    /**
     * Validates email.
     *
     * @param email The email
     * @return The Validator
     */
    public static Validator notValidEmail(final TextField<String> email) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(!EmailAddress.isValidText(email.getValue())) {
                    validate.addMessage(
                        email.getFieldLabel()
                        + " "+UI_CONSTANTS.isNotValid()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Check whether given resource's parent has a resource with given name.
     *
     * @param folder The folder to check.
     * @param name name Resource name
     * @return The Validator
     */
    public static Validator uniqueResourceName(final ResourceSummary folder, final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                // FIXME: Conversion to type ResourceName can fail.
                new ResourceNameExistsAction(folder,
                                             new ResourceName(name.getValue())){
                    @Override protected void execute(final boolean nameExists) {
                        if (nameExists) {
                           validate.addMessage(
                               UI_MESSAGES.nameExistsInParentFolder(
                                   name.getValue())
                           );
                        }
                        validate.next();
                    }
                }.execute();
            }
        };
    }

    /**
     * Validates that input is not too short.
     *
     * @param input The string to validate.
     * @param min The minimum length of the String
     * @return The Validator
     */
    public static Validator minLength(final TextField<String> input, final int min) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(null == input.getValue()
                   || input.getValue().length() < min) {
                    validate.addMessage(
                        input.getFieldLabel()
                        + " "+UI_CONSTANTS.isTooShort()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Validates page fields against template regular expressions.
     *
     * @param page The page to validate.
     * @return The Validator
     */
    public static Validator validateFields(final Page page) {
        return new Validator() {
            public void validate(final Validate validate) {
                new ValidateFieldAction(page) {
                    /** {@inheritDoc} */
                    @Override
                    protected void onOK(final Response response) {
                        final String errors = response.getText();

                        if (!errors.isEmpty()) {

                            validate.addMessage(
                                UI_CONSTANTS.regexpValidationFailed()
                                +errors
                            );
                        }
                        validate.next();
                    }
                }.execute();
            }
        };
    }

    /**
     * Validates text so it does not contain bracket < > characters.
     *
     * @param text The text
     * @return The Validator
     */
    public static Validator noBrackets(final TextField<String> text) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(text.getValue() != null
                        && !text.getValue().matches(NO_BRACKETS)) {
                    validate.addMessage(
                        text.getFieldLabel()
                        + " "+UI_CONSTANTS.mustNotContainBrackets()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Factory method for password matching validators.
     *
     * @param pw1 The password to check.
     * @param pw2 The password to check.
     * @return A new instance of the password validator.
     */
    public static Validator matchingPasswords(final String pw1, final String pw2) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (pw1 != null && !pw1.equals(pw2)) {
                    validate.addMessage(UI_CONSTANTS.passwordsDidNotMatch());
                }
                validate.next();
            }
        };
    }

    /**
     * Factory method for password strength validators.
     *
     * @param pw The password to check.
     * @return A new instance of the password validator.
     */
    public static Validator passwordStrength(final String pw) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (pw != null && pw.length() < MIN_PASSWORD_LENGTH) {
                    validate.addMessage(UI_CONSTANTS.passwordTooShort());
                } else if (!Password.isStrong(pw)) {
                    validate.addMessage(UI_CONSTANTS.passwordTooWeak());
                }
                validate.next();
            }
        };
    }

    /**
     * Create a validator that checks a field contains only numeric characters
     * (0-9).
     *
     * @param input The field to validate.
     * @return A new validator instance.
     */
    public static Validator emptyOrNumber(final TextField<String> input) {

        return new Validator() {
            public void validate(final Validate validate) {
                final String value = input.getValue();
                if(null != value
                   && !value.trim().equals("")
                   && !value.matches("^([0-9]|[1-9][0-9]|[1-9][0-9][0-9])$")) {
                    validate.addMessage(
                        input.getFieldLabel()
                        + " "+UI_CONSTANTS.isNotValid()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Constructor.
     *
     */
    public AbstractValidations() {

        super();
    }

}