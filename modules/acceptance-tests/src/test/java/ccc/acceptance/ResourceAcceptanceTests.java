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

import ccc.api.Duration;
import ccc.api.JsonKeys;
import ccc.api.ResourceSummary;
import ccc.api.UserSummary;
import ccc.api.rest.FolderNew;
import ccc.api.rest.ResourceCacheDurationPU;
import ccc.api.rest.ResourceTemplatePU;
import ccc.commands.CommandFailedException;
import ccc.domain.Snapshot;


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
    public void testUpdateResourceMetadata() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Map<String, String> origData = _queries.metadata(folder.getId());

        final String newTitle = UUID.randomUUID().toString();
        final Snapshot md = new Snapshot();
        md.set(JsonKeys.TITLE, newTitle);
        md.set(JsonKeys.DESCRIPTION, newTitle);
        md.set(JsonKeys.TAGS, newTitle);
        md.set(JsonKeys.METADATA, Collections.singletonMap("uuid", newTitle));

        // ACT
        _commands.lock(folder.getId());
        _commands.updateMetadata(folder.getId(), md);
        final ResourceSummary updated = _queries.resource(folder.getId());
        final Map<String, String> newData = _queries.metadata(folder.getId());


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
        final Collection<String> origRoles = _queries.roles(folder.getId());
        final Set<String> roles = Collections.singleton("foo");

        // ACT
        _commands.lock(folder.getId());
        _commands.changeRoles(folder.getId(), roles);
        final Collection<String> withRoles = _queries.roles(folder.getId());

        _commands.changeRoles(folder.getId(), new HashSet<String>());
        final Collection<String> noRoles = _queries.roles(folder.getId());

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
            _queries.resourceForPath("/content");

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
     *
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
     *
     * @throws CommandFailedException If the test fails.
     */
    public void testUpdateCacheDuration() throws CommandFailedException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Duration origDuration = _queries.cacheDuration(folder.getId());
        final ResourceCacheDurationPU duration =
            new ResourceCacheDurationPU(new Duration(9));

        // ACT
        _commands.lock(folder.getId());

        _commands.updateCacheDuration(folder.getId(), duration);
        final Duration withDuration = _queries.cacheDuration(folder.getId());

        _commands.updateCacheDuration(
            folder.getId(), new ResourceCacheDurationPU(null));
        final Duration noDuration = _queries.cacheDuration(folder.getId());

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
        final ResourceSummary renamed = _queries.resource(folder.getId());

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
        final ResourceSummary included = _queries.resource(folder.getId());

        _commands.excludeFromMainMenu(folder.getId());
        final ResourceSummary excluded = _queries.resource(folder.getId());

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
        final ResourceSummary published = _queries.resource(folder.getId());

        _commands.unpublish(folder.getId());
        final ResourceSummary unpublished = _queries.resource(folder.getId());

        // ASSERT
        assertNull(folder.getPublishedBy());
        assertNull(folder.getLockedBy());
        assertNotNull(published.getPublishedBy());
        assertNotNull(published.getLockedBy());
        assertNull(unpublished.getPublishedBy());
        assertNotNull(unpublished.getLockedBy());
    }


    private ResourceSummary tempFolder() throws CommandFailedException {
        final String fName = UUID.randomUUID().toString();
        final ResourceSummary content = _queries.resourceForPath("/content");
        return _commands.createFolder(new FolderNew(content.getId(), fName));
    }
}
