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
package ccc.contentcreator.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * Security API.
 *
 * @author Civic Computing Ltd.
 */
@RemoteServiceRelativePath("security")
public interface SecurityService
    extends
        RemoteService {

    /**
     * End a user's session.
     */
    void logout();

    /**
     * Authenticate a user and start a new session.
     *
     * @param username The user to authenticate.
     * @param password The user's credentials.
     * @return true if the user is successfully authenticated; false otherwise.
     */
    boolean login(String username, String password);

    /**
     * Query whether the current session has a valid user associated with it.
     *
     * @return true if there is a user associated; false otherwise.
     */
    boolean isLoggedIn();

    /**
     * Reads property string.
     *
     * @param key The key of the property.
     * @return Value of the property if found.
     */
    String readProperty(final String key);

}
