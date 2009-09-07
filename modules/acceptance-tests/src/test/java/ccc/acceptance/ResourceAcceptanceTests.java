/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.acceptance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.rest.CommandFailedException;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;
import ccc.types.Duration;


/**
 * Acceptance tests for resource management.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceAcceptanceTests
    extends
        AbstractAcceptanceTest {

    private static final Logger LOG =
        Logger.getLogger(ResourceAcceptanceTests.class);


    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testUnlockResource() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final UserDto us = _users.loggedInUser();

        // ACT
        _commands.lock(folder.getId());
        final ResourceSummary locked = _commands.resource(folder.getId());

        _commands.unlock(folder.getId());
        final ResourceSummary unlocked = _commands.resource(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertEquals(us.getUsername(), locked.getLockedBy());
        assertNull(unlocked.getLockedBy());
    }


    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testMoveResource() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary assets = _commands.resourceForPath("/assets");

        // ACT
        _commands.lock(folder.getId());
        _commands.move(folder.getId(), assets.getId());
        final ResourceSummary moved = _commands.resource(folder.getId());

        // ASSERT
        assertEquals("/content/"+folder.getName(), folder.getAbsolutePath());
        assertEquals(assets.getId(), moved.getParentId());
    }


    /**
     * Test.
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateResourceMetadata() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Map<String, String> origData = _commands.metadata(folder.getId());

        final String newTitle = UUID.randomUUID().toString();
        final JsonImpl md = new JsonImpl();
        md.set(JsonKeys.TITLE, newTitle);
        md.set(JsonKeys.DESCRIPTION, newTitle);
        md.set(JsonKeys.TAGS, newTitle);
        md.set(JsonKeys.METADATA, Collections.singletonMap("uuid", newTitle));

        // ACT
        _commands.lock(folder.getId());
        _commands.updateMetadata(folder.getId(), md);
        final ResourceSummary updated = _commands.resource(folder.getId());
        final Map<String, String> newData = _commands.metadata(folder.getId());


        // ASSERT
        assertEquals(0, origData.size());
        assertFalse(newTitle.equals(folder.getTitle()));
        assertFalse(newTitle.equals(folder.getDescription()));
        assertFalse(newTitle.equals(folder.getTags()));

        assertEquals(newTitle, updated.getTitle());
        assertEquals(newTitle, updated.getDescription());
        assertEquals(newTitle, updated.getTags());
        assertEquals(1, newData.size());
        assertEquals(newTitle, newData.get("uuid"));
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateResourceRoles() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Collection<String> origRoles = _commands.roles(folder.getId());
        final Set<String> roles = Collections.singleton("foo");

        // ACT
        _commands.lock(folder.getId());
        _commands.changeRoles(folder.getId(), roles);
        final Collection<String> withRoles = _commands.roles(folder.getId());

        _commands.changeRoles(folder.getId(), new HashSet<String>());
        final Collection<String> noRoles = _commands.roles(folder.getId());

        // ASSERT
        assertEquals(0, origRoles.size());
        assertEquals(roles.size(), withRoles.size());
        assertTrue(roles.containsAll(withRoles));
        assertEquals(0, noRoles.size());
    }


    /**
     * Test.
     */
    public void testLookupResourceForPath() {

        // ARRANGE

        // ACT
        final ResourceSummary contentRoot =
            _commands.resourceForPath("/content");

        // ASSERT
        assertEquals("content", contentRoot.getName());
        assertNull(contentRoot.getParentId());
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testLockResource() throws CommandFailedException {

        // ARRANGE

        final ResourceSummary contentRoot =
            _commands.resourceForPath("/content");

        // ACT
        _commands.lock(contentRoot.getId());

        // ASSERT
        final UserDto currentUser = _users.loggedInUser();
        final ResourceSummary updatedRoot =
            _commands.resource(contentRoot.getId());
        assertEquals(currentUser.getUsername(), updatedRoot.getLockedBy());
        _commands.unlock(contentRoot.getId());
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testChangeResourceTemplate() throws CommandFailedException {

        // ARRANGE

        final ResourceSummary folder = _commands.resourceForPath("/content");
        final ResourceSummary ts = dummyTemplate(folder);
        _commands.lock(folder.getId());

        // ACT
        try {
            _commands.updateResourceTemplate(
                folder.getId(), new ResourceDto(ts.getId()));
        } finally {
            try {
                _commands.unlock(folder.getId());
            } catch (final Exception e) {
                LOG.warn("Failed to unlock resource.", e);
            }
        }

        // ASSERT
        final ResourceSummary updatedFolder = _commands.resource(folder.getId());
        assertEquals(ts.getId(), updatedFolder.getTemplateId());
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateCacheDuration() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Duration origDuration = _commands.cacheDuration(folder.getId());
        final ResourceDto duration =
            new ResourceDto(new Duration(9));

        // ACT
        _commands.lock(folder.getId());

        _commands.updateCacheDuration(folder.getId(), duration);
        final Duration withDuration = _commands.cacheDuration(folder.getId());

        _commands.updateCacheDuration(
            folder.getId(), new ResourceDto((Duration) null));
        final Duration noDuration = _commands.cacheDuration(folder.getId());

        // ASSERT
        assertNull(origDuration);
        assertEquals(9, withDuration.time());
        assertNull(noDuration);
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testRename() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final String newName = UUID.randomUUID().toString();

        // ACT
        _commands.lock(folder.getId());

        _commands.rename(folder.getId(), newName);
        final ResourceSummary renamed = _commands.resource(folder.getId());

        // ASSERT
        assertFalse(newName.equals(folder.getName()));
        assertEquals(newName, renamed.getName());

    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testIncludeInMainMenu() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        // ACT
        _commands.lock(folder.getId());

        _commands.includeInMainMenu(folder.getId());
        final ResourceSummary included = _commands.resource(folder.getId());

        _commands.excludeFromMainMenu(folder.getId());
        final ResourceSummary excluded = _commands.resource(folder.getId());

        // ASSERT
        assertFalse(folder.isIncludeInMainMenu());
        assertNull(folder.getLockedBy());
        assertTrue(included.isIncludeInMainMenu());
        assertNotNull(included.getLockedBy());
        assertFalse(excluded.isIncludeInMainMenu());
        assertNotNull(excluded.getLockedBy());
    }


    /**
     * Test.
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testPublish() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        // ACT
        _commands.lock(folder.getId());

        _commands.publish(folder.getId());
        final ResourceSummary published = _commands.resource(folder.getId());

        _commands.unpublish(folder.getId());
        final ResourceSummary unpublished = _commands.resource(folder.getId());

        // ASSERT
        assertNull(folder.getPublishedBy());
        assertNull(folder.getLockedBy());
        assertNotNull(published.getPublishedBy());
        assertNotNull(published.getLockedBy());
        assertNull(unpublished.getPublishedBy());
        assertNotNull(unpublished.getLockedBy());
    }
}
