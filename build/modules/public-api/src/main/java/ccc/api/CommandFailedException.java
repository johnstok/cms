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
package ccc.api;




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
     * @param errorCode
     * @param localExceptionId
     */
    public CommandFailedException(final int errorCode,
                                  final String localExceptionId) {
        super("CCC Error: "+localExceptionId);
        _failure = new Failure(errorCode, localExceptionId);
    }

    /**
     * Accessor.
     *
     * @return The failure's code.
     */
    public int getCode() {
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
