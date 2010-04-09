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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.json.JsonImpl;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;
import ccc.rest.dto.AclDto.Entry;
import ccc.rest.exceptions.EntityNotFoundException;
import ccc.rest.exceptions.RestException;
import ccc.types.Duration;
import ccc.types.ResourceName;


/**
 * Acceptance tests for resource management.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceAcceptanceTest
    extends
        AbstractAcceptanceTest {

    /** MAX_RANDOM_VALUE : int. */
    private static final int MAX_RANDOM_VALUE = 1000;
    private static final Logger LOG =
        Logger.getLogger(ResourceAcceptanceTest.class);


    /**
     * Test.
     */
    public void testUnlockResource() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final UserDto us = getUsers().loggedInUser();

        // ACT
        getCommands().lock(folder.getId());
        final ResourceSummary locked = getCommands().resource(folder.getId());

        getCommands().unlock(folder.getId());
        final ResourceSummary unlocked = getCommands().resource(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertEquals(us.getUsername(), locked.getLockedBy());
        assertNull(unlocked.getLockedBy());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testMoveResource() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary assets =
            getCommands().resourceForPath("/assets");

        // ACT
        getCommands().lock(folder.getId());
        getCommands().move(folder.getId(), assets.getId());
        final ResourceSummary moved = getCommands().resource(folder.getId());

        // ASSERT
        assertEquals("/assets/"+folder.getName(), moved.getAbsolutePath());
        assertEquals(assets.getId(), moved.getParent());
    }

    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testMoveSecondResourceFromFolder() throws Exception {

        //ARRANGE
        final ResourceSummary firstFolder = tempFolder();
        final ResourceSummary secondFolder = tempFolder();

        final ResourceSummary childFolder1 = getFolders().createFolder(
            new FolderDto(firstFolder.getId(),
            new ResourceName(UUID.randomUUID().toString())));
        getFolders().createFolder(
            new FolderDto(firstFolder.getId(),
            new ResourceName(UUID.randomUUID().toString())));

        // ACT
        getCommands().lock(childFolder1.getId());
        getCommands().move(childFolder1.getId(), secondFolder.getId());

        // ASSERT
        final ResourceSummary content = getCommands().resourceForPath("");
        final Collection<ResourceSummary> children =
            getFolders().getChildren(content.getId());
        assertNotNull(children);

    }


    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testUpdateResourceMetadata() throws Exception  {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final Map<String, String> origData =
            getCommands().metadata(folder.getId());

        final String newTitle = UUID.randomUUID().toString();
        final JsonImpl md = new JsonImpl();
        md.set(JsonKeys.TITLE, newTitle);
        md.set(JsonKeys.DESCRIPTION, newTitle);
        md.set(JsonKeys.TAGS, newTitle);
        md.set(JsonKeys.METADATA, Collections.singletonMap("uuid", newTitle));

        // ACT
        getCommands().lock(folder.getId());
        getCommands().updateMetadata(folder.getId(), md);
        final ResourceSummary updated = getCommands().resource(folder.getId());
        final Map<String, String> newData =
            getCommands().metadata(folder.getId());


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
     * @throws Exception If the test fails.
     */
    public void testUpdateAclUsers() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final UserDto user = tempUser();
        final AclDto acl = new AclDto();
        final Entry e = new Entry();
        e._canRead = true;
        e._canWrite = true;
        e._principal = user.getId();

        // ACT
        acl.setUsers(Collections.singleton(e));
        getCommands().lock(folder.getId());
        getCommands().changeRoles(folder.getId(), acl);

        // ASSERT
        final AclDto actual = getCommands().roles(folder.getId());
        assertEquals(0, actual.getGroups().size());
        assertEquals(1, actual.getUsers().size());
        assertEquals(
            user.getId(), actual.getUsers().iterator().next()._principal);
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testUpdateResourceRoles() throws RestException {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final AclDto origRoles =
            getCommands().roles(folder.getId());
        final Entry e = new Entry();
        e._canRead = true;
        e._canWrite = true;
        e._name = "SITE_BUILDER";
        e._principal =
            getGroups().list("SITE_BUILDER").iterator().next().getId();
        final AclDto roles =
            new AclDto()
                .setGroups(Collections.singleton(e));

        // ACT
        getCommands().lock(folder.getId());
        getCommands().changeRoles(folder.getId(), roles);
        final AclDto withRoles =
            getCommands().roles(folder.getId());

        getCommands().changeRoles(folder.getId(), new AclDto());
        final AclDto noRoles = getCommands().roles(folder.getId());

        // ASSERT
        assertEquals(0, origRoles.getGroups().size());
        assertEquals(roles.getGroups().size(), withRoles.getGroups().size());
        assertTrue(roles.getGroups().containsAll(withRoles.getGroups()));
        assertEquals(0, noRoles.getGroups().size());
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
     *
     * @throws Exception If the test fails.
     */
    public void testLockResource() throws Exception  {

        // ARRANGE

        final ResourceSummary contentRoot = getCommands().resourceForPath("");

        // ACT
        getCommands().lock(contentRoot.getId());

        // ASSERT
        final UserDto currentUser = getUsers().loggedInUser();
        final ResourceSummary updatedRoot =
            getCommands().resource(contentRoot.getId());
        assertEquals(currentUser.getUsername(), updatedRoot.getLockedBy());
        getCommands().unlock(contentRoot.getId());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testChangeResourceTemplate() throws Exception  {

        // ARRANGE

        final ResourceSummary folder = getCommands().resourceForPath("");
        final ResourceSummary ts = dummyTemplate(folder);
        getCommands().lock(folder.getId());

        // ACT
        try {
            getCommands().updateResourceTemplate(
                folder.getId(), new ResourceDto(ts.getId()));
        } finally {
            try {
                getCommands().unlock(folder.getId());
            } catch (final Exception e) {
                LOG.warn("Failed to unlock resource.", e);
            }
        }

        // ASSERT
        final ResourceSummary updatedFolder =
            getCommands().resource(folder.getId());
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
        final Duration origDuration =
            getCommands().cacheDuration(folder.getId());
        final ResourceDto duration =
            new ResourceDto(new Duration(9));

        // ACT
        getCommands().lock(folder.getId());

        getCommands().updateCacheDuration(folder.getId(), duration);
        final Duration withDuration =
            getCommands().cacheDuration(folder.getId());

        getCommands().updateCacheDuration(
            folder.getId(), new ResourceDto((Duration) null));
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
     *
     * @throws Exception If the test fails.
     */
    public void testRename() throws Exception  {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final String newName = UUID.randomUUID().toString();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().rename(folder.getId(), newName);
        final ResourceSummary renamed = getCommands().resource(folder.getId());

        // ASSERT
        assertFalse(newName.equals(folder.getName()));
        assertEquals(newName, renamed.getName());

    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testIncludeInMainMenu() throws Exception  {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().includeInMainMenu(folder.getId());
        final ResourceSummary included = getCommands().resource(folder.getId());

        getCommands().excludeFromMainMenu(folder.getId());
        final ResourceSummary excluded = getCommands().resource(folder.getId());

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
     * @throws Exception If the test fails.
     */
    public void testPublish() throws Exception  {

        // ARRANGE
        final ResourceSummary folder = tempFolder();

        // ACT
        getCommands().lock(folder.getId());

        getCommands().publish(folder.getId());
        final ResourceSummary published =
            getCommands().resource(folder.getId());

        getCommands().unpublish(folder.getId());
        final ResourceSummary unpublished =
            getCommands().resource(folder.getId());

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
    public void testResourceForLegacyId() throws RestException {

        // ARRANGE
        final ResourceSummary f = tempFolder();

        final String id = ""+new Random().nextInt(MAX_RANDOM_VALUE);

        final JsonImpl md = new JsonImpl();
        md.set(JsonKeys.TITLE, f.getTitle());
        md.set(JsonKeys.DESCRIPTION, f.getDescription());
        md.set(JsonKeys.TAGS, f.getTags());
        md.set(JsonKeys.METADATA, Collections.singletonMap("legacyId", id));

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
     *
     * @throws Exception If the test fails.
     */
    public void testSimpleDelete() throws Exception  {

        // ARRANGE
        final ResourceSummary f = tempFolder();

        // ACT
        getCommands().lock(f.getId());
        getCommands().deleteResource(f.getId());

        // ASSERT
        try {
            getCommands().resource(f.getId());
            fail();
        } catch (final ClientResponseFailure e) {
            final EntityNotFoundException enf = convertException(e);
            assertEquals(f.getId(), enf.getId());
        }
    }


//    /**
//     * Test.
//     *
//     * @throws RestException If the test fails.
//     */
//    public void testComputeTemplate() throws RestException {
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
