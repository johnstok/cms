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
import ccc.domain.Scheduler;
import ccc.rest.Actions;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.PagesExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.search.SearchEngine;


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
    public ResourcesExt lookupCommands() {
        return _registry.<ResourcesExt>get(jndiPath(ResourcesExt.NAME));
    }

    /**
     * Look up the page commands API.
     *
     * @return A page commands instance.
     */
    public PagesExt lookupPageCommands() {
        return _registry.<PagesExt>get(jndiPath(PagesExt.NAME));
    }

    /**
     * Look up the folder commands API.
     *
     * @return A folder commands instance.
     */
    public FoldersExt lookupFolderCommands() {
        return _registry.<FoldersExt>get(jndiPath(FoldersExt.NAME));
    }

    /**
     * Look up the user commands API.
     *
     * @return A user commands instance.
     */
    public Users lookupUserCommands() {
        return _registry.<Users>get(jndiPath(Users.NAME));
    }

    /**
     * Look up the action scheduler.
     *
     * @return An action scheduler.
     */
    public Scheduler lookupActionScheduler() {
        return _registry.<Scheduler>get(jndiPath(Actions.NAME));
    }

    /**
     * Look up the search scheduler.
     *
     * @return A search scheduler.
     */
    public Scheduler lookupSearchScheduler() {
        return _registry.<Scheduler>get(jndiPath(SearchEngine.NAME));
    }

    /**
     * Look up an implementation of the templates API.
     *
     * @return A templates implementation.
     */
    public Templates lookupTemplates() {
        return _registry.<Templates>get(jndiPath(Templates.NAME));
    }

    private String jndiPath(final String serviceName) {
        return _appName+"/"+serviceName+"/remote";
    }
}