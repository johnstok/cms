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

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
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
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
    }

    /**
     * Test.
     */
    public void testFail() {

        // ARRANGE
        final IRestApi api =
            ProxyFactory.create(
                IRestApi.class, "http://localhost:81/api", login());

        // ACT
        api.roots();

        // ASSERT

    }

    private HttpClient login() {
        throw new UnsupportedOperationException("Method not implemented.");
    }
}
