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

import static ccc.api.types.HttpStatusCode.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
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
import ccc.api.ServiceLocator;
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
import ccc.api.exceptions.RestException;
import ccc.api.jaxrs.providers.RestExceptionMapper;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.api.types.Username;
import ccc.client.http.ProxyServiceLocator;
import ccc.plugins.s11n.json.JsonImpl;


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

    private ServiceLocator _sl;

    private final String _hostUrl       = "http://localhost:8080/cc7";
    private final String _secure        = _hostUrl+"/ccc/api/secure";
    private final String _public        = _hostUrl+"/ccc/api/public";
    private final String _createFileUrl = _hostUrl+"/ccc/upload";
    private final String _updateFileUrl = _hostUrl+"/ccc/update_file";
    private final String _previewUrl    = _hostUrl+"/ccc/preview";

    private HttpClient _http;


    /**
     * Accessor.
     *
     * @return Returns the URL to the public API.
     */
    protected String getPublicApiURL() {
        return _public;
    }


    /**
     * Accessor.
     *
     * @return Returns the HTTP client.
     */
    protected HttpClient getHttpClient() {
        return _http;
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
     * Create a template.
     *
     * @param parent The parent folder for the template.
     *
     * @return The template's summary.
     *
     * @throws RestException If the call fails on the server.
     */
    protected ResourceSummary dummyTemplate(final ResourceSummary parent)
    throws RestException {
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
     *
     * @throws RestException If creation of the page fails.
     */
    protected ResourceSummary tempPage(final UUID parentFolder,
                                       final UUID template)
    throws RestException {
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
     *
     * @throws RestException If creation fails on the server.
     */
    protected ResourceSummary tempFolder() throws RestException {
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content = getCommands().resourceForPath("");
        return getFolders().createFolder(
            new FolderDto(content.getId(), new ResourceName(fName)));
    }


    /**
     * Create an alias for testing.
     *
     * @return The alias' summary.
     *
     * @throws RestException If creation fails on the server.
     */
    protected ResourceSummary tempAlias() throws RestException {
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
     *
     * @throws RestException If creation fails on the server.
     */
    protected UserDto tempUser() throws RestException {

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
     * Update an existing file to be a text file with the specified text.
     * TODO: Merge into ccc.migration.FileUploader class.
     *
     * @param fText The text for the file.
     * @param rs The summary for the file to update.
     *
     * @throws RestException If the operation fails on the server.
     * @throws IOException If the operation fails sending the data.
     *
     * @return The response from the server, as a string.
     */
    protected String updateTextFile(final String fText,
                                    final ResourceSummary rs)
    throws IOException, RestException {
        final PostMethod postMethod = new PostMethod(_updateFileUrl);
        final Part[] parts = {
            new StringPart("id", rs.getId().toString()),
            new FilePart(
                "file",
                new ByteArrayPartSource(
                    rs.getName(), fText.getBytes(Charset.forName("UTF-8"))),
                    "text/plain",
            "UTF-8")
        };
        postMethod.setRequestEntity(
            new MultipartRequestEntity(parts, postMethod.getParams())
        );

        _http.executeMethod(postMethod);

        final int status = postMethod.getStatusCode();
        final String body = postMethod.getResponseBodyAsString();
        if (OK==status) {
            return body;
        }
        throw new RestExceptionMapper().fromResponse(status, body);
    }


    /**
     * Get the contents of the specified resource.
     * TODO: Merge into client library.
     *
     * @param rs The resource to read.
     * @param wc Should the working copy be read?
     *
     * @return The contents of the resource as a string.
     *
     * @throws IOException  If reading from the server fails.
     */
    protected String previewContent(final ResourceSummary rs, final boolean wc)
    throws IOException {
        final GetMethod get =
            new GetMethod(
                _previewUrl
                + rs.getAbsolutePath()
                + ((wc) ? "?wc=" : ""));
        try {
            _http.executeMethod(get);
            final int status = get.getStatusCode();
            if (OK==status) {
                return get.getResponseBodyAsString();
            }
            throw new RuntimeException(
                status+": "+get.getResponseBodyAsString());
        } finally {
            get.releaseConnection();
        }
    }


    /**
     * Create a file for testing.
     * TODO: Merge into ccc.migration.FileUploader class.
     *
     * @param fName The file's name.
     * @param fText The file's contents.
     * @param filesFolder The parent folder for the file.
     *
     * @return The summary of the newly created file.
     *
     * @throws IOException If creation fails on the client.
     */
    protected ResourceSummary createFile(final String fName,
                                         final String fText,
                                         final ResourceSummary filesFolder)
    throws IOException {

        final PostMethod postMethod = new PostMethod(_createFileUrl);

        try {
            final Part[] parts = {
                new StringPart("path", filesFolder.getId().toString()),
                new StringPart("fileName", fName),
                new FilePart(
                    "file",
                    new ByteArrayPartSource(
                        fName, fText.getBytes(Charset.forName("UTF-8"))),
                    "text/plain",
                    "UTF-8")
            };
            postMethod.setRequestEntity(
                new MultipartRequestEntity(parts, postMethod.getParams())
            );

            final int status = _http.executeMethod(postMethod);
            final String body = postMethod.getResponseBodyAsString();

            if (OK==status) {
                return new ResourceSummary(new JsonImpl(body));
            }
            throw new RestExceptionMapper().fromResponse(status, body);

        } finally {
            postMethod.releaseConnection();
        }
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
        _http = new HttpClient();
        _sl   = new ProxyServiceLocator(_http, _hostUrl);
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
        _sl   = null;
        _http = null;
    }
}
