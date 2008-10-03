/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 411 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2008-09-24 10:52:48 +0100 (Wed, 24 Sep 2008) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dto;

import java.util.HashSet;
import java.util.Set;

import com.extjs.gxt.ui.client.data.BaseModelData;



/**
 * A dto for a CCC resource.
 *
 * @author Civic Computing Ltd.
 */
public class UserDTO extends BaseModelData implements DTO {

    /**
     * Constructor.
     */
    public UserDTO() {
        super();
        setRoles(new HashSet<String>());
    }

    /**
     * Accessor for the id property.
     *
     * @return The id as a string.
     */
    public String getId() {
        return get("id");
    }

    /**
     * Mutator for the id property.
     *
     * @param id The id as a string.
     */
    public void setId(final String id) {
        set("id", id);
    }

    /**
     * Mutator for the version property.
     *
     * @param version The version as an integer.
     */
    public void setVersion(final Integer version) {
        set("version", version);
    }

    /**
     * Accessor for the version property.
     *
     * @return The version as an integer.
     */
    public Integer getVersion() {
        return get("version");
    }

    /**
     * Mutator for the email property.
     *
     * @param email The email as a string.
     */
    public void setEmail(final String email) {
        set("email", email);
    }

    /**
     * Accessor for the email property.
     *
     * @return The email as a string.
     */
    public String getEmail() {
        return get("email");
    }

    /**
     * Mutator for the username property.
     *
     * @param username The username as a string.
     */
    public void setUsername(final String username) {
        set("username", username);
    }

    /**
     * Accessor for the username property.
     *
     * @return The username as a string.
     */
    public String getUsername() {
        return get("username");
    }

    /**
     * Mutator for the roles property.
     *
     * @param roles The roles as a set.
     */
    public void setRoles(final Set<String> roles) {
        set("roles", roles);
    }

    /**
     * Accessor for the roles property.
     *
     * @return The roles as a set.
     */
    public Set<String> getRoles() {
        return get("roles");
    }
}
