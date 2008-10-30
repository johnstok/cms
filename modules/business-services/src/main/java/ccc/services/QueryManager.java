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

import java.util.List;

import ccc.commons.Maybe;
import ccc.domain.Folder;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;


/**
 * Query methods to find and list domain objects.
 *
 * @author Civic Computing Ltd.
 */
interface QueryManager {

    /**
     * Find a setting given its name.
     *
     * @param name The name of the setting to find.
     * @return The setting with the specified name.
     */
    Maybe<Setting> findSetting(final Name name);

    /**
     * Retrieve the folder that represents the root of all stored content.
     *
     * @return The root folder, wrapped in a {@link Maybe}.
     */
    Maybe<Folder> findContentRoot();

    /**
     * Retrieve the folder that represents the root of all stored content.
     *
     * @return The root folder, wrapped in a {@link Maybe}.
     */
    Maybe<Folder> findAssetsRoot();

    /**
     * Execute a named query that returns multiple results.
     *
     * @param queryName
     * @param resultType
     * @param params
     * @param <T>
     * @return
     */
    <T> List<T> list(String queryName, Class<T> resultType, Object... params);
}
