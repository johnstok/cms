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
        RuntimeException {

    public static final int EXISTS = 3;
    public static final int PRIVILEGES = 4;

    private int _errorCode;
    private ActionType _action;
    private Map<String, String> _params = new HashMap<String, String>();

    /**
     * Constructor.
     *
     * @param errorCode
     * @param action
     * @param params
     */
    public CCCRemoteException(final int errorCode,
                              final ActionType action,
                              final Map<String, String> params) {
        _errorCode = errorCode;
        _action = action;
        _params = params;
    }

    /**
     * Constructor.
     *
     * @param errorCode
     * @param action
     */
    public CCCRemoteException(final int errorCode, final ActionType action) {
        this(errorCode, action, new HashMap<String, String>());
    }
}
