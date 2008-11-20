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
     * TODO: Add a description of this method.
     *
     * @param name
     * @return
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
     * TODO: Add a description of this method.
     *
     * @param name
     * @return
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

}
