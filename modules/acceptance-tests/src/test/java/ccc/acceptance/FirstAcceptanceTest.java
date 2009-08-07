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

import static ccc.api.HttpStatusCodes.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;

import ccc.api.Duration;
import ccc.api.FailureCode;
import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;
import ccc.api.UserDelta;
import ccc.api.UserSummary;
import ccc.api.Username;
import ccc.api.rest.FolderNew;
import ccc.api.rest.ResourceCacheDurationPU;
import ccc.api.rest.ResourceTemplatePU;
import ccc.api.rest.TemplateNew;
import ccc.api.rest.UserNew;
import ccc.commands.CommandFailedException;
import ccc.domain.Failure;
import ccc.ws.SecurityAPI;


/**
 * Initial acceptance tests.
 *
 * @author Civic Computing Ltd.
 */
public class FirstAcceptanceTest
    extends
        AbstractAcceptanceTest {

    private static final Logger LOG =
        Logger.getLogger(FirstAcceptanceTest.class);

    /**
     * Test.
     */
    public void testLookupResourceForPath() {

        // ARRANGE

        // ACT
        final ResourceSummary contentRoot =
            _queries.resourceForPath("/content");

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

        final ResourceSummary contentRoot =
            _queries.resourceForPath("/content");

        // ACT
        _commands.lock(contentRoot.getId());

        // ASSERT
        final UserSummary currentUser = _queries.loggedInUser();
        final ResourceSummary updatedRoot =
            _queries.resource(contentRoot.getId());
        assertEquals(currentUser.getUsername(), updatedRoot.getLockedBy());
        _commands.unlock(contentRoot.getId());
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testChangeResourceTemplate() throws CommandFailedException {

        // ARRANGE

        final ResourceSummary folder = _queries.resourceForPath("/content");
        final ResourceSummary ts = dummyTemplate(folder);
        _commands.lock(folder.getId());

        // ACT
        try {
            _commands.updateResourceTemplate(
                folder.getId(), new ResourceTemplatePU(ts.getId()));
        } finally {
            try {
                _commands.unlock(folder.getId());
            } catch (final Exception e) {
                LOG.warn("Failed to unlock resource.", e);
            }
        }

        // ASSERT
        final ResourceSummary updatedFolder = _queries.resource(folder.getId());
        assertEquals(ts.getId(), updatedFolder.getTemplateId());
    }


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
    public void testUpdateDuration() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary contentFolder =
            _queries.resourceForPath("/content");
        final ResourceSummary durationFolder =
            _commands.createFolder(
                new FolderNew(contentFolder.getId(), "duration"));
        _commands.lock(durationFolder.getId());
        assertNull(_queries.cacheDuration(durationFolder.getId()));

        // ACT
        _commands.updateCacheDuration(
            durationFolder.getId(),
            new ResourceCacheDurationPU(new Duration(1)));
        assertEquals(
            new Duration(1), _queries.cacheDuration(durationFolder.getId()));

        _commands.deleteCacheDuration(durationFolder.getId());

        // ASSERT
        assertNull(_queries.cacheDuration(durationFolder.getId()));
    }

    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testCreateUser() throws CommandFailedException {

        // ARRANGE
        final UserDelta d =
            new UserDelta(
                "abc@def.com",
                new Username("qwerty"),
                new HashSet<String>(),
                new HashMap<String, String>());

        // ACT
        _commands.createUser(new UserNew(d, "Testtest00-"));
        final Collection<UserSummary> users =
            _queries.listUsersWithUsername(d.getUsername().toString());

        // ASSERT
        assertEquals(1, users.size());
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
