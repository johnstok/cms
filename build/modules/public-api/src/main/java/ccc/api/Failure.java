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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * A serializable representation of CCC failure.
 *
 * @author Civic Computing Ltd.
 */
public class Failure implements Serializable {

    /** UNEXPECTED : int. */
    public static final int UNEXPECTED    = 0;
    /** UNLOCKED : int. */
    public static final int UNLOCKED      = 1;
    /** LOCK_MISMATCH : int. */
    public static final int LOCK_MISMATCH = 2;
    /** EXISTS : int. */
    public static final int EXISTS        = 3;
    /** PRIVILEGES : int. */
    public static final int PRIVILEGES    = 4;
    /** WC_UNSUPPORTED : int. */
    public static final int WC_UNSUPPORTED = 5;
    /** CYCLE : int. */
    public static final int CYCLE = 6;

    private int                 _code        = 0;
    private String              _exceptionId = "";
    private Map<String, String> _params      = new HashMap<String, String>();

    @SuppressWarnings("unused") private Failure() { super(); }

    /**
     * Constructor.
     *
     * @param code The internal code for this failure.
     * @param exceptionId The unique id of the exception logged for this
     *  failure.
     */
    public Failure(final int code,
                   final String exceptionId) {
        _code = code;
        _exceptionId = exceptionId;
    }

    /**
     * Constructor.
     *
     * @param code The internal code for this failure.
     * @param exceptionId The unique id of the exception logged for this
     *  failure.
     * @param params Further details describing the failure.
     */
    public Failure(final int code,
                   final String exceptionId,
                   final Map<String, String> params) {
        _code = code;
        _exceptionId = exceptionId;
        _params.putAll(params);
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
     * @return Returns the exceptionId.
     */
    public String getExceptionId() {
        return _exceptionId;
    }


    /**
     * Accessor.
     *
     * @return Returns the parameters.
     */
    public final Map<String, String> getParams() {
        return _params;
    }
}
