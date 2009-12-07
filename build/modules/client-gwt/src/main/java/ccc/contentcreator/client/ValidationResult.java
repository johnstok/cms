/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
                sb.append("\n");
            }
            sb.append(error);
        }
        return sb.toString();
    }

}
