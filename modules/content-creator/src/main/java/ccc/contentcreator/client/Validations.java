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

import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class Validations {

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    static ErrorReporter reportErrors() {
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
    static Validator notEmpty(final TextField<String> name) {
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

}
