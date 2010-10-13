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
import ccc.api.core.Page;
import ccc.api.core.PagedCollection;
import ccc.api.core.Resource;
import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.core.ACL.Entry;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.Duration;
import ccc.api.types.MimeType;
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
        final Folder folder = tempFolder();
        final User us = getUsers().retrieveCurrent();

        // ACT
        getCommands().lock(folder.getId());
        final Resource locked = getCommands().retrieve(folder.getId());

        getCommands().unlock(folder.getId());
        final Resource unlocked = getCommands().retrieve(folder.getId());

        // ASSERT
        assertNull(folder.getLockedById());
        assertEquals(us.getId(), locked.getLockedById());
        assertNull(unlocked.getLockedById());
    }


    /**
     * Test.
     */
    public void testMoveResource() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Resource assets =
            getCommands().resourceForPath("/assets");

        // ACT
        getCommands().lock(folder.getId());
        getCommands().move(folder.getId(), assets.getId());
        final Resource moved = getCommands().retrieve(folder.getId());

        // ASSERT
        assertEquals("/assets/"+folder.getName(), moved.getAbsolutePath());
        assertEquals(assets.getId(), moved.getParent());
    }

    /**
     * Test.
     */
    public void testMoveSecondResourceFromFolder() {

        //ARRANGE
        final Folder firstFolder = tempFolder();
        final Folder secondFolder = tempFolder();

        final Folder childFolder1 = getFolders().create(
            new Folder(firstFolder.getId(),
            new ResourceName(UUID.randomUUID().toString())));
        getFolders().create(
            new Folder(firstFolder.getId(),
            new ResourceName(UUID.randomUUID().toString())));

        // ACT
        getCommands().lock(childFolder1.getId());
        getCommands().move(childFolder1.getId(), secondFolder.getId());

        // ASSERT
        final Resource content = getCommands().resourceForPath("");
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
        final Folder folder = tempFolder();
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
        final Resource updated = getCommands().retrieve(folder.getId());
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
        final Folder folder = tempFolder();
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
        final Folder folder = tempFolder();
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
        final Resource contentRoot = getCommands().resourceForPath("");

        // ASSERT
        assertEquals("content", contentRoot.getName().toString());
        assertNull(contentRoot.getParent());
    }


    /**
     * Test.
     */
    public void testLockResource() {

        // ARRANGE
        final Resource contentRoot = getCommands().resourceForPath("");

        // ACT
        getCommands().lock(contentRoot.getId());

        // ASSERT
        final User currentUser = getUsers().retrieveCurrent();
        final Resource updatedRoot =
            getCommands().retrieve(contentRoot.getId());
        assertEquals(currentUser.getId(), updatedRoot.getLockedById());
        getCommands().unlock(contentRoot.getId());
    }


    /**
     * Test.
     */
    public void testChangeResourceTemplate() {

        // ARRANGE
        final Folder folder = tempFolder();
        final Template ts = dummyTemplate(folder);
        getCommands().lock(folder.getId());

        // ACT
        getCommands().updateResourceTemplate(
            folder.getId(), new Resource(ts.getId()));

        // ASSERT
        final Resource updatedFolder =
            getCommands().retrieve(folder.getId());
        assertEquals(ts.getId(), updatedFolder.getTemplateId());
    }


    /**
     * Test.
     */
    public void testUpdateCacheDuration() {

        // ARRANGE
        final Folder folder = tempFolder();
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
        final Folder folder = tempFolder();
        final String newName = UUID.randomUUID().toString();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().rename(folder.getId(), newName);
        final Resource renamed = getCommands().retrieve(folder.getId());

        // ASSERT
        assertFalse(newName.equals(folder.getName()));
        assertEquals(newName, renamed.getName().toString());

    }


    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        final Folder folder = tempFolder();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().includeInMainMenu(folder.getId());
        final Resource included = getCommands().retrieve(folder.getId());

        getCommands().excludeFromMainMenu(folder.getId());
        final Resource excluded = getCommands().retrieve(folder.getId());

        // ASSERT
        assertFalse(folder.isIncludeInMainMenu());
        assertNull(folder.getLockedById());
        assertTrue(included.isIncludeInMainMenu());
        assertNotNull(included.getLockedById());
        assertFalse(excluded.isIncludeInMainMenu());
        assertNotNull(excluded.getLockedById());
    }


    /**
     * Test.
     */
    public void testPublish() {

        // ARRANGE
        final Folder folder = tempFolder();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().publish(folder.getId());
        final Resource published =
            getCommands().retrieve(folder.getId());

        getCommands().unpublish(folder.getId());
        final Resource unpublished =
            getCommands().retrieve(folder.getId());

        // ASSERT
        assertNull(folder.getPublishedById());
        assertNull(folder.getLockedById());
        assertNotNull(published.getPublishedById());
        assertNotNull(published.getLockedById());
        assertNull(unpublished.getPublishedById());
        assertNotNull(unpublished.getLockedById());
    }


    /**
     * Test.
     */
    public void testResourceForLegacyId() {

        // ARRANGE
        final Folder f = tempFolder();

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
        final Folder f = tempFolder();

        // ACT
        getCommands().lock(f.getId());
        getCommands().delete(f.getId());

        // ASSERT
        final Resource resource = getCommands().retrieve(f.getId());
        assertNull("Resource should be null", resource);
    }


    /**
     * Test.
     */
    public void testListOver1000() {

        // ARRANGE
        final Folder f = tempFolder();
        for (int i=0;i<1100;i++) {
            tempPage(f.getId(), null);
        }

        // ACT
        final PagedCollection<ResourceSummary> list = getCommands().list(f.getId(),
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

    // clearWorkingCopy, applyWorkingCopy, history and createWorkingCopy tested
    // in FileUploadAcceptanceTest

    /**
     * Test.
     */
    public void testDeletedResourceRetrive() {

        // ARRANGE
        final ResourceSummary templateFolder =
            getCommands().resourceForPath("/assets/templates");
        String name = UUID.randomUUID().toString();

        final Template t = new Template();
        t.setName(new ResourceName(name));
        t.setParent(templateFolder.getId());
        t.setDescription("t-desc");
        t.setTitle("t-title");
        t.setDefinition("<fields/>");
        t.setBody("#set($resources = $services.getResources())"
            +"#set($id = $uuid.fromString(\"1fe95530-3e2c-41a8-a572-3fadbf8aa076\"))"
            +"ok $resources.retrieve($id) $resources.retrieve($resource.getId()).getName()");
        t.setMimeType(MimeType.HTML);
        final ResourceSummary ts = getTemplates().create(t);

        final ResourceSummary f = tempFolder();
        name = UUID.randomUUID().toString();
        final Page page = new Page(f.getId(),
            name,
            ts.getId(),
            "title",
            "",
            true);
        final ResourceSummary ps = getPages().create(page);

        // ACT
        getCommands().lock(ps.getId());
        getCommands().lock(f.getId());
        getCommands().publish(ps.getId());
        getCommands().publish(f.getId());

        // ASSERT
        final String content = getBrowser().get(ps.getAbsolutePath());
        assertEquals("ok $resources.retrieve($id) "+ps.getName(), content);

    }


    /**
     * Test.
     */
    public void testLockResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            getCommands().lock(folder.getId());
            // ASSERT
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testUnlockResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            getCommands().unlock(folder.getId());
            // ASSERT
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testPublishResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            getCommands().publish(folder.getId());
            // ASSERT
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testUnpublishResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        getCommands().publish(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().unpublish(folder.getId());
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testRenameResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().rename(folder.getId(), "newname");
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testMoveResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().move(folder.getId(), folder.getId());
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testChangeResourceTemplateFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().updateResourceTemplate(folder.getId(),
                dummyTemplate(folder));
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testDeleteResourceFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().delete(folder.getId());
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testUpdateCachingFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().updateCacheDuration(folder.getId(), folder);
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testIncludeInMenuFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().includeInMainMenu(folder.getId());
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testUpdateMetadataFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            getCommands().updateMetadata(folder.getId(), folder);
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testUpdateACLFailPermission() {

        // ARRANGE
        final Folder folder = tempFolder();
        getCommands().lock(folder.getId());
        final User u = getUsers().retrieveCurrent();
        setNoWriteACL(folder, u);

        // ACT
        try {
            // ASSERT
            setNoWriteACL(folder, u);
            fail();
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(u.getId(), ex.getUser());
        }
    }

}
