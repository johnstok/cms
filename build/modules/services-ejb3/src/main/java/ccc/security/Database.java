/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.security;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import ccc.api.types.Username;


/**
 * Database API used for authentication.
 *
 * @author Civic Computing Ltd.
 */
public interface Database {

    /**
     * Lookup details of a user from the DB. Details are returned in object
     * array with the following structure:
     * <br>[0] user id,         as a string
     * <br>[1] hashed password, as a byte array
     * <br>[2] password salt,   as a string
     *
     * @param username The username representing the user to look up.
     * @return The user data as an object array; NULL if no valid user is found.
     * @throws SQLException If an error occurs while communicating with the DB.
     */
    Object[] lookupUser(Username username) throws SQLException;

    /**
     * Look up a user's roles from the DB..
     *
     * @param userId The user id whose roles we will retrieve.
     * @return A set of roles, represented as strings.
     * @throws SQLException If an error occurs while communicating with the DB.
     */
    Set<String> lookupRoles(String userId) throws SQLException;

    /**
     * Specify the options for the database.
     *
     * @param options A map of key:value pairs.
     */
    void setOptions(Map<String, ?> options);

}
