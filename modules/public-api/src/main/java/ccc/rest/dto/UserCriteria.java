/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
 * All rights reserved.
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
 * FIXME: move to persistence
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
     * @param username
     * @param email
     * @param group
     */
    public UserCriteria(final String username,
                        final String email,
                        final String group) {
        _username = username;
        _email = email;
        _groups = group;
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
    public final String getUsername() {

        return _username;
    }

    /**
     * Accessor.
     *
     * @return Returns the role.
     */
    public final String getGroups() {

        return _groups;
    }

    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    public final String getEmail() {

        return _email;
    }

}
