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
package ccc.migration;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.persistence.ActionDao;
import ccc.persistence.FolderCommands;
import ccc.persistence.PageCommands;
import ccc.persistence.UserCommands;
import ccc.rest.Queries;
import ccc.search.SearchEngine;
import ccc.services.Commands;
import ccc.services.Scheduler;


/**
 * A class to perform application name specific Registry lookups.
 *
 * @author Civic Computing Ltd.
 */
public class ServiceLookup {
    private final Registry _registry;
    private final String _appName;

    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param registry The registry to use for look up.
     */
    public ServiceLookup(final String appName, final Registry registry) {
        _appName = appName;
        _registry = registry;
    }

    /**
     * Constructor.
     *
     * @param appName The name of the application.
     */
    public ServiceLookup(final String appName) {
        this(appName, new JNDI());
    }

    /**
     * Constructor.
     *
     * @param appName The name of the application.
     * @param providerUrl The java naming provider URL
     */
    public ServiceLookup(final String appName, final String providerUrl) {
        final Registry registry = new JNDI(providerUrl);
        _appName = appName;
        _registry = registry;
    }

    /**
     * Look up the commands API.
     *
     * @return A commands instance.
     */
    public Commands lookupCommands() {
        return _registry.<Commands>get(jndiPath(Commands.NAME));
    }

    /**
     * Look up the page commands API.
     *
     * @return A page commands instance.
     */
    public PageCommands lookupPageCommands() {
        return _registry.<PageCommands>get(jndiPath(PageCommands.NAME));
    }

    /**
     * Look up the folder commands API.
     *
     * @return A folder commands instance.
     */
    public FolderCommands lookupFolderCommands() {
        return _registry.<FolderCommands>get(jndiPath(FolderCommands.NAME));
    }

    /**
     * Look up the user commands API.
     *
     * @return A user commands instance.
     */
    public UserCommands lookupUserCommands() {
        return _registry.<UserCommands>get(jndiPath(UserCommands.NAME));
    }

    /**
     * Look up the query API.
     *
     * @return A query instance.
     */
    public Queries lookupQueries() {
        return _registry.<Queries>get(jndiPath(Queries.NAME));
    }

    /**
     * Look up the action scheduler.
     *
     * @return An action scheduler.
     */
    public Scheduler lookupActionScheduler() {
        return _registry.<Scheduler>get(jndiPath(ActionDao.NAME));
    }

    /**
     * Look up the search scheduler.
     *
     * @return A search scheduler.
     */
    public Scheduler lookupSearchScheduler() {
        return _registry.<Scheduler>get(jndiPath(SearchEngine.NAME));
    }

    private String jndiPath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }
}
