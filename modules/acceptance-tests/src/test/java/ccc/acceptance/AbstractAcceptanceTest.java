/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.acceptance;

import static ccc.types.HttpStatusCode.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.Security;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.rest.providers.ActionSummaryCollectionReader;
import ccc.rest.providers.ActionSummaryReader;
import ccc.rest.providers.AliasDeltaReader;
import ccc.rest.providers.BooleanProvider;
import ccc.rest.providers.DurationReader;
import ccc.rest.providers.FailureWriter;
import ccc.rest.providers.JsonReader;
import ccc.rest.providers.JsonableWriter;
import ccc.rest.providers.MetadataWriter;
import ccc.rest.providers.PageDeltaReader;
import ccc.rest.providers.ResSummaryReader;
import ccc.rest.providers.ResourceSummaryCollectionReader;
import ccc.rest.providers.RevisionSummaryCollectionReader;
import ccc.rest.providers.StringCollectionWriter;
import ccc.rest.providers.TemplateDeltaReader;
import ccc.rest.providers.UUIDProvider;
import ccc.rest.providers.UserSummaryCollectionReader;
import ccc.rest.providers.UserSummaryReader;
import ccc.serialization.JsonImpl;
import ccc.types.Failure;
import ccc.types.HttpStatusCode;
import ccc.types.MimeType;
import ccc.types.Paragraph;


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

    static {
        final ResteasyProviderFactory pFactory =
            ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(pFactory);

        // Writers
        pFactory.addMessageBodyWriter(JsonableWriter.class);
        pFactory.addMessageBodyWriter(StringCollectionWriter.class);
        pFactory.addMessageBodyWriter(MetadataWriter.class);
        pFactory.addMessageBodyWriter(JsonReader.class);
        pFactory.addMessageBodyWriter(UUIDProvider.class);

        // Readers
        pFactory.addMessageBodyReader(ResourceSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(ResSummaryReader.class);
        pFactory.addMessageBodyReader(DurationReader.class);
        pFactory.addMessageBodyReader(UserSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(UserSummaryReader.class);
        pFactory.addMessageBodyReader(BooleanProvider.class);
        pFactory.addMessageBodyReader(FailureWriter.class);
        pFactory.addMessageBodyReader(StringCollectionWriter.class);
        pFactory.addMessageBodyReader(MetadataWriter.class);
        pFactory.addMessageBodyReader(ActionSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(AliasDeltaReader.class);
        pFactory.addMessageBodyReader(RevisionSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(UUIDProvider.class);
        pFactory.addMessageBodyReader(TemplateDeltaReader.class);
        pFactory.addMessageBodyReader(PageDeltaReader.class);
        pFactory.addMessageBodyReader(ActionSummaryReader.class);

        // String Converters
        pFactory.addStringConverter(UUIDProvider.class);
    }

    private final String _hostUrl       = "http://localhost:81";
    private final String _secure        = _hostUrl+"/api/secure";
    private final String _public        = _hostUrl+"/api/public";
    private final String _createFileUrl = _hostUrl+"/upload";
    private final String _updateFileUrl = _hostUrl+"/update_file";

    private HttpClient _http;
    private Resources _commands;
    private Users _users;
    private Actions _actions;
    private Folders _folders;
    private Pages _pages;
    private Security _security;
    private Templates _templates;
    private Files _files;
    private Aliases _aliases;


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
        return _commands;
    }


    /**
     * Accessor.
     *
     * @return Returns the users.
     */
    protected Users getUsers() {
        return _users;
    }


    /**
     * Accessor.
     *
     * @return Returns the actions.
     */
    protected Actions getActions() {
        return _actions;
    }


    /**
     * Accessor.
     *
     * @return Returns the folders.
     */
    protected Folders getFolders() {
        return _folders;
    }


    /**
     * Accessor.
     *
     * @return Returns the pages.
     */
    protected Pages getPages() {
        return _pages;
    }


    /**
     * Accessor.
     *
     * @return Returns the security.
     */
    protected Security getSecurity() {
        return _security;
    }


    /**
     * Accessor.
     *
     * @return Returns the templates.
     */
    protected Templates getTemplates() {
        return _templates;
    }


    /**
     * Accessor.
     *
     * @return Returns the files.
     */
    protected Files getFiles() {
        return _files;
    }


    /**
     * Accessor.
     *
     * @return Returns the aliases.
     */
    protected Aliases getAliases() {
        return _aliases;
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
            _templates.createTemplate(
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
        return _pages.createPage(page);
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
        final ResourceSummary content = resourceForPath("/content");
        return _folders.createFolder(new FolderDto(content.getId(), fName));
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
        final ResourceSummary folder = resourceForPath("/content");
        final AliasDto alias =
            new AliasDto(folder.getId(), name, folder.getId());
        return _aliases.createAlias(alias);
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

        final String body = postMethod.getResponseBodyAsString();
        if (OK==postMethod.getStatusCode()) {
            return body;
        }
        throw new RestException(new Failure(new JsonImpl(body)));
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
                "http://localhost:81/preview"
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
     * @throws RestException If creation fails on the server.
     */
    protected ResourceSummary createFile(final String fName,
                                         final String fText,
                                         final ResourceSummary filesFolder)
    throws IOException, RestException {

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
            throw new RestException(new Failure(new JsonImpl(body)));

        } finally {
            postMethod.releaseConnection();
        }
    }


    /**
     * Look up the resource for a path.
     * <p><i>This method works around an encoding issue in REST-EASY 1.1</i>.
     *
     * @param path The resource's fully qualified path.
     *
     * @return The corresponding resource summary.
     */
    protected ResourceSummary resourceForPath(final String path) {

        final ClientRequest request =
            new ClientRequest(_secure+"/resources-by-path"+path, _http);

        try {
            final ClientResponse<ResourceSummary> response =
                request.get(ResourceSummary.class);
            if (response.getStatus() == HttpStatusCode.OK) {
                return response.getEntity();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Request failed.");
    }


    private HttpClient login() {
        final HttpClient client = new HttpClient();

        final Security security =
            ProxyFactory.create(Security.class, _public, client);
        security.login("super", "sup3r2008");

        return client;
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _http     = login();
        _commands = ProxyFactory.create(Resources.class, _secure, _http);
        _users =    ProxyFactory.create(Users.class, _secure, _http);
        _actions =  ProxyFactory.create(Actions.class, _secure, _http);
        _folders =  ProxyFactory.create(Folders.class, _secure, _http);
        _pages =    ProxyFactory.create(Pages.class, _secure, _http);
        _security = ProxyFactory.create(Security.class, _public, _http);
        _templates = ProxyFactory.create(Templates.class, _secure, _http);
        _files = ProxyFactory.create(Files.class, _secure+"/files", _http);
        _aliases =
            ProxyFactory.create(Aliases.class, _secure+"/aliases", _http);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        try {
            _security.logout();
        } catch (final Exception e) {
            LOG.warn("Logout failed.", e);
        }
        _http     = null;
        _commands = null;
        _security = null;
        _users    = null;
        _actions  = null;
        _folders  = null;
        _pages  = null;
        _templates  = null;
        _files  = null;
        _aliases  = null;
    }


    @SuppressWarnings("unused")
    private void post(final String url) throws IOException {
            final PostMethod postMethod = new PostMethod(url);

            final NameValuePair userid   =
                new NameValuePair("j_username", "super");
            final NameValuePair password =
                new NameValuePair("j_password", "sup3r2008");
            postMethod.setRequestBody(
                new NameValuePair[] {userid, password});

            final int status = _http.executeMethod(postMethod);
    //        final String body = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection();
            LOG.debug("POST "+url+"  ->  "+status+"\n\n");
        }

    @SuppressWarnings("unused")
    private void get(final String url) throws IOException {
        final GetMethod getMethod = new GetMethod(url);
        final int status = _http.executeMethod(getMethod);
        final String body = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();
        LOG.debug("GET "+url+"  ->  "+status+"\n"+body+"\n\n");
    }

}
