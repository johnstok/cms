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

import static ccc.api.HttpStatusCodes.*;

import java.io.IOException;
import java.nio.charset.Charset;
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
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.rest.TemplateNew;
import ccc.commands.CommandFailedException;
import ccc.domain.Failure;
import ccc.domain.Snapshot;
import ccc.services.Queries;
import ccc.ws.BooleanProvider;
import ccc.ws.DurationReader;
import ccc.ws.FailureWriter;
import ccc.ws.JsonableWriter;
import ccc.ws.ResSummaryReader;
import ccc.ws.ResourceSummaryCollectionReader;
import ccc.ws.RestCommands;
import ccc.ws.SecurityAPI;
import ccc.ws.UserDeltaReader;
import ccc.ws.UserSummaryCollectionReader;
import ccc.ws.UserSummaryReader;


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

        // Readers
        pFactory.addMessageBodyReader(ResourceSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(ResSummaryReader.class);
        pFactory.addMessageBodyReader(DurationReader.class);
        pFactory.addMessageBodyReader(UserSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(UserSummaryReader.class);
        pFactory.addMessageBodyReader(BooleanProvider.class);
        pFactory.addMessageBodyReader(FailureWriter.class);
        pFactory.addMessageBodyReader(UserDeltaReader.class);
    }

    private final String _hostUrl =         "http://localhost:81";
    protected final String _secure =        _hostUrl+"/api/secure";
    protected final String _public =        _hostUrl+"/api/public";
    protected final String _createFileUrl = _hostUrl+"/upload";
    protected final String _updateFileUrl = _hostUrl+"/update_file";

    protected HttpClient _http;
    protected Queries _queries;
    protected RestCommands _commands;
    protected SecurityAPI _security;


    /**
     * Create a template.
     *
     * @param parent The parent folder for the template.
     *
     * @return The template's summary.
     *
     * @throws CommandFailedException If the call fails on the server.
     */
    protected ResourceSummary dummyTemplate(final ResourceSummary parent)
    throws CommandFailedException {
        final String templateName = UUID.randomUUID().toString();
        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);
        final ResourceSummary ts =
            _commands.createTemplate(
                new TemplateNew(
                    parent.getId(),
                    newTemplate,
                    templateName,
                    templateName,
                    templateName));
        return ts;
    }



    /*
     * TODO: Merge into ccc.migration.FileUploader class.
     */
    protected String updateTextFile(final String fText,
                                    final ResourceSummary rs)
    throws IOException, CommandFailedException {
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
        throw new CommandFailedException(new Failure(new Snapshot(body)));
    }


    /*
     * TODO: Merge into client library.
     */
    protected String previewContent(final ResourceSummary rs)
    throws IOException {
        final GetMethod get =
            new GetMethod("http://localhost:81/preview"+rs.getAbsolutePath());
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


    /*
     * TODO: Merge into ccc.migration.FileUploader class.
     */
    protected ResourceSummary createFile(final String fName,
                                         final String fText,
                                         final ResourceSummary filesFolder)
    throws IOException, CommandFailedException {

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
                return new ResourceSummary(new Snapshot(body));
            }
            throw new CommandFailedException(new Failure(new Snapshot(body)));

        } finally {
            postMethod.releaseConnection();
        }
    }

    private HttpClient login() {
        final HttpClient client = new HttpClient();

        final SecurityAPI security =
            ProxyFactory.create(SecurityAPI.class, _public, client);
        security.login("super", "sup3r2008");

        return client;
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _http     = login();
        _queries  = ProxyFactory.create(Queries.class, _secure, _http);
        _commands = ProxyFactory.create(RestCommands.class, _secure, _http);
        _security = ProxyFactory.create(SecurityAPI.class, _public, _http);
    }


    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _http     = null;
        _queries  = null;
        _commands = null;
        _security = null;
    }


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

    private void get(final String url) throws IOException {
        final GetMethod getMethod = new GetMethod(url);
        final int status = _http.executeMethod(getMethod);
        final String body = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();
        LOG.debug("GET "+url+"  ->  "+status+"\n"+body+"\n\n");
    }

}