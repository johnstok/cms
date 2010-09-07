/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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

package ccc.tests.acceptance;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import ccc.acceptance.client.BundleWrapper;
import ccc.acceptance.client.HttpClientRequestExecutor;
import ccc.acceptance.client.ServerTextParser;
import ccc.acceptance.client.WindowStub;
import ccc.api.core.API;
import ccc.api.core.ActionSummary;
import ccc.api.core.Actions;
import ccc.api.core.Alias;
import ccc.api.core.Aliases;
import ccc.api.core.Comments;
import ccc.api.core.Files;
import ccc.api.core.Folder;
import ccc.api.core.Folders;
import ccc.api.core.Group;
import ccc.api.core.Groups;
import ccc.api.core.Page;
import ccc.api.core.PagedCollection;
import ccc.api.core.Pages;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Resources;
import ccc.api.core.SearchEngine;
import ccc.api.core.Security;
import ccc.api.core.Template;
import ccc.api.core.Templates;
import ccc.api.core.User;
import ccc.api.core.Users;
import ccc.api.http.ProxyServiceLocator;
import ccc.api.http.SiteBrowser;
import ccc.api.types.MimeType;
import ccc.api.types.NormalisingEncoder;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.api.types.SortOrder;
import ccc.api.types.Username;
import ccc.client.core.CoreEvents;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.RequestExecutor;
import ccc.client.events.Event;
import ccc.client.events.EventHandler;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;
import ccc.client.validation.AbstractValidations;
import ccc.commons.Testing;


/**
 * Abstract helper class for acceptance tests.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractAcceptanceTest
    extends
        TestCase {

    private static final WindowStub WINDOW;
    private static final int PAGE_SIZE = 20;
    private static final Logger LOG =
        Logger.getLogger(AbstractAcceptanceTest.class);

    private ProxyServiceLocator _sl;

    private final String _hostUrl       = "http://localhost";

    static {
        final API api = new API();
        api.addLink(API.ALIASES, "/secure/aliases");
        api.addLink(API.USERS, "/secure/users");
        api.addLink(API.PAGES, "/secure/pages");
        api.addLink(API.FOLDERS, "/secure/folders");
        api.addLink(API.FILES, "/secure/files");
        InternalServices.API = api;

        I18n.USER_ACTIONS =
            BundleWrapper.wrap(
                ActionNameConstants.class,
                "ccc.client.gwt.i18n.GWTActionNameConstants");
        Testing.stub(ActionNameConstants.class);
        I18n.UI_CONSTANTS =
            BundleWrapper.wrap(
                UIConstants.class,
                "ccc.client.gwt.i18n.GWTUIConstants");
        I18n.UI_MESSAGES =
            BundleWrapper.wrap(
                UIMessages.class,
                "ccc.client.gwt.i18n.GWTUIMessages");
        WINDOW = new WindowStub();
        InternalServices.WINDOW = WINDOW;
        InternalServices.ENCODER = new NormalisingEncoder();
        InternalServices.PARSER = new ServerTextParser();

        InternalServices.CORE_BUS.registerHandler(
            new EventHandler<CoreEvents>() {
                @Override
                public void handle(final Event<CoreEvents> event) {
                    switch (event.getType()) {
                        case ERROR:
                            throw new RuntimeException(
                                "Test failed.",
                                event.<Throwable>getProperty("exception"));
                        default:
                            break;
                    }
                }
            });

        InternalServices.VALIDATOR = new AbstractValidations() {
            @Override
            public String notValidXML(final String definition) {
                throw new UnsupportedOperationException("Method not implemented.");
            }
        };
    }


    /**
     * Accessor.
     *
     * @return Returns the commands.
     */
    protected Resources getCommands() {
        return _sl.getResources();
    }


    /**
     * Accessor.
     *
     * @return Returns the comments.
     */
    protected Comments getComments() {
        return _sl.getComments();
    }


    /**
     * Accessor.
     *
     * @return Returns the users.
     */
    protected Users getUsers() {
        return _sl.getUsers();
    }


    /**
     * Accessor.
     *
     * @return Returns the actions.
     */
    protected Actions getActions() {
        return _sl.getActions();
    }


    /**
     * Accessor.
     *
     * @return Returns the folders.
     */
    protected Folders getFolders() {
        return _sl.getFolders();
    }


    /**
     * Accessor.
     *
     * @return Returns the pages.
     */
    protected Pages getPages() {
        return _sl.getPages();
    }


    /**
     * Accessor.
     *
     * @return Returns the groups.
     */
    protected Groups getGroups() {
        return _sl.getGroups();
    }


    /**
     * Accessor.
     *
     * @return Returns the security.
     */
    protected Security getSecurity() {
        return _sl.getSecurity();
    }


    /**
     * Accessor.
     *
     * @return Returns the templates.
     */
    protected Templates getTemplates() {
        return _sl.getTemplates();
    }


    /**
     * Accessor.
     *
     * @return Returns the files.
     */
    protected Files getFiles() {
        return _sl.getFiles();
    }


    /**
     * Accessor.
     *
     * @return Returns the aliases.
     */
    protected Aliases getAliases() {
        return _sl.getAliases();
    }


    /**
     * Accessor.
     *
     * @return Returns the search engine.
     */
    protected SearchEngine getSearch() {
        return _sl.getSearch();
    }


    /**
     * Accessor.
     *
     * @return Returns a site browser.
     */
    protected SiteBrowser getBrowser() {
        return _sl.getBrowser();
    }


    /**
     * Creates a HTTP executor.
     *
     * @return The executor.
     */
    protected final RequestExecutor createExecutor() {
        return
            new HttpClientRequestExecutor(
                _sl.getHttpClient(),
                _hostUrl+"/ccc/");
    }


    /**
     * Accessor.
     *
     * @return The client window for testing.
     */
    protected final WindowStub getWindow() {
        return WINDOW;
    }


    /**
     * Create a template.
     *
     * @param parent The parent folder for the template.
     *
     * @return The template's summary.
     */
    protected ResourceSummary dummyTemplate(final ResourceSummary parent) {
        final String templateName = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(templateName));
        t.setParent(parent.getId());
        t.setDescription(templateName);
        t.setTitle(templateName);
        t.setBody("body");
        t.setDefinition("<fields/>");
        t.setMimeType(MimeType.HTML);

        return getTemplates().create(t);
    }

    /**
     * Create a page for testing.
     *
     * @param parentFolder The parent folder for the page.
     * @param template The page's template.
     *
     * @return The summary for the page.
     */
    protected ResourceSummary tempPage(final UUID parentFolder,
                                       final UUID template) {
        final String name = UUID.randomUUID().toString();
        final Page page = new Page(parentFolder,
                                        name,
                                        template,
                                        "title",
                                        "",
                                        true);
        page.setParagraphs(
            Collections.singleton(
                Paragraph.fromText("content", "test content")));
        return getPages().create(page);
    }

    /**
     * Create a folder for testing.
     *
     * @return The folder's summary.
     */
    protected ResourceSummary tempFolder() {
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content = getCommands().resourceForPath("");
        return getFolders().create(
            new Folder(content.getId(), new ResourceName(fName)));
    }


    /**
     * Create an alias for testing.
     *
     * @return The alias' summary.
     */
    protected ResourceSummary tempAlias() {
        final String name = UUID.randomUUID().toString();
        final ResourceSummary folder = getCommands().resourceForPath("");
        final Alias alias =
            new Alias(
                folder.getId(), new ResourceName(name), folder.getId());
        return getAliases().create(alias);
    }


    /**
     * Create a user for testing.
     *
     * @return The user DTO.
     */
    protected User tempUser() {

        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";
        final List<Group> groups =
            getGroups().query("CONTENT_CREATOR",
                1,
                PAGE_SIZE).getElements();
        final Group contentCreator = groups.iterator().next();

        // Create the user
        final User u =
            new User()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setGroups(Collections.singleton(contentCreator.getId()))
                .setMetadata(Collections.singletonMap("key", "value"))
                .setPassword("Testtest00-");

        return getUsers().create(u);
    }


    /**
     * Create a dummy username for use during testing.
     *
     * @return A new unique username.
     */
    protected Username dummyUsername() {
        return new Username(UUID.randomUUID().toString().substring(0, 8));
    }


    /**
     * Create a short unique ID.
     *
     * @return The UID as a string.
     */
    protected String uid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _sl   = new ProxyServiceLocator(_hostUrl);
        InternalServices.EXECUTOR = createExecutor();
        getSecurity().login("migration", "migration");
        InternalServices.ACTIONS = createActions();
        InternalServices.GROUPS = createGroups();
    }


    private PagedCollection<Group> createGroups() {
        return getGroups().query(null, 1, 1);
    }


    private PagedCollection<ActionSummary> createActions() {
        return getActions().listPendingActions(null, SortOrder.ASC, 1, 1);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        try {
            getSecurity().logout();
        } catch (final Exception e) {
            LOG.warn("Logout failed.", e);
        }
        _sl = null;
        InternalServices.EXECUTOR = null;
    }
}
