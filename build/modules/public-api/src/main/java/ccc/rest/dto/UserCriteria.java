/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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
package ccc.rest.dto;

import java.io.Serializable;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


/**
 * TODO: move to ccc.persistence
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserCriteria implements Jsonable, Serializable {

    private String _username;
    private String _groups;
    private String _email;

    /**
     * Constructor.
     *
     * @param username Username criteria.
     * @param email Email criteria.
     * @param groups Groups criteria.
     */
    public UserCriteria(final String username,
                        final String email,
                        final String groups) {
        _username = username;
        _email = email;
        _groups = groups;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.USERNAME, _username);
        json.set(JsonKeys.EMAIL, _email);
        json.set(JsonKeys.GROUPS, _groups);
    }

    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    public String getUsername() {

        return _username;
    }

    /**
     * Accessor.
     *
     * @return Returns the role.
     */
    public String getGroups() {

        return _groups;
    }

    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    public String getEmail() {

        return _email;
    }

}
