/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.tests.acceptance;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.api.core.ACL;
import ccc.api.core.Folder;
import ccc.api.core.Group;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.core.ACL.Entry;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.Duration;
import ccc.api.types.ResourceName;
import ccc.api.types.SortOrder;


/**
 * Acceptance tests for resource management.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceAcceptanceTest
    extends
        AbstractAcceptanceTest {

    private static final int MAX_RANDOM_VALUE = 1000;
    private static final int PAGE_SIZE = 20;
    private static final Logger LOG =
        Logger.getLogger(ResourceAcceptanceTest.class);


    /**
     * Test.
     */
    public void testSearchForResources() {

        // ARRANGE
        final ResourceCriteria rc = new ResourceCriteria();
        rc.matchMetadatum("searchable", "false");

        // ACT
        final PagedCollection<ResourceSummary> result =
            getCommands().list(rc, 1, 10);

        // ASSERT
        assertEquals(1, result.getElements().size());
        assertEquals(1, result.getTotalCount());
        assertEquals("assets", result.getElements().get(0).getTitle());
    }


    /**
     * Test.
     */
    public void testUnlockResource() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final User us = getUsers().retrieveCurrent();

        // ACT
        getCommands().lock(folder.getId());
        final ResourceSummary locked = getCommands().retrieve(folder.getId());

        getCommands().unlock(folder.getId());
        final ResourceSummary unlocked = getCommands().retrieve(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertEquals(us.getUsername(), locked.getLockedBy());
        assertNull(unlocked.getLockedBy());
    }


    /**
     * Test.
     */
    public void testMoveResource() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary assets =
            getCommands().resourceForPath("/assets");

        // ACT
        getCommands().lock(folder.getId());
        getCommands().move(folder.getId(), assets.getId());
        final ResourceSummary moved = getCommands().retrieve(folder.getId());

        // ASSERT
        assertEquals("/assets/"+folder.getName(), moved.getAbsolutePath());
        assertEquals(assets.getId(), moved.getParent());
    }

    /**
     * Test.
     */
    public void testMoveSecondResourceFromFolder() {

        //ARRANGE
        final ResourceSummary firstFolder = tempFolder();
        final ResourceSummary secondFolder = tempFolder();

        final ResourceSummary childFolder1 = getFolders().create(
            new Folder(firstFolder.getId(),
            new ResourceName(UUID.randomUUID().toString())));
        getFolders().create(
            new Folder(firstFolder.getId(),
            new ResourceName(UUID.randomUUID().toString())));

        // ACT
        getCommands().lock(childFolder1.getId());
        getCommands().move(childFolder1.getId(), secondFolder.getId());

        // ASSERT
        final ResourceSummary content = getCommands().resourceForPath("");
        final PagedCollection<ResourceSummary> children =
            getCommands().list(
                content.getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "name",
                SortOrder.ASC,
                1,
                1000);
        assertNotNull(children);
        assertNotNull(children.getElements());

    }


    /**
     * Test.
     */
    public void testUpdateResourceMetadata() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Map<String, String> origData =
            getCommands().metadata(folder.getId());

        final String newTitle = UUID.randomUUID().toString();
        final Resource md = new Resource();
        md.setTitle(newTitle);
        md.setDescription(newTitle);
        md.setTags(Collections.singleton(newTitle));
        md.setMetadata(Collections.singletonMap("uuid", newTitle));

        // ACT
        getCommands().lock(folder.getId());
        getCommands().updateMetadata(folder.getId(), md);
        final ResourceSummary updated = getCommands().retrieve(folder.getId());
        final Map<String, String> newData =
            getCommands().metadata(folder.getId());


        // ASSERT
        assertEquals(0, origData.size());
        assertFalse(newTitle.equals(folder.getTitle()));
        assertFalse(newTitle.equals(folder.getDescription()));
        assertEquals(0, folder.getTags().size());

        assertEquals(newTitle, updated.getTitle());
        assertEquals(newTitle, updated.getDescription());
        assertEquals(Collections.singleton(newTitle), updated.getTags());
        assertEquals(1, newData.size());
        assertEquals(newTitle, newData.get("uuid"));
    }


    /**
     * Test.
     */
    public void testUpdateAclUsers() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final User user = tempUser();
        final ACL acl = new ACL();
        final Entry e = new Entry();
        e.setReadable(true);
        e.setWriteable(true);
        e.setPrincipal(user.getId());

        // ACT
        acl.setUsers(Collections.singleton(e));
        getCommands().lock(folder.getId());
        getCommands().changeAcl(folder.getId(), acl);

        // ASSERT
        final ACL actual = getCommands().acl(folder.getId());
        assertEquals(0, actual.getGroups().size());
        assertEquals(1, actual.getUsers().size());
        assertEquals(
            user.getId(), actual.getUsers().iterator().next().getPrincipal());
    }


    /**
     * Test.
     */
    public void testUpdateResourceAcl() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ACL origAcl =
            getCommands().acl(folder.getId());
        final Entry e = new Entry();
        e.setReadable(true);
        e.setWriteable(true);
        e.setName("SITE_BUILDER");
        final List<Group> groups =
            getGroups().query("SITE_BUILDER",
                              1,
                              PAGE_SIZE).getElements();
        e.setPrincipal(groups.iterator().next().getId());
        final ACL acl =
            new ACL()
                .setGroups(Collections.singleton(e));

        // ACT
        getCommands().lock(folder.getId());
        getCommands().changeAcl(folder.getId(), acl);
        final ACL withAcl =
            getCommands().acl(folder.getId());

        getCommands().changeAcl(folder.getId(), new ACL());
        final ACL noPermsAcl = getCommands().acl(folder.getId());

        // ASSERT
        assertEquals(0, origAcl.getGroups().size());
        assertEquals(acl.getGroups().size(), withAcl.getGroups().size());
        assertTrue(acl.getGroups().containsAll(withAcl.getGroups()));
        assertEquals(0, noPermsAcl.getGroups().size());
    }


    /**
     * Test.
     */
    public void testLookupResourceForPath() {

        // ARRANGE

        // ACT
        final ResourceSummary contentRoot = getCommands().resourceForPath("");

        // ASSERT
        assertEquals("content", contentRoot.getName());
        assertNull(contentRoot.getParent());
    }


    /**
     * Test.
     */
    public void testLockResource() {

        // ARRANGE

        final ResourceSummary contentRoot = getCommands().resourceForPath("");

        // ACT
        getCommands().lock(contentRoot.getId());

        // ASSERT
        final User currentUser = getUsers().retrieveCurrent();
        final ResourceSummary updatedRoot =
            getCommands().retrieve(contentRoot.getId());
        assertEquals(currentUser.getUsername(), updatedRoot.getLockedBy());
        getCommands().unlock(contentRoot.getId());
    }


    /**
     * Test.
     */
    public void testChangeResourceTemplate() {

        // ARRANGE

        final ResourceSummary folder = getCommands().resourceForPath("");
        final ResourceSummary ts = dummyTemplate(folder);
        getCommands().lock(folder.getId());

        // ACT
        try {
            getCommands().updateResourceTemplate(
                folder.getId(), new Resource(ts.getId()));
        } finally {
            try {
                getCommands().unlock(folder.getId());
            } catch (final Exception e) {
                LOG.warn("Failed to unlock resource.", e);
            }
        }

        // ASSERT
        final ResourceSummary updatedFolder =
            getCommands().retrieve(folder.getId());
        assertEquals(ts.getId(), updatedFolder.getTemplateId());
    }


    /**
     * Test.
     */
    public void testUpdateCacheDuration() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Duration origDuration =
            getCommands().cacheDuration(folder.getId());
        final Resource duration =
            new Resource(new Duration(9));

        // ACT
        getCommands().lock(folder.getId());

        getCommands().updateCacheDuration(folder.getId(), duration);
        final Duration withDuration =
            getCommands().cacheDuration(folder.getId());

        getCommands().updateCacheDuration(
            folder.getId(), new Resource((Duration) null));
        final Duration noDuration = getCommands().cacheDuration(folder.getId());

        getCommands().deleteCacheDuration(folder.getId());
        final Duration noDuration2 =
            getCommands().cacheDuration(folder.getId());

        // ASSERT
        assertNull(origDuration);
        assertEquals(9, withDuration.time());
        assertNull(noDuration);
        assertNull(noDuration2);
    }


    /**
     * Test.
     */
    public void testRename() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final String newName = UUID.randomUUID().toString();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().rename(folder.getId(), newName);
        final ResourceSummary renamed = getCommands().retrieve(folder.getId());

        // ASSERT
        assertFalse(newName.equals(folder.getName()));
        assertEquals(newName, renamed.getName());

    }


    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().includeInMainMenu(folder.getId());
        final ResourceSummary included = getCommands().retrieve(folder.getId());

        getCommands().excludeFromMainMenu(folder.getId());
        final ResourceSummary excluded = getCommands().retrieve(folder.getId());

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
     */
    public void testPublish() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().publish(folder.getId());
        final ResourceSummary published =
            getCommands().retrieve(folder.getId());

        getCommands().unpublish(folder.getId());
        final ResourceSummary unpublished =
            getCommands().retrieve(folder.getId());

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
     */
    public void testResourceForLegacyId() {

        // ARRANGE
        final ResourceSummary f = tempFolder();

        final String id = ""+new Random().nextInt(MAX_RANDOM_VALUE);

        final Resource md = new Resource();
        md.setTitle(f.getTitle());
        md.setDescription(f.getDescription());
        md.setTags(f.getTags());
        md.setMetadata(Collections.singletonMap("legacyId", id));

        // ACT
        getCommands().lock(f.getId());
        getCommands().updateMetadata(f.getId(), md);

        final ResourceSummary legacy = getCommands().resourceForLegacyId(id);

        // ASSERT
        assertEquals(f.getName(), legacy.getName());
        assertEquals(f.getId(), legacy.getId());
    }


    /**
     * Test.
     */
    public void testSimpleDelete() {

        // ARRANGE
        final ResourceSummary f = tempFolder();

        // ACT
        getCommands().lock(f.getId());
        getCommands().delete(f.getId());

        // ASSERT
        try {
            getCommands().retrieve(f.getId());
            fail();
        } catch (final EntityNotFoundException e) {
            assertEquals(f.getId(), e.getId());
        }
    }

    /**
     * Test.
     */
    public void testListOver1000() {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        for (int i=0;i<1100;i++) {
            tempPage(f.getId(), null);
        }

        // ACT
        PagedCollection<ResourceSummary> list = getCommands().list(f.getId(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "title",
            SortOrder.ASC,
            1,
            1300);

        // ASSERT
        assertEquals(1100, list.getTotalCount());
        assertEquals(1100, list.getElements().size());
    }


//    /**
//     * Test.
//     */
//    public void testComputeTemplate() {
//
//        // ARRANGE
//        final ResourceSummary f = tempFolder();
//        final ResourceSummary ts = dummyTemplate(f);
//        final TemplateSummary original = _templates.templateDelta(ts.getId());
//
//        _commands.lock(f.getId());
//        _commands.updateResourceTemplate(
//            f.getId(), new ResourceDto(ts.getId()));
//
//        final String fName = UUID.randomUUID().toString();
//        final ResourceSummary cf =
//            _folders.createFolder(new FolderDto(f.getId(), fName));
//
//        // ACT
//        final TemplateSummary computed =
//            _commands.computeTemplate(cf.getId());
//
//        // ASSERT
//        assertEquals(original.getDefinition(), computed.getDefinition());
//        assertEquals(original.getBody(), computed.getBody());
//    }

    // clearWorkingCopy, applyWorkingCopy, history and createWorkingCopy tested
    // in FileUploadAcceptanceTest
}