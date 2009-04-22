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
package ccc.services;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.services.api.Commands;
import ccc.services.api.Queries;


/**
 * A class to perform application name specific Registry lookups.
 *
 * @author Civic Computing Ltd.
 */
public class ServiceLookup {
    private final Registry _registry;
    private final String _appName;

    public ServiceLookup(final String appName, final Registry registry) {
        _appName = appName;
        _registry = registry;
    }

    public ServiceLookup(final String appName) {
        this(appName, new JNDI());
    }

    public Commands lookupCommands() {
        return _registry.<Commands>get(jndiPath(Commands.NAME));
    }

    public Queries lookupQueries() {
        return _registry.<Queries>get(jndiPath(Queries.NAME));
    }

    public Scheduler lookupActionScheduler() {
        return _registry.<Scheduler>get(jndiPath(ActionDao.NAME));
    }

    public Scheduler lookupSearchScheduler() {
        return _registry.<Scheduler>get(jndiPath(SearchEngine.NAME));
    }

    public DataManager dataManager() {
        return _registry.<DataManager>get(localPath(DataManager.NAME));
    }

    public UserManager localUserManager() {
        return _registry.<UserManager>get(localPath(UserManager.NAME));
    }

    public SearchEngine localSearchEngine() {
        return _registry.<SearchEngine>get(localPath(SearchEngine.NAME));
    }

    private String localPath(final String serviceName) {
        return _appName+"/"+serviceName+"/local";
    }

    private String jndiPath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }
}
