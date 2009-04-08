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
package ccc.cli;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.services.ActionDao;
import ccc.services.Scheduler;
import ccc.services.SearchEngine;
import ccc.services.api.Commands;
import ccc.services.api.Queries;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
class ServiceLookup {
    private final Registry _registry = new JNDI();

    private String _appName;

    ServiceLookup(final String appName) {
        _appName = appName;
    }

    Commands lookupCommands() {
        return _registry.<Commands>get(jndiPath(Commands.NAME));
    }

    Queries lookupQueries() {
        return _registry.<Queries>get(jndiPath(Queries.NAME));
    }

    Scheduler lookupActionScheduler() {
        return _registry.<Scheduler>get(jndiPath(ActionDao.NAME));
    }

    Scheduler lookupSearchScheduler() {
        return _registry.<Scheduler>get(jndiPath(SearchEngine.NAME));
    }

    private String jndiPath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }
}
