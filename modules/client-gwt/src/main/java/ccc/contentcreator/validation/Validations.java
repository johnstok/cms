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
package ccc.contentcreator.validation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.Globals;
import ccc.contentcreator.core.GlobalsImpl;
import ccc.contentcreator.i18n.UIConstants;
import ccc.contentcreator.i18n.UIMessages;
import ccc.contentcreator.remoting.ResourceNameExistsAction;
import ccc.contentcreator.remoting.ValidateFieldAction;
import ccc.types.Paragraph;
import ccc.types.Password;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.http.client.Response;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.impl.DOMParseException;


/**
 * Client side validations library.
 *
 * @author Civic Computing Ltd.
 */
@Deprecated
public final class Validations {

    /** MIN_PASSWORD_LENGTH : int. */
    public static final int MIN_PASSWORD_LENGTH = 10;

    /** VALID_CHARACTERS : String. */
    public static final String  VALID_CHARACTERS = "[\\.\\-\\w]+";

    /** VALID_USERNAME_CHARACTERS : String. */
    public static final String  VALID_USERNAME_CHARACTERS = "[\\w]+";

    /** NO_BRACKETS : String. */
    public static final String  NO_BRACKETS = "[^<^>]*";

    /** VALID_EMAIL : String. */
    public static final String VALID_EMAIL =
        "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
        + "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*"
        +"[a-z0-9])?";

    /** VALID_URL : String. */
    public static final String VALID_URL ="(http://|https://)([a-zA-Z0-9]+\\."
        + "[a-zA-Z0-9\\-]+|[a-zA-Z0-9\\-]+)\\.[a-zA-Z0-9\\.]{2,9}(/"
        + "[a-zA-Z0-9\\.\\?=/#%&\\+-]+|/|)";

    /** GLOBALS : IGlobals. */
    static final Globals GLOBALS = new GlobalsImpl();

    /** UI_CONSTANTS : UIConstants. */
    static final UIConstants UI_CONSTANTS = GLOBALS.uiConstants();

    /** UI_MESSAGES : UIMessages. */
    static final UIMessages UI_MESSAGES = GLOBALS.uiMessages();

    private Validations() {
        super();
    }

    /**
     * Create a new error reporter.
     *
     * @return The error reporter for
     */
    public static ErrorReporter reportErrors() {
        return new ErrorReporter() {
            public void report(final List<String> errors) {
                GLOBALS.alert(errors.toString());
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
     * Validates that xml is valid.
     *
     * @param definition The string to validate.
     * @return The Validator
     */
    public static Validator notValidXML(final String definition) {
        return new Validator() {
            public void validate(final Validate validate) {
                try {
                    final Document d = XMLParser.parse(definition);
                    final NodeList l = d.getElementsByTagName("option");
                    for (int n=0; n<l.getLength(); n++) {
                        final NamedNodeMap al = l.item(n).getAttributes();
                        final Node value = al.getNamedItem("value");
                        if (value != null
                            && value.getNodeValue().indexOf(',') != -1) {
                            validate.addMessage("XML option value "
                                + UI_CONSTANTS.mustNotContainComma());
                        }
                    }
                } catch (final DOMParseException e) {
                    validate.addMessage("XML "+UI_CONSTANTS.isNotValid());
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
    public static Validator uniqueResourceName(
                                         final ResourceSummaryModelData folder,
                                         final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                new ResourceNameExistsAction(folder.getId(),
                                             name.getValue()){
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
                if(!email.getValue().matches(VALID_EMAIL)) {
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
     * @param id The id of the resource which parent folder to check.
     * @param name name Resource name
     * @return The Validator
     */
    public static Validator uniqueResourceName(final UUID id,
                                               final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                new ResourceNameExistsAction(id,
                                             name.getValue()){
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
    public static Validator minLength(final TextField<String> input,
                                      final int min) {
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
     * @param delta ParagraphDelta of the paragraph fields to validate.
     * @param definition Template definition.
     * @return The Validator
     */
    public static Validator validateFields(final Set<Paragraph> delta,
                                           final String definition) {
        return new Validator() {
            public void validate(final Validate validate) {
                new ValidateFieldAction(delta, definition) {
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
    public static Validator matchingPasswords(final String pw1,
                                              final String pw2) {
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
}
