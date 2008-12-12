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

import ccc.domain.Alias;


/**
 * DAO API for the {@link Alias} class.
 *
 * @author Civic Computing Ltd.
 */
public interface AliasDao {

    /**
     * Create an Alias in the specified folder.
     *
     * @param folderId The {@link UUID} for the containing folder.
     * @param alias The Alias to be created.
     */
    void create(UUID folderId, Alias alias);

    /**
     * Update alias' target.
     *
     * @param targetId The new target UUID
     * @param aliasId The alias UUID
     * @param aliasVersion The expected version of the alias.
     */
    void updateAlias(UUID targetId, UUID aliasId, long aliasVersion);

}
