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

import java.util.ArrayList;
import java.util.List;



/**
 * Validation engine.
 *
 * @author Civic Computing Ltd.
 */
public final class Validate {


    /**
     * Validator implementation that prevents further validations from
     * executing.
     *
     * @author Civic Computing Ltd.
     */
    public class Stopper
        implements
            Validator {


        /** {@inheritDoc} */
        public void validate(final Validate validate) {
            if (getErrors().size() > 0) {
                setContinue(false);
            }
            next();
        }

    }

    private boolean _continue = true;
    private final List<String> _errors = new ArrayList<String>();
    private final Runnable _action;
    private final List<Validator> _validators = new ArrayList<Validator>();
    private int _nextValidation = 0;
    private ErrorReporter _reporter;

    /**
     * Constructor.
     *
     * @param createAlias
     */
    private Validate(final Runnable createAlias) {
        _action = createAlias;
    }


    /**
     * Accessor.
     *
     * @return Returns the errors.
     */
    List<String> getErrors() {
        return _errors;
    }


    /**
     * Mutator.
     *
     * @param continue1 The continue to set.
     */
    void setContinue(final boolean continue1) {
        _continue = continue1;
    }


    /**
     * Factory method for creating validation objects.
     *
     * @param runnable The behaviour we are validating.
     *
     * @return A validator for the action.
     */
    public static Validate callTo(final Runnable runnable) {
        return new Validate(runnable);
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param validator Validator to handle.
     * @return Validator.
     */
    public Validate check(final Validator validator) {
        _validators.add(validator);
        return this;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return Validator.
     */
    public Validate stopIfInError() {
        _validators.add(new Stopper());
        return this;
    }


    /**
     * Check whether any errors have been reported.
     * <p>If errors are reported the specified reporter will be notified.
     *
     * @param reporter The reporter to notify.
     */
    public void callMethodOr(final ErrorReporter reporter) {
        _reporter = reporter;
        next();
    }


    /**
     * Add a message to the list of error messages.
     *
     * @param message The error message to add.
     */
    public void addMessage(final String message) {
        _errors.add(message);
    }


    /**
     * Perform the next validation.
     */
    public void next() {
        // More validations & no explicit stop
        if (_continue && _nextValidation<_validators.size()) {
            final Validator v = _validators.get(_nextValidation);
            _nextValidation++;
            v.validate(this);
        } else {
            if (_errors.size() > 0) {
                _reporter.report(_errors);
            } else {
                _action.run();
            }
        }
    }
}
