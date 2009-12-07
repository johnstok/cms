/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
     * Reads all property strings.
     *
     * @return Map as a JSON object.
     */
    String readAllProperties();

}
