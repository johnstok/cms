/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import ccc.api.synchronous.Resources;
import ccc.api.types.DBC;
import ccc.commons.Registry;
import ccc.rest.extensions.ResourcesExt;
import ccc.services.Migration;


/**
 * Extended service locator for remoting.
 * FIXME: Extract common code - none of the standard services can be accessed
 *  via JNDI.
 *
 * @author Civic Computing Ltd.
 */
public class MigrationServiceLocator {

    private final Registry  _registry;
    private final String    _appName;


    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param registry The registry to use for look up.
     */
    public MigrationServiceLocator(final String appName,
                                   final Registry registry) {
        _appName =  DBC.require().notEmpty(appName);
        _registry = DBC.require().notNull(registry);
    }


    private Registry getRegistry() {
        return _registry;
    }


    private String remotePath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }


    public ResourcesExt getResourcesExt() {
        return getRegistry().<ResourcesExt>get(remotePath(Resources.NAME));
    }


    public Migration getMigration() {
        return getRegistry().<Migration>get(remotePath(Migration.NAME));
    }
}
