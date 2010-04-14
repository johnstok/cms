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
import java.util.HashSet;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import ccc.api.Actions;
import ccc.api.Aliases;
import ccc.api.Comments;
import ccc.api.Files;
import ccc.api.Folders;
import ccc.api.Groups;
import ccc.api.Pages;
import ccc.api.Resources;
import ccc.api.Security;
import ccc.api.Templates;
import ccc.api.Users;
import ccc.api.dto.AliasDto;
import ccc.api.dto.FolderDto;
import ccc.api.dto.GroupDto;
import ccc.api.dto.PageDelta;
import ccc.api.dto.PageDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.TemplateDelta;
import ccc.api.dto.TemplateDto;
import ccc.api.dto.UserDto;
import ccc.api.http.ProxyServiceLocator;
import ccc.api.http.SiteBrowser;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.api.types.Username;


/**
 * Abstract helper class for acceptance tests.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractAcceptanceTest
    extends
        TestCase {
    private static final Logger LOG =
        Logger.getLogger(AbstractAcceptanceTest.class);

    private ProxyServiceLocator _sl;

    private final String _hostUrl       = "http://localhost:8080/cc7";


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
     * @return Returns a site browser.
     */
    protected SiteBrowser getBrowser() {
        return _sl.getBrowser();
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
        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);
        final ResourceSummary ts =
            getTemplates().createTemplate(
                new TemplateDto(
                    parent.getId(),
                    newTemplate,
                    templateName,
                    templateName,
                    templateName));
        return ts;
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
        final PageDelta delta = new PageDelta(new HashSet<Paragraph>());
        final PageDto page = new PageDto(parentFolder,
                                        delta,
                                        name,
                                        template,
                                        "title",
                                        "",
                                        true);
        return getPages().createPage(page);
    }

    /**
     * Create a folder for testing.
     *
     * @return The folder's summary.
     */
    protected ResourceSummary tempFolder() {
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content = getCommands().resourceForPath("");
        return getFolders().createFolder(
            new FolderDto(content.getId(), new ResourceName(fName)));
    }


    /**
     * Create an alias for testing.
     *
     * @return The alias' summary.
     */
    protected ResourceSummary tempAlias() {
        final String name = UUID.randomUUID().toString();
        final ResourceSummary folder = getCommands().resourceForPath("");
        final AliasDto alias =
            new AliasDto(
                folder.getId(), new ResourceName(name), folder.getId());
        return getAliases().createAlias(alias);
    }


    /**
     * Create a user for testing.
     *
     * @return The user DTO.
     */
    protected UserDto tempUser() {

        final Username username = dummyUsername();
        final String email = username+"@abc.def";
        final String name = "testuser";
        final GroupDto contentCreator =
            getGroups().list("CONTENT_CREATOR").iterator().next();

        // Create the user
        final UserDto u =
            new UserDto()
                .setEmail(email)
                .setUsername(username)
                .setName(name)
                .setRoles(Collections.singleton(contentCreator.getId()))
                .setMetadata(Collections.singletonMap("key", "value"))
                .setPassword("Testtest00-");

        return getUsers().createUser(u);
    }


    /**
     * Create a dummy username for use during testing.
     *
     * @return A new unique username.
     */
    protected Username dummyUsername() {
        return new Username(UUID.randomUUID().toString().substring(0, 8));
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _sl   = new ProxyServiceLocator(_hostUrl);
        getSecurity().login("migration", "migration");
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
    }
}
