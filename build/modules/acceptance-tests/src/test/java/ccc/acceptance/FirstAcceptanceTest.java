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

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.api.Duration;
import ccc.api.Queries;
import ccc.api.ResourceSummary;


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
        pFactory.addMessageBodyReader(ResourceSummaryProvider.class);
    }

    private final String _hostUrl = "http://localhost:81";
    private final String _baseUrl = _hostUrl+"/api";

    /**
     * Test.
     */
    public void testFail() {

        // ARRANGE
        final Queries api =
            ProxyFactory.create(Queries.class, _baseUrl, login());

        // ACT
        final Collection<ResourceSummary> roots = api.roots();

        // ASSERT
        assertEquals(2, roots.size());
        testDuration(api, roots.iterator().next());
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param next
     */
    private void testDuration(final Queries api, final ResourceSummary rs) {
        final Duration d = api.cacheDuration(rs.getId());
        assertNull(d);
    }

    private HttpClient login() {
        try {
            final HttpClient client = new HttpClient();
            get( client, _baseUrl);
            post(client, _hostUrl+"/j_security_check");
            get( client, _baseUrl);
            return client;

        } catch (final IOException e) {
            throw new RuntimeException("Authentication failed ", e);
        }
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
