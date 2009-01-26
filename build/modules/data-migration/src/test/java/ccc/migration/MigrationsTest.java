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
package ccc.migration;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;


/**
 * Tests for the {@link Migrations} class.
 *
 * @author Civic Computing Ltd.
 */
public class MigrationsTest extends TestCase {

    /**
     * Test.
     */
    public void testHttpAuth() {

        // ARRANGE
        final HttpClient client = new HttpClient();
        final Migrations m = new Migrations(null, null);

        // ACT
        m.authenticateForUpload(client, "http://localhost:8080/creator");
    }
}
