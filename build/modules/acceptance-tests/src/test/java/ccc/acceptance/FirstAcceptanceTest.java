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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.api.CommandFailedException;
import ccc.api.Duration;
import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.services.Queries;
import ccc.ws.BooleanProvider;
import ccc.ws.DurationReader;
import ccc.ws.JsonableWriter;
import ccc.ws.ResSummaryReader;
import ccc.ws.ResourceSummaryCollectionReader;
import ccc.ws.RestCommands;
import ccc.ws.UserSummaryCollectionReader;
import ccc.ws.UserSummaryReader;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FirstAcceptanceTest
    extends
        TestCase {
    private static Logger log = Logger.getLogger(FirstAcceptanceTest.class);

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
    }

    private final String _hostUrl = "http://localhost:81";
    private final String _baseUrl = _hostUrl+"/api";

    /**
     * Test.
     */
    public void testLookupResourceForPath() {

        // ARRANGE
        final Queries api =
            ProxyFactory.create(Queries.class, _baseUrl, login());

        // ACT
        final ResourceSummary contentRoot = api.resourceForPath("/content");

        // ASSERT
        assertEquals("content", contentRoot.getName());
        assertNull(contentRoot.getParentId());
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testLockResource() throws CommandFailedException {

        // ARRANGE
        final Queries queries =
            ProxyFactory.create(Queries.class, _baseUrl, login());
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _baseUrl, login());

        final ResourceSummary contentRoot = queries.resourceForPath("/content");

        // ACT
        commands.lock(contentRoot.getId());

        // ASSERT
        final UserSummary currentUser = queries.loggedInUser();
        final ResourceSummary updatedRoot = queries.resource(contentRoot.getId());
        assertEquals(currentUser.getUsername(), updatedRoot.getLockedBy());
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateTemplate() throws CommandFailedException {

        // ARRANGE
        final Queries queries =
            ProxyFactory.create(Queries.class, _baseUrl, login());
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _baseUrl, login());

        final ResourceSummary templateFolder =
            queries.resourceForPath("/assets/templates");
        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);

        // ACT
        final ResourceSummary ts =
            commands.createTemplate(
                templateFolder.getId(), newTemplate, "t-title", "t-desc", "t-name");

        // ASSERT
        assertEquals("/assets/templates/t-name", ts.getAbsolutePath());
        assertEquals("t-desc", ts.getDescription());
        assertEquals("t-name", ts.getName());
        assertEquals("t-title", ts.getTitle());
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateDuration() throws CommandFailedException {

        // ARRANGE
        final Queries queries =
            ProxyFactory.create(Queries.class, _baseUrl, login());
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _baseUrl, login());

        final ResourceSummary contentFolder =
            queries.resourceForPath("/content");
        final ResourceSummary durationFolder =
            commands.createFolder(contentFolder.getId(), "duration");
        commands.lock(durationFolder.getId());
        assertNull(queries.cacheDuration(durationFolder.getId()));

        // ACT
        commands.updateCacheDuration(durationFolder.getId(), new Duration(1));
        assertEquals(
            new Duration(1), queries.cacheDuration(durationFolder.getId()));

        commands.deleteCacheDuration(durationFolder.getId());

        // ASSERT
        assertNull(queries.cacheDuration(durationFolder.getId()));
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateUser() throws CommandFailedException {

        // ARRANGE
        final Queries queries =
            ProxyFactory.create(Queries.class, _baseUrl, login());
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _baseUrl, login());

        final UserDelta d =
            new UserDelta(
                "abc@def.com",
                new Username("qwerty"),
                new HashSet<String>(),
                new HashMap<String, String>());

        // ACT
        commands.createUser(d, "Testtest00-");
        final Collection<UserSummary> users =
            queries.listUsersWithUsername(d.getUsername().toString());

        // ASSERT
        assertEquals(1, users.size());
    }

    /**
     * Test.
     */
    public void testRestLogin() {

        // ARRANGE
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _baseUrl, new HttpClient());

        // ACT
        commands.login("super", "sup3r2008");

        // ASSERT
        assertTrue(commands.isLoggedIn());
    }

    private HttpClient login() {
        final HttpClient client = new HttpClient();
        final RestCommands commands =
            ProxyFactory.create(RestCommands.class, _baseUrl, client);

        // ACT
        commands.login("super", "sup3r2008");
        return client;


//        try {
//            final HttpClient client = new HttpClient();
//            get( client, _baseUrl);
//            post(client, _hostUrl+"/j_security_check");
//            get( client, _baseUrl);
//            return client;
//
//        } catch (final IOException e) {
//            throw new RuntimeException("Authentication failed ", e);
//        }
    }

    private void post(final HttpClient client, final String url) throws IOException {
        final PostMethod postMethod = new PostMethod(url);

        final NameValuePair userid   =
            new NameValuePair("j_username", "super");
        final NameValuePair password =
            new NameValuePair("j_password", "sup3r2008");
        postMethod.setRequestBody(
            new NameValuePair[] {userid, password});

        final int status = client.executeMethod(postMethod);
//        final String body = postMethod.getResponseBodyAsString();
        postMethod.releaseConnection();
        log.debug("POST "+url+"  ->  "+status+"\n\n");
    }

    private void get(final HttpClient client, final String url) throws IOException {
        final GetMethod getMethod = new GetMethod(url);
        final int status = client.executeMethod(getMethod);
        final String body = getMethod.getResponseBodyAsString();
        getMethod.releaseConnection();
        log.debug("GET "+url+"  ->  "+status+"\n"+body+"\n\n");
    }
}
