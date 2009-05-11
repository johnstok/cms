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
package ccc.services;

import java.util.UUID;

import ccc.domain.User;

/**
 * DAO API for the {@link Alias} class.
 *
 * @author Civic Computing Ltd.
 */
public interface AliasDao {

    /** NAME : String. */
    String NAME = "AliasDao";

    /**
     * Update alias' target.
     *
     * @param targetId The new target UUID
     * @param aliasId The alias UUID
     */
    void updateAlias(User actor, UUID targetId, UUID aliasId);

}
