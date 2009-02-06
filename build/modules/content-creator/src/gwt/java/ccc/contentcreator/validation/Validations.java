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

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.services.api.ParagraphDelta;

import com.extjs.gxt.ui.client.data.ModelData;
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

    private static final String  VALID_CHARACTERS = "[\\.\\w]+";

    private static final String VALID_EMAIL =
        "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
        + "@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*"
        +"[a-z0-9])?";

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
                        + " "+_uiConstants.isNotValid()
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
    public static Validator uniqueResourceName(final ModelData folder,
                                         final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                Globals.queriesService().nameExistsInFolder(
                    folder.<String>get("id"),
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    "A resource with name '"
                                    + name.getValue()
                                    + _uiConstants.alreadyExistsInThisFolder()
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
    public static Validator uniqueResourceName(final String id,
                                               final TextField<String> name) {
        return new Validator() {
            public void validate(final Validate validate) {
                Globals.queriesService().nameExistsInFolder(
                    id,
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    _uiConstants.resourceWithName()
                                    + name.getValue()
                                    + _uiConstants.alreadyExistsInTheParentFolder()
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
    public static Validator validateFields(final List<ParagraphDelta> delta,
                                           final String definition) {
        return new Validator() {
            public void validate(final Validate validate) {
                Globals.commandService().validateFields(
                    delta,
                    definition,
                    new ErrorReportingCallback<List <String>>(){
                        public void onSuccess(final List<String> errors) {
                            if (!errors.isEmpty()) {
                                final StringBuffer sb = new StringBuffer();
                                for (final String error : errors) {
                                    sb.append(error);
                                    sb.append(" ");
                                }

                                validate.addMessage(
                                    _uiConstants.validationFailed()
                                    +sb.toString()
                                );
                            }
                            validate.next();
                        }});
            }
        };
    }
}
