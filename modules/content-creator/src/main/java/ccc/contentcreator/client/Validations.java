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
package ccc.contentcreator.client;

import java.util.List;

import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dto.FolderDTO;

import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Validations {

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
                    validate.addMessage( // TODO: Fix
                        name.getFieldLabel()
                        + " cannot be empty"
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
                        + " is not valid" // i18n
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
    public static Validator uniqueResourceName(final FolderDTO folder,
                                         final TextField<String> name) {

        return new Validator() {
            public void validate(final Validate validate) {
                Globals.resourceService().nameExistsInFolder(
                    folder,
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    "A resource with name '"
                                    + name.getValue()
                                    + "' already exists in this folder."
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
                        + " is not valid" // i18n
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
                Globals.resourceService().nameExistsInParentFolder(
                    id,
                    name.getValue(),
                    new ErrorReportingCallback<Boolean>(){
                        public void onSuccess(final Boolean nameExists) {
                            if (nameExists) {
                                validate.addMessage(
                                    "A resource with name '"
                                    + name.getValue()
                                    + "' already exists in the parent folder."
                                );
                            }
                            validate.next();
                        }});
            }
        };
    }
}
