/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.ArrayList;
import java.util.List;


/**
 * Validation results.
 *
 * @author Civic Computing Ltd.
 */
public class ValidationResult {

    private List<String> _errors;

    /**
     * Constructor.
     *
     */
    public ValidationResult() {
        _errors = new ArrayList<String>();
    }

    /**
     * Accessor.
     *
     * @return Returns the errors.
     */
    public final List<String> getErrors() {

        return _errors;
    }

    /**
     * Mutator.
     *
     * @param errors The errors to set.
     */
    public final void setErrors(final List<String> errors) {

        _errors = errors;
    }

    /**
     * Add a new error to the errors list.
     *
     * @param error The new error to add.
     */
    public void addError(final String error) {
        _errors.add(error);
    }

    /**
     * Accessor.
     *
     * @return Returns the valid.
     */
    public final boolean isValid() {
        return _errors.size() == 0;
    }

    /**
     * Return a string of errors messages.
     *
     * @return The error text.
     */
    public String getErrorText() {
        final StringBuilder sb = new StringBuilder();
        for (final String error : _errors) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(error);
        }
        return sb.toString();
    }

}
