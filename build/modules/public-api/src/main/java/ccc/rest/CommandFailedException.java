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
package ccc.rest;

import ccc.types.Failure;
import ccc.types.FailureCode;




/**
 * An exception representing the failure of a CCC command.
 *
 * @author Civic Computing Ltd.
 */
public class CommandFailedException
    extends
        Exception {

    private Failure _failure;

    @SuppressWarnings("unused") private CommandFailedException() { super(); }


    /**
     * Constructor.
     *
     * @param failure The failure.
     */
    public CommandFailedException(final Failure failure) {
        super("CCC Error: "+failure.getExceptionId());
        _failure = failure;
    }

    /**
     * Accessor.
     *
     * @return The failure's code.
     */
    public FailureCode getCode() {
        return _failure.getCode();
    }

    /**
     * Accessor.
     *
     * @return The failure.
     */
    public Failure getFailure() {
        return _failure;
    }
}
