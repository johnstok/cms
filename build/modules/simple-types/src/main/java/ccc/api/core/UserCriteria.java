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
package ccc.api.core;

import java.io.Serializable;


/**
 * TODO: move to ccc.persistence
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class UserCriteria implements Serializable {

    private String _username = null;
    private String _groups = null;
    private String _email = null;
    private String _metadataKey = null;
    private String _metadataValue = null;

    /**
     * Constructor.
     *
     */
    public UserCriteria() {
        super();
    }

    /**
     * Constructor.
     *
     * @param username Username criteria.
     * @param email Email criteria.
     * @param groups Groups criteria.
     * @param metadataKey Metadata key criteria.
     * @param metadataValue Metadata value criteria.
     */
    public UserCriteria(final String username,
                        final String email,
                        final String groups,
                        final String metadataKey,
                        final String metadataValue) {
        _username = username;
        _email = email;
        _groups = groups;
        _metadataKey = metadataKey;
        _metadataValue = metadataValue;
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
     * @return Returns the group.
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

    /**
     * Accessor.
     *
     * @return Returns the metadata key.
     */
    public String getMetadataKey() {
        return _metadataKey;
    }

    /**
     * Accessor.
     *
     * @return Returns the metadata value.
     */
    public String getMetadataValue() {
        return _metadataValue;
    }


    /**
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        _username = username;
    }


    /**
     * Mutator.
     *
     * @param groups The groups to set.
     */
    public void setGroups(final String groups) {
        _groups = groups;
    }


    /**
     * Mutator.
     *
     * @param email The email to set.
     */
    public void setEmail(final String email) {
        _email = email;
    }


    /**
     * Mutator.
     *
     * @param metadataKey The metadataKey to set.
     */
    public void setMetadataKey(final String metadataKey) {
        _metadataKey = metadataKey;
    }


    /**
     * Mutator.
     *
     * @param metadataValue The metadataValue to set.
     */
    public void setMetadataValue(final String metadataValue) {
        _metadataValue = metadataValue;
    }


}
