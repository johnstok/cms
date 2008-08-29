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
    public Setting findSetting(final Name name);
}
