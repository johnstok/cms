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

import ccc.commons.Maybe;
import ccc.domain.Folder;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;


/**
 * Query methods to find and list domain objects.
 *
 * @author Civic Computing Ltd.
 */
public interface QueryManager {

    /**
     * Find a setting given its name.
     *
     * @param name The name of the setting to find.
     * @return The setting with the specified name.
     */
    Maybe<Setting> findSetting(final Name name);

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    Maybe<Folder> findContentRoot();

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    Maybe<Folder> findAssetsRoot();

//    /**
//     * Look up the root folder for the content hierarchy.
//     *
//     * @param name The name of the resource.
//     * @return The folder with the specified name.
//     */
//    Maybe<Folder> lookupRoot(final ResourceName name);
}
