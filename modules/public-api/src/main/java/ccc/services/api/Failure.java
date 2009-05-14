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
package ccc.services.api;

import java.io.Serializable;


/**
 * A serializable representation of CCC failure.
 *
 * @author Civic Computing Ltd.
 */
public class Failure implements Serializable {

    public static final int UNEXPECTED    = 0;
    public static final int UNLOCKED      = 1;
    public static final int LOCK_MISMATCH = 2;
    public static final int EXISTS        = 3;
    public static final int PRIVILEGES    = 4;

    private int                 _code        = 0;
    private ParamList           _params      = new ParamList();
    private String              _exceptionId = "";

    @SuppressWarnings("unused") private Failure() { super(); }

    /**
     * Constructor.
     *
     * @param code The internal code for this failure.
     * @param params Additional details regarding the failure.
     * @param exceptionId The unique id of the exception logged for this
     *  failure.
     */
    public Failure(final int code,
                   final ParamList params,
                   final String exceptionId) {
        _code = code;
        _params = params;
        _exceptionId = exceptionId;
    }


    /**
     * Accessor.
     *
     * @return Returns the errorCode.
     */
    public int getCode() {
        return _code;
    }


    /**
     * Accessor.
     *
     * @return Returns the param's.
     */
    public ParamList getParams() {
        return _params;
    }


    /**
     * Accessor.
     *
     * @return Returns the exceptionId.
     */
    public String getExceptionId() {
        return _exceptionId;
    }
}
