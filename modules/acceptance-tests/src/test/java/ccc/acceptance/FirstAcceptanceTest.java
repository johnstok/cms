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

import static ccc.types.HttpStatusCode.*;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;

import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.rest.TemplateNew;
import ccc.commands.CommandFailedException;
import ccc.domain.Failure;
import ccc.types.FailureCode;
import ccc.ws.SecurityAPI;


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
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateTemplate() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary templateFolder =
            _queries.resourceForPath("/assets/templates");
        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);

        // ACT
        final ResourceSummary ts =
            _commands.createTemplate(
                new TemplateNew(
                    templateFolder.getId(),
                    newTemplate,
                    "t-title",
                    "t-desc",
                    "t-name"));

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
    public void testFail() throws CommandFailedException {

        // ARRANGE

        // ACT
        try {
            _commands.fail();
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
        final SecurityAPI security =
            ProxyFactory.create(SecurityAPI.class, _public, new HttpClient());

        // ACT
        security.login("super", "sup3r2008");

        // ASSERT
        assertTrue(security.isLoggedIn().booleanValue());
    }
}
