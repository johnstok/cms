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
package ccc.contentcreator.validation;

import java.util.List;
import java.util.Set;

import ccc.api.ID;
import ccc.api.Paragraph;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Validations {

    private final static UIConstants _uiConstants =
        GWT.create(UIConstants.class);
    private final static UIMessages _uiMessages =
        GWT.create(UIMessages.class);
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);

    private static final String  VALID_CHARACTERS = "[\\.\\-\\w]+";

    private static final String  VALID_USERNAME_CHARACTERS = "[\\w]+";

    private static final String  NO_BRACKETS = "[^<^>]*";

    private static final String VALID_EMAIL =
        "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
        + "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*"
        +"[a-z0-9])?";

    private static final String  STRONG_PW =
        "^(?=.{10,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$";

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public static ErrorReporter reportErrors() {
        return new ErrorReporter() {
            public void report(final List<String> errors) {
                Globals.alert(errors.toString());
            }
        };
    }

    /**
     * Validates that name is not empty.
     *
     * @param name The string to validate.
     * @return The Validator
     */
    public static Validator notEmpty(final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(null == name.getValue()
                   || name.getValue().trim().equals("")) {
                    validate.addMessage(
                        name.getFieldLabel()
                        + " "+_uiConstants.cannotBeEmpty()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Validates that xml is valid.
     *
     * @param definition The string to validate.
     * @return The Validator
     */
    public static Validator notValidXML(final TextField<String> definition) {
        return new Validator() {
            public void validate(final Validate validate) {
                try {
                    XMLParser.parse(definition.getValue());
                } catch (final DOMParseException e) {
                    validate.addMessage("XML "+_uiConstants.isNotValid());
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
                        + " "+_uiConstants.isNotValidResourceName()
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
                        + " "+_uiConstants.isNotValidUserName()
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
    public static Validator uniqueResourceName(
                                         final ResourceSummaryModelData folder,
                                         final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                Globals.queriesService().nameExistsInFolder(
                    folder.getId(),
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(USER_ACTIONS.checkUniqueResourceName()){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    _uiMessages.resourceWithNameAlreadyExistsInThisFolder(name.getValue())
                                );
                            }
                            validate.next();
                        }});
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
                if(!email.getValue().matches(VALID_EMAIL)) {
                    validate.addMessage(
                        email.getFieldLabel()
                        + " "+_uiConstants.isNotValid()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Check whether given resource's parent has a resource with given name.
     *
     * @param id The id of the resource which parent folder to check.
     * @param name name Resource name
     * @return The Validator
     */
    public static Validator uniqueResourceName(final ID id,
                                               final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                Globals.queriesService().nameExistsInFolder(
                    id,
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(USER_ACTIONS.checkUniqueResourceName()){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    _uiMessages.resourceWithNameAlreadyExistsInTheParentFolder(name.getValue())
                                );
                            }
                            validate.next();
                        }});
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
    public static Validator minLength(final TextField<String> input,
                                      final int min) {
        return new Validator() {
            public void validate(final Validate validate) {
                if(null == input.getValue()
                   || input.getValue().length() < min) {
                    validate.addMessage(
                        input.getFieldLabel()
                        + " "+_uiConstants.isTooShort()
                    );
                }
                validate.next();
            }
        };
    }

    /**
     * Validates page fields against template regular expressions.
     *
     * @param delta ParagraphDelta of the paragraph fields to validate.
     * @param definition Template definition.
     * @return The Validator
     */
    public static Validator validateFields(final Set<Paragraph> delta,
                                           final String definition) {
        return new Validator() {
            public void validate(final Validate validate) {
                Globals.commandService().validateFields(
                    delta,
                    definition,
                    new ErrorReportingCallback<List <String>>(USER_ACTIONS.validatePageFields()){
                        public void onSuccess(final List<String> errors) {
                            if (!errors.isEmpty()) {
                                final StringBuffer sb = new StringBuffer();
                                for (final String error : errors) {
                                    sb.append(error);
                                    sb.append(" ");
                                }

                                validate.addMessage(
                                    _uiConstants.regexpValidationFailed()
                                    +sb.toString()
                                );
                            }
                            validate.next();
                        }});
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
                        + " "+_uiConstants.mustNotContainBrackets()
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
    public static Validator matchingPasswords(final String pw1,
                                              final String pw2) {
        return new Validator() {
            public void validate(final Validate validate) {
                if (pw1 != null && !pw1.equals(pw2)) {
                    validate.addMessage(_uiConstants.passwordsDidNotMatch());
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
                if (pw != null && pw.length() < 10) {
                    validate.addMessage(_uiConstants.passwordTooShort());
                } else if (!pw.matches(STRONG_PW)) {
                    validate.addMessage(_uiConstants.passwordTooWeak());
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
                        + " "+_uiConstants.isNotValid()
                    );
                }
                validate.next();
            }
        };
    }
}
