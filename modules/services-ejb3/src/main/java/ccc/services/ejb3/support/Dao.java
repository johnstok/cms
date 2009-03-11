/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3.support;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.domain.Entity;
import ccc.domain.VersionedEntity;


/**
 * Core DAO API.
 *
 * @author Civic Computing Ltd.
 */
public interface Dao {

    /** NAME : String. */
    String NAME = "Dao";

    /**
     * Find the object with the specified type and id.
     *
     * @param <T> The type of the object.
     * @param type A class instance representing the object's type.
     * @param id The id of the object.
     * @return The object with the specified id.
     */
    <T extends Entity> T find(Class<T> type, UUID id);

    <T extends VersionedEntity> T find(Class<T> type, UUID id, long version);

    <T> List<T> list(String queryName, Class<T> resultType, Object... params);

    <T> Collection<T> uniquify(String queryName, Class<T> resultType, Object... params);

    <T> T find(String queryName, Class<T> resultType, Object... params);

    <T> boolean exists(String queryName, Class<T> resultType, Object... params);

    void create(Entity entity);

}
