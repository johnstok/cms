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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.rest.RestException;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;
import ccc.serialization.JsonImpl;
import ccc.serialization.JsonKeys;
import ccc.types.Duration;
import ccc.types.ResourceOrder;


/**
 * Acceptance tests for resource management.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceAcceptanceTest
    extends
        AbstractAcceptanceTest {

    private static final Logger LOG =
        Logger.getLogger(ResourceAcceptanceTest.class);


    /**
     * Test.
     * @throws RestException If the test fails.
     */
    public void testUnlockResource() throws RestException {

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
     * @throws RestException If the test fails.
     */
    public void testMoveResource() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary assets = resourceForPath("/assets");

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
     * @throws RestException If the test fails.
     */
    public void testUpdateResourceMetadata() throws RestException {

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
     * @throws RestException If the test fails.
     */
    public void testUpdateResourceRoles() throws RestException {

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
        final ResourceSummary contentRoot = resourceForPath("/content");

        // ASSERT
        assertEquals("content", contentRoot.getName());
        assertNull(contentRoot.getParentId());
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testLockResource() throws RestException {

        // ARRANGE

        final ResourceSummary contentRoot = resourceForPath("/content");

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
     * @throws RestException If the test fails.
     */
    public void testChangeResourceTemplate() throws RestException {

        // ARRANGE

        final ResourceSummary folder = resourceForPath("/content");
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
     * @throws RestException If the test fails.
     */
    public void testUpdateCacheDuration() throws RestException {

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
     * @throws RestException If the test fails.
     */
    public void testRename() throws RestException {

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
     * @throws RestException If the test fails.
     */
    public void testIncludeInMainMenu() throws RestException {

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
     * @throws RestException If the test fails.
     */
    public void testPublish() throws RestException {

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

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testChangeFolderSortOrder() throws RestException {
        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final List<String> sortList  = new ArrayList<String>();
        // ACT
        _commands.lock(folder.getId());
        final FolderDelta fd =
            new FolderDelta(ResourceOrder.DATE_CHANGED_ASC.name(), null, sortList);

        _folders.updateFolder(folder.getId(), fd);
        final ResourceSummary updated = _commands.resource(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(ResourceOrder.DATE_CHANGED_ASC.name(), updated.getSortOrder());
    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testChangeFolderIndexPage() throws RestException {
        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary template =
            dummyTemplate(resourceForPath("/content"));
        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        _commands.lock(folder.getId());
        final List<String> sortList  = new ArrayList<String>();
        sortList.add(page.getId().toString());

        final FolderDelta fd =
            new FolderDelta(tempFolder().getSortOrder(), page.getId(), sortList);
        _folders.updateFolder(folder.getId(), fd);
        final ResourceSummary updated = _commands.resource(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(ResourceOrder.MANUAL.name(), updated.getSortOrder());
        assertEquals(page.getId(), updated.getIndexPageId());
        assertEquals(1, updated.getChildCount());

    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testGetChildrenManualOrder() throws RestException {
        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary template =
            dummyTemplate(_commands.resourceForPath("/content"));
        final ResourceSummary page1 = tempPage(folder.getId(), template.getId());
        final ResourceSummary page2 = tempPage(folder.getId(), template.getId());
        final ResourceSummary page3 = tempPage(folder.getId(), template.getId());

        // ACT
        _commands.lock(folder.getId());
        final List<String> sl  = new ArrayList<String>();
        sl.add(page2.getId().toString());
        sl.add(page1.getId().toString());
        sl.add(page3.getId().toString());

        final FolderDelta fd =
            new FolderDelta(ResourceOrder.DATE_CHANGED_ASC.name(), null, sl);

        _folders.updateFolder(folder.getId(), fd);
        final ResourceSummary updated = _commands.resource(folder.getId());

        final Collection<ResourceSummary> children =
            _folders.getChildrenManualOrder(folder.getId());
        final List<ResourceSummary> list =
            new ArrayList<ResourceSummary>(children);

        // ASSERT
        assertNull(folder.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(ResourceOrder.DATE_CHANGED_ASC.name(),
                     updated.getSortOrder());
        assertEquals(page2.getId(), list.get(0).getId());
        assertEquals(page1.getId(), list.get(1).getId());
        assertEquals(page3.getId(), list.get(2).getId());
    }

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testGetChildren() throws RestException {
        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(_commands.resourceForPath("/content"));
        final ResourceSummary page1 = tempPage(f.getId(), template.getId());
        final ResourceSummary page2 = tempPage(f.getId(), template.getId());
        final ResourceSummary page3 = tempPage(f.getId(), template.getId());

        // ACT
        _commands.lock(f.getId());
        final List<String> sl  = new ArrayList<String>();
        sl.add(page2.getId().toString());
        sl.add(page1.getId().toString());
        sl.add(page3.getId().toString());

        final FolderDelta fd =
            new FolderDelta(ResourceOrder.DATE_CREATED_ASC.name(), null, sl);

        _folders.updateFolder(f.getId(), fd);
        final ResourceSummary updated = _commands.resource(f.getId());

        final List<ResourceSummary> list =
            new ArrayList<ResourceSummary>(_folders.getChildren(f.getId()));

        // ASSERT
        assertNull(f.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(ResourceOrder.DATE_CREATED_ASC.name(),
            updated.getSortOrder());
        assertEquals(page1.getId(), list.get(0).getId());
        assertEquals(page2.getId(), list.get(1).getId());
        assertEquals(page3.getId(), list.get(2).getId());
    }
}
