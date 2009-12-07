/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.acceptance;

import static ccc.types.HttpStatusCode.*;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;

import ccc.rest.RestException;
import ccc.rest.Security;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Initial acceptance tests.
 *
 * @author Civic Computing Ltd.
 */
public class FirstAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testFail() throws RestException {

        // ARRANGE

        // ACT
        try {
            getCommands().fail();
            fail();

        // ASSERT
        } catch (final ClientResponseFailure e) {
            assertEquals(IM_A_TEAPOT, e.getResponse().getStatus());
            final Failure f = e.getResponse().getEntity(Failure.class);
            assertEquals(FailureCode.PRIVILEGES, f.getCode());
        }
    }

    /**
     * Test.
     */
    public void testRestLogin() {

        // ARRANGE
        final Security security =
            ProxyFactory.create(
                Security.class, getPublicApiURL(), new HttpClient());

        // ACT
        security.login("super", "sup3r2008");

        // ASSERT
        assertTrue(security.isLoggedIn().booleanValue());
    }
}
