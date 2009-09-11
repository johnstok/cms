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

import java.util.ArrayList;
import java.util.List;



/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class Validate {


    /**
     * TODO: Add Description for this type.
     *
     * @author Civic Computing Ltd.
     */
    public class Stopper
        implements
            Validator {


        /** {@inheritDoc} */
        public void validate(final Validate validate) {
            if (_errors.size() > 0) {
                _continue = false;
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
