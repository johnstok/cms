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
     * TODO: Add a description of this method.
     *
     * @param createAlias
     * @return
     */
    public static Validate callTo(final Runnable createAlias) {
        return new Validate(createAlias);
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
     * TODO: Add a description of this method.
     *
     * @param reporter
     */
    public void callMethodOr(final ErrorReporter reporter) {
        _reporter = reporter;
        next();
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param message
     */
    public void addMessage(final String message) {
        _errors.add(message);
    }


    /**
     * TODO: Add a description of this method.
     *
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
