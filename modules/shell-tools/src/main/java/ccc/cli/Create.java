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



import static ccc.api.types.PredefinedResourceNames.*;

import java.util.Collections;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.Option;

import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.exceptions.CCException;
import ccc.api.http.ProxyServiceLocator;
import ccc.api.synchronous.Folders;
import ccc.api.synchronous.Pages;
import ccc.api.synchronous.Resources;
import ccc.api.synchronous.Templates;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
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

        final Create c  = parseOptions(args, Create.class);



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
            final Templates templates = sl.getTemplates();
            final Pages pages = sl.getPages();

            final Folder content = folders.createRoot(CONTENT);
            folders.createRoot(TRASH);

            final Folder assets = folders.create(
                new Folder(content.getId(), new ResourceName(ASSETS)));

            final Folder tFolder = folders.create(
                new Folder(assets.getId(), new ResourceName(TEMPLATES)));
            folders.create(
                new Folder(assets.getId(), new ResourceName(CSS)));
            folders.create(
                new Folder(assets.getId(), new ResourceName(IMAGES)));

            folders.create(
                new Folder(content.getId(), new ResourceName(FILES)));
            folders.create(
                new Folder(content.getId(), new ResourceName(IMAGES)));

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

            final Template ts = createDefaultTemplate(tFolder, templates);

            createDefaultPages(resources, pages, content, ts);

            LOG.info("Created default folder structure.");
        } catch (final CCException e) {
            LOG.error("Failed to create app.", e);
        }
    }


    private Template createDefaultTemplate(final Folder tFolder,
                                           final Templates templates) {

        final Template t = new Template();
        t.setBody(
            "<html>\n"
                + "    <head><title>$resource.getTitle()</title></head>\n"
                + "    <body>"
                + "$resource.getParagraph('content').getText()"
                + "</body>\n"
                + "</html>");
        t.setDefinition(
            "<fields>\n"
                + "    <field name=\"content\" type=\"html\" />\n"
                + "</fields>");
        t.setName(new ResourceName("simple"));
        t.setTitle("Simple template");
        t.setDescription("Simple template with a single HTML field.");
        t.setMimeType(MimeType.HTML);
        t.setParent(tFolder.getId());
        return templates.create(t);
    }


    private void createDefaultPages(final Resources resources,
                                    final Pages pages,
                                    final Folder content,
                                    final Template ts) {

        final Page p = new Page();
        p.setParagraphs(
            Collections.singleton(
                Paragraph.fromText(
                    "content",
                    "<br><br><center><h1>"
                        + "Welcome to Content Control!</h1></center>")));
        p.setName(new ResourceName("welcome"));
        p.setTemplate(ts.getId());
        p.setTitle("Welcome");
        p.setParent(content.getId());
        final Page ps = pages.create(p);
        resources.lock(ps.getId());
        resources.publish(ps.getId());
        resources.unlock(ps.getId());

        final Page p404 = new Page();
        p404.setParagraphs(
            Collections.singleton(
                Paragraph.fromText(
                    "content",
                    "<br><br><center><h1>"
                        + "404</h1></center>")));
        p404.setName(new ResourceName("notfound"));
        p404.setTemplate(ts.getId());
        p404.setTitle("Not found");
        p404.setParent(content.getId());
        final Page ps404 = pages.create(p404);
        resources.lock(ps404.getId());
        resources.publish(ps404.getId());
        resources.unlock(ps404.getId());
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
