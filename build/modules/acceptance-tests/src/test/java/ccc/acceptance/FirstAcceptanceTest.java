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

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.ws.IRestApi;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FirstAcceptanceTest
    extends
        TestCase {

    static {
        final ResteasyProviderFactory pFactory =
            ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(pFactory);
        pFactory.addMessageBodyReader(ResourceSummaryProvider.class);
    }

    private final String _baseUrl = "http://localhost:81/api";

    /**
     * Test.
     */
    public void testFail() {

        // ARRANGE
        final IRestApi api =
            ProxyFactory.create(IRestApi.class, _baseUrl, login());

        // ACT
        api.roots();

        // ASSERT

    }

    private HttpClient login() {
        final HttpClient client = new HttpClient();
        final GetMethod root = new GetMethod(_baseUrl);
        final PostMethod authpost = new PostMethod(_baseUrl+"/j_security_check");

        final NameValuePair userid   =
            new NameValuePair("j_username", "super");
        final NameValuePair password =
            new NameValuePair("j_password", "sup3r2008");
        authpost.setRequestBody(
            new NameValuePair[] {userid, password});

        try {
            int status = client.executeMethod(root);
            root.releaseConnection();
            status = client.executeMethod(authpost);
            final String body = authpost.getResponseBodyAsString();
            authpost.releaseConnection();
            return client;
        } catch (final IOException e) {
            throw new RuntimeException("Authentication failed ", e);
        }
    }
}
