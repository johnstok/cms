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

    public int _errorCode;
    public Map<String, String> _params = new HashMap<String, String>();
}
