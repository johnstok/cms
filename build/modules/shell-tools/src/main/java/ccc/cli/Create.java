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



import static ccc.types.PredefinedResourceNames.*;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.migration.ServiceLookup;
import ccc.rest.RestException;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.extensions.FoldersExt;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.ResourceName;

/**
 * Entry class for the 'create' application.
 */
public final class Create extends CccApp {
    private static final Logger LOG = Logger.getLogger(Create.class);

    private static Options options;
    private static ServiceLookup services;

    private Create() { /* NO-OP */ }


    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        options  = parseOptions(args, Options.class);

        login(options.getUsername(), options.getPassword());

        services =
            new ServiceLookup(options.getApp(), options.getProviderURL());

        createSchemaStructure();

        logout();

        report("Finished in ");
    }

    private static void createSchemaStructure() {
        try {
            final ResourcesExt resourcesExt = services.getResources();
            final FoldersExt foldersExt = services.getFolders();

            final ResourceSummary content = foldersExt.createRoot(CONTENT);
            foldersExt.createRoot(TRASH);

            final ResourceSummary assets = foldersExt.createFolder(
                new FolderDto(content.getId(), new ResourceName(ASSETS)));

            foldersExt.createFolder(
                new FolderDto(assets.getId(), new ResourceName(TEMPLATES)));
            foldersExt.createFolder(
                new FolderDto(assets.getId(), new ResourceName(CSS)));
            foldersExt.createFolder(
                new FolderDto(assets.getId(), new ResourceName(IMAGES)));

            foldersExt.createFolder(
                new FolderDto(content.getId(), new ResourceName(FILES)));
            foldersExt.createFolder(
                new FolderDto(content.getId(), new ResourceName(IMAGES)));
            resourcesExt.createSearch(content.getId(), "search");

            // TODO: Remove. Should set 'publish' root via UI
            resourcesExt.lock(content.getId());
            resourcesExt.publish(content.getId());
            resourcesExt.unlock(content.getId());
            resourcesExt.lock(assets.getId());
            resourcesExt.publish(assets.getId());
            resourcesExt.unlock(assets.getId());

            LOG.info("Created default folder structure.");
        } catch (final RestException e) {
            LOG.error("Failed to create app.", e);
        }
    }

    /**
     * Options for the default layout tool.
     *
     * @author Civic Computing Ltd.
     */
    static class Options {
        @Option(
            name="-u", required=true, usage="Username for connecting to CCC.")
        private String _username;

        @Option(
            name="-p", required=true, usage="Password for connecting to CCC.")
        private String _password;

        @Option(
            name="-a", required=true, usage="App name.")
        private String _app;

        @Option(
            name="-jn",
            required=false,
            usage="optional JNDI provider URL, defaults to localhost")
            private String _providerURL;


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
            return _password;
        }


        /**
         * Accessor.
         *
         * @return Returns the app.
         */
        String getApp() {
            return _app;
        }


        /**
         * Accessor.
         *
         * @return Returns the JNDI provider URL.
         */
        String getProviderURL() {
            return _providerURL;
        }
    }
}
