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

import java.util.HashMap;
import java.util.Map;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CCCRemoteException
    extends
        Exception {

    public static final int UNEXPECTED    = 0;
    public static final int UNLOCKED      = 1;
    public static final int LOCK_MISMATCH = 2;
    public static final int EXISTS        = 3;
    public static final int PRIVILEGES    = 4;

    private int _errorCode = -1;
    private ActionType _action;
    private Map<String, String> _params = new HashMap<String, String>();
    private String _localExceptionId = "";

    @SuppressWarnings("unused") private CCCRemoteException() { super(); }

    /**
     * Constructor.
     *
     * @param errorCode
     * @param action
     * @param params
     * @param localExceptionId
     */
    public CCCRemoteException(final int errorCode,
                              final ActionType action,
                              final String localExceptionId,
                              final Map<String, String> params) {
        super("Remote exception: "+localExceptionId);
        _errorCode = errorCode;
        _action = action;
        _params = params;
        _localExceptionId  = localExceptionId;
    }

    /**
     * Constructor.
     *
     * @param errorCode
     * @param action
     * @param localExceptionId
     */
    public CCCRemoteException(final int errorCode,
                              final ActionType action,
                              final String localExceptionId) {
        this(
            errorCode, action, localExceptionId, new HashMap<String, String>());
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public int getCode() {
        return _errorCode;
    }
}
