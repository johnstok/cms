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

import ccc.rest.CommandFailedException;
import ccc.rest.Security;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.types.Failure;
import ccc.types.FailureCode;
import ccc.types.MimeType;


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
            _commands.resourceForPath("/assets/templates");
        final TemplateDelta newTemplate =
            new TemplateDelta("body", "<fields/>", MimeType.HTML);

        // ACT
        final ResourceSummary ts =
            _templates.createTemplate(
                new TemplateDto(
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
        final Security security =
            ProxyFactory.create(Security.class, _public, new HttpClient());

        // ACT
        security.login("super", "sup3r2008");

        // ASSERT
        assertTrue(security.isLoggedIn().booleanValue());
    }
}
