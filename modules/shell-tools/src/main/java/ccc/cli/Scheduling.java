/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli;


import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.core.Actions;
import ccc.api.core.ServiceLocator;
import ccc.api.http.ProxyServiceLocator;

/**
 * Command line management of Action executor.
 */
public final class Scheduling extends CccApp {
    private static final Logger LOG = Logger.getLogger(Scheduling.class);

    private static ServiceLocator services;


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        final Scheduling s = parseOptions(args, Scheduling.class);

        try {
            s.run();
        } catch (final RuntimeException e) {
            LOG.error("Error performing command.", e);
            System.exit(1);
        }
    }


    /**
     * Execute scheduling command.
     *
     */
    public void run() {

        services = new ProxyServiceLocator(getBaseUrl());

        if (!services.getSecurity().login(getUsername(), getPassword())) {
            throw new RuntimeException("Failed to authenticate.");
        }

        final Actions s = services.getActions();

        if ("start".equals(getAction())) {
            s.start();
            LOG.info("Started.");

        } else if ("stop".equals(getAction())) {
            s.stop();
            LOG.info("Stopped.");

        } else if ("running".equals(getAction())) {
            final boolean running = s.isRunning();
            LOG.info("Running: "+running+".");

        } else {
            throw new RuntimeException("Invalid command: "+getAction());
        }

        services.getSecurity().logout();

        report("Finished in ");
    }


    @Option(
        name="-u", required=true, usage="Username for connecting to CCC.")
    private String _username;

    @Option(
        name="-p", required=false, usage="Password for connecting to CCC.")
    private String _password;

    @Option(
        name="-b", required=true, usage="Base URL for the application.")
    private String _baseUrl;

    @Option(
        name="-c", required=true, usage="Action to perform.")
    private String _action;


    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    String getUsername() {
        return _username;
    }


    /**
     * Accessor.
     *
     * @return Returns the password.
     */
    String getPassword() {
        if (_password == null) {
            return readConsolePassword("Password for connecting to CCC");
        }
        return _password;
    }


    /**
     * Accessor.
     *
     * @return Returns the base URL for the app.
     */
    String getBaseUrl() {
        return _baseUrl;
    }


    /**
     * Accessor.
     *
     * @return Returns the action.
     */
    String getAction() {
        return _action;
    }


    /**
     * Mutator.
     *
     * @param username The username to set.
     */
    public void setUsername(final String username) {
        _username = username;
    }


    /**
     * Mutator.
     *
     * @param password The password to set.
     */
    public void setPassword(final String password) {
        _password = password;
    }


    /**
     * Mutator.
     *
     * @param baseUrl The baseUrl to set.
     */
    public void setBaseUrl(final String baseUrl) {
        _baseUrl = baseUrl;
    }


    /**
     * Mutator.
     *
     * @param action The action to set.
     */
    public void setAction(final String action) {
        _action = action;
    }
}
