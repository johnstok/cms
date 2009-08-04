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
package ccc.acceptance;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.api.CommandFailedException;
import ccc.api.Failure;
import ccc.api.FailureCodes;
import ccc.api.ResourceSummary;
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
import ccc.ws.UserSummaryCollectionReader;
import ccc.ws.UserSummaryReader;


/**
 * Tests for file upload.
 *
 * @author Civic Computing Ltd.
 */
public class FileUploadAcceptanceTest
    extends
        TestCase {

    static {
        final ResteasyProviderFactory pFactory =
            ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(pFactory);
        pFactory.addMessageBodyReader(ResourceSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(ResSummaryReader.class);
        pFactory.addMessageBodyReader(DurationReader.class);
        pFactory.addMessageBodyReader(UserSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(UserSummaryReader.class);
        pFactory.addMessageBodyWriter(JsonableWriter.class);
        pFactory.addMessageBodyReader(BooleanProvider.class);
        pFactory.addMessageBodyReader(FailureWriter.class);
    }

    private final String _hostUrl = "http://localhost:81/api";
    private final String _createFileUrl = "http://localhost:81/upload";
    private final String _updateFileUrl = "http://localhost:81/update_file";
    private final String _secure = _hostUrl+"/secure";
    private final String _public = _hostUrl+"/public";


    /**
     * Test.
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateFile() throws IOException, CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final HttpClient c = login();
        final Queries queries =
            ProxyFactory.create(Queries.class, _secure, c);

        final ResourceSummary filesFolder =
            queries.resourceForPath("/content/files");

        // ACT
        final ResourceSummary rs = createFile(fName, "Hello!", c, filesFolder);


        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/content/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", previewContent(rs, c));
    }


    /**
     * Test.
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateFileRejectsDuplicateNames()
    throws IOException, CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final HttpClient c = login();
        final Queries queries =
            ProxyFactory.create(Queries.class, _secure, c);

        final ResourceSummary filesFolder =
            queries.resourceForPath("/content/files");
        final ResourceSummary rs = createFile(fName, "Hello!", c, filesFolder);

        // ACT
        try {
            createFile(fName, "Hello!", c, filesFolder);
        } catch (final CommandFailedException e) {
            assertEquals(FailureCodes.EXISTS, e.getCode());
        }


        // ASSERT
        assertEquals(fName, rs.getName());
        assertEquals("/content/files/"+fName, rs.getAbsolutePath());
        assertEquals("Hello!", previewContent(rs, c));
    }


    /**
     * Test.
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateFile() throws IOException, CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final HttpClient c = login();
        final Queries queries =
            ProxyFactory.create(Queries.class, _secure, c);
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _secure, c);

        final ResourceSummary filesFolder =
            queries.resourceForPath("/content/files");
        final ResourceSummary rs = createFile(fName, "Hello!", c, filesFolder);
        commands.lock(rs.getId());

        // ACT
        final String body = updateTextFile(c, "Update!", rs);

        // ASSERT
        assertEquals("NULL", body);
        assertEquals("Update!", previewContent(rs, c));
    }


    /**
     * Test.
     * @throws IOException If the test fails.
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateFileRequiresLock() throws IOException,
                                                    CommandFailedException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final HttpClient c = login();
        final Queries queries =
            ProxyFactory.create(Queries.class, _secure, c);

        final ResourceSummary filesFolder =
            queries.resourceForPath("/content/files");
        final ResourceSummary rs = createFile(fName, "Hello!", c, filesFolder);

        // ACT
        try {
            updateTextFile(c, "Update!", rs);

        // ASSERT
        } catch (final CommandFailedException e) {
            assertEquals(FailureCodes.UNLOCKED, e.getCode());
        }
        assertEquals("Hello!", previewContent(rs, c));
    }


    /*
     * TODO: Merge into ccc.migration.FileUploader class.
     */
    private String updateTextFile(final HttpClient c,
                                  final String fText,
                                  final ResourceSummary rs)
    throws IOException, HttpException, CommandFailedException {
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

        c.executeMethod(postMethod);

        final String body = postMethod.getResponseBodyAsString();
        if (200==postMethod.getStatusCode()) {
            return body;
        }
        throw new CommandFailedException(new Failure(new Snapshot(body)));
    }


    /*
     * TODO: Merge into client library.
     */
    private String previewContent(final ResourceSummary rs,
                                  final HttpClient c) throws IOException {
        final GetMethod get =
            new GetMethod("http://localhost:81/preview"+rs.getAbsolutePath());
        try {
            c.executeMethod(get);
            final int status = get.getStatusCode();
            if (200==status) {
                return get.getResponseBodyAsString();
            }
            throw new RuntimeException(status+": "+get.getResponseBodyAsString());
        } finally {
            get.releaseConnection();
        }
    }


    private HttpClient login() {
        final HttpClient client = new HttpClient();

        final SecurityAPI security =
            ProxyFactory.create(SecurityAPI.class, _public, client);
        security.login("super", "sup3r2008");

        return client;

    }


    /*
     * TODO: Merge into ccc.migration.FileUploader class.
     */
    private ResourceSummary createFile(final String fName,
                                       final String fText,
                                       final HttpClient c,
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

            final int status = c.executeMethod(postMethod);
            final String body = postMethod.getResponseBodyAsString();

            if (200==status) {
                return new ResourceSummary(new Snapshot(body));
            }
            throw new CommandFailedException(new Failure(new Snapshot(body)));

        } finally {
            postMethod.releaseConnection();
        }
    }
}
