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



import static ccc.api.types.PredefinedResourceNames.ASSETS;
import static ccc.api.types.PredefinedResourceNames.CONTENT;
import static ccc.api.types.PredefinedResourceNames.CSS;
import static ccc.api.types.PredefinedResourceNames.FILES;
import static ccc.api.types.PredefinedResourceNames.IMAGES;
import static ccc.api.types.PredefinedResourceNames.TEMPLATES;
import static ccc.api.types.PredefinedResourceNames.TRASH;

import java.util.Collections;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.core.Folder;
import ccc.api.core.Folders;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Resources;
import ccc.api.exceptions.CCException;
import ccc.api.http.ProxyServiceLocator;
import ccc.api.types.ResourceName;

/**
 * Entry class for the 'create' application.
 */
public final class Create extends CccApp {
    private static final Logger LOG = Logger.getLogger(Create.class);

    /**
     * Entry point for this application.
     *
     * @param args String array of application arguments.
     */
    public static void main(final String[] args) {
        LOG.info("Starting.");

        Create c  = parseOptions(args, Create.class);



        c.createSchemaStructure();

        report("Finished in ");
    }

    /**
     * Create schema structure.
     *
     */
    public void createSchemaStructure() {
        final ProxyServiceLocator sl =
            new ProxyServiceLocator(getUploadUrl());

        if (!sl.getSecurity().login(getUsername(), getPassword())) {
            throw new RuntimeException("Failed to authenticate.");
        }

        try {

            final Folders folders = sl.getFolders();
            final Resources resources = sl.getResources();

            final ResourceSummary content = folders.createRoot(CONTENT);
            folders.createRoot(TRASH);

            final ResourceSummary assets = folders.create(
                new Folder(content.getId(), new ResourceName(ASSETS)));

            folders.create(
                new Folder(assets.getId(), new ResourceName(TEMPLATES)));
            folders.create(
                new Folder(assets.getId(), new ResourceName(CSS)));
            folders.create(
                new Folder(assets.getId(), new ResourceName(IMAGES)));

            folders.create(
                new Folder(content.getId(), new ResourceName(FILES)));
            folders.create(
                new Folder(content.getId(), new ResourceName(IMAGES)));
            resources.createSearch(content.getId(), "search");

            resources.lock(content.getId());
            resources.publish(content.getId());
            final Resource cMetadata = new Resource();
            cMetadata.setTitle(content.getTitle());
            cMetadata.setDescription(content.getDescription());
            cMetadata.setTags(new HashSet<String>());
            cMetadata.setMetadata(
                Collections.singletonMap("searchable", "true"));
            resources.updateMetadata(content.getId(), cMetadata);
            resources.unlock(content.getId());

            resources.lock(assets.getId());
            resources.publish(assets.getId());
            final Resource aMetadata = new Resource();
            aMetadata.setTitle(assets.getTitle());
            aMetadata.setDescription(assets.getDescription());
            aMetadata.setTags(new HashSet<String>());
            aMetadata.setMetadata(
                Collections.singletonMap("searchable", "false"));
            resources.updateMetadata(assets.getId(), aMetadata);
            resources.unlock(assets.getId());

            LOG.info("Created default folder structure.");
        } catch (final CCException e) {
            LOG.error("Failed to create app.", e);
        }
    }

    @Option(
        name="-u", required=true, usage="Username for connecting to CCC.")
        private String _username;

    @Option(
        name="-p", required=false, usage="Password for connecting to CCC.")
        private String _password;

    @Option(
        name="-o", required=true, usage="The URL for file upload.")
        private String _uploadUrl;


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
     * @return Returns the uploadUrl.
     */
    public String getUploadUrl() {
        return _uploadUrl;
    }
}
