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

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Async version of the {@link SecurityService} interface.
 *
 * @author Civic Computing Ltd.
 */
public interface SecurityServiceAsync {

    void logout(AsyncCallback<Void> asyncCallback);

    void login(String username, String password, AsyncCallback<Boolean> asyncCallback);

    void isLoggedIn(AsyncCallback<Boolean> asyncCallback);

    void loggedInUserRoles(AsyncCallback<Set<String>> asyncCallback);
}
