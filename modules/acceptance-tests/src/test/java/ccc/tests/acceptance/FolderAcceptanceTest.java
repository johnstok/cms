/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.tests.acceptance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.dto.AclDto;
import ccc.api.dto.FolderDelta;
import ccc.api.dto.FolderDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.UserDto;
import ccc.api.dto.AclDto.Entry;
import ccc.api.exceptions.RestException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;
import ccc.types.ResourceOrder;


/**
 * Acceptance tests for Folders REST methods.
 *
 * @author Civic Computing Ltd.
 */
public class FolderAcceptanceTest extends AbstractAcceptanceTest {

    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testGetFolderChildren() throws RestException {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final String cn1 = "a-"+UUID.randomUUID().toString();
        final String cn2 = "b-"+UUID.randomUUID().toString();
        final ResourceSummary content =
            getCommands().resourceForPath("");
        final ResourceSummary testFolder =
            getFolders().createFolder(
                new FolderDto(content.getId(), new ResourceName(fName)));
        final ResourceSummary child1 =
            getFolders().createFolder(
                new FolderDto(testFolder.getId(), new ResourceName(cn1)));
        final ResourceSummary child2 =
            getFolders().createFolder(
                new FolderDto(testFolder.getId(), new ResourceName(cn2)));

        // ACT
        final List<ResourceSummary> folders = new ArrayList<ResourceSummary>(
            getFolders().getFolderChildren(testFolder.getId()));

        // ASSERT
        assertEquals(2, folders.size());
        assertEquals(child1.getId(), folders.get(0).getId());
        assertEquals(child2.getId(), folders.get(1).getId());
    }


    /**
     * Test.
     *
     * @throws RestException If the test fails.
     */
    public void testNameExistsInFolder() throws RestException {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page = tempPage(f.getId(), template.getId());

        // ACT
        final Boolean exists =
            getFolders().nameExistsInFolder(f.getId(), page.getName());

        // ASSERT
        assertTrue("Name should exists in the folder", exists.booleanValue());
        assertFalse("Name should not exists in the folder",
            getFolders().nameExistsInFolder(f.getId(), "foo").booleanValue());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testGetChildrenManualOrder() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page1 =
            tempPage(folder.getId(), template.getId());
        final ResourceSummary page2 =
            tempPage(folder.getId(), template.getId());
        final ResourceSummary page3 =
            tempPage(folder.getId(), template.getId());

        // ACT
        getCommands().lock(folder.getId());
        final List<String> sl  = new ArrayList<String>();
        sl.add(page2.getId().toString());
        sl.add(page1.getId().toString());
        sl.add(page3.getId().toString());

        final FolderDelta fd =
            new FolderDelta(ResourceOrder.DATE_CHANGED_ASC.name(), null, sl);

        getFolders().updateFolder(folder.getId(), fd);
        final ResourceSummary updated = getCommands().resource(folder.getId());

        final Collection<ResourceSummary> children =
            getFolders().getChildrenManualOrder(folder.getId());
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
     * @throws Exception If the test fails.
     */
    public void testGetChildren() throws Exception {

        // ARRANGE
        final ResourceSummary f = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page1 = tempPage(f.getId(), template.getId());
        pause(1000);
        final ResourceSummary page2 = tempPage(f.getId(), template.getId());
        pause(1000);
        final ResourceSummary page3 = tempPage(f.getId(), template.getId());

        // ACT
        getCommands().lock(f.getId());
        final List<String> sl  = new ArrayList<String>();
        sl.add(page2.getId().toString());
        sl.add(page1.getId().toString());
        sl.add(page3.getId().toString());

        final FolderDelta fd =
            new FolderDelta(ResourceOrder.DATE_CREATED_ASC.name(), null, sl);

        getFolders().updateFolder(f.getId(), fd);
        final ResourceSummary updated = getCommands().resource(f.getId());

        final List<ResourceSummary> list =
            new ArrayList<ResourceSummary>(getFolders().getChildren(f.getId()));

        // ASSERT
        assertNull(f.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(ResourceOrder.DATE_CREATED_ASC.name(),
            updated.getSortOrder());
        assertEquals(page1.getId(), list.get(0).getId());
        assertEquals(page2.getId(), list.get(1).getId());
        assertEquals(page3.getId(), list.get(2).getId());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testChangeFolderIndexPage() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        getCommands().lock(folder.getId());
        final List<String> sortList  = new ArrayList<String>();
        sortList.add(page.getId().toString());

        final FolderDelta fd =
            new FolderDelta(
                tempFolder().getSortOrder(), page.getId(), sortList);
        getFolders().updateFolder(folder.getId(), fd);
        final ResourceSummary updated = getCommands().resource(folder.getId());

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
     * @throws Exception If the test fails.
     */
    public void testSecurityBlocksFolderRead() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final UserDto user = tempUser();
        final UserDto me = getUsers().loggedInUser();
        final AclDto acl = new AclDto();
        final Entry e = new Entry();
        e._canRead = true;
        e._canWrite = true;
        e._principal = user.getId();
        acl.setUsers(Collections.singleton(e));
        getCommands().lock(folder.getId());
        getCommands().changeRoles(folder.getId(), acl);

        // ACT
        try {
            getFolders().getChildren(folder.getId());
            fail();

        // ASSERT
        } catch (final ClientResponseFailure ex) {
            final UnauthorizedException ue = convertException(ex);
            assertEquals(folder.getId(), ue.getTarget());
            assertEquals(me.getId(), ue.getUser());
        }
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testChangeFolderSortOrder() throws Exception {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final List<String> sortList  = new ArrayList<String>();
        // ACT
        getCommands().lock(folder.getId());
        final FolderDelta fd =
            new FolderDelta(
                ResourceOrder.DATE_CHANGED_ASC.name(), null, sortList);

        getFolders().updateFolder(folder.getId(), fd);
        final ResourceSummary updated = getCommands().resource(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(
            ResourceOrder.DATE_CHANGED_ASC.name(), updated.getSortOrder());
    }


    /**
     * Test.
     */
    public void testRoots() {

        // ACT
        final List<ResourceSummary> roots =
            new ArrayList<ResourceSummary>(getFolders().roots());

        // ASSERT
        assertEquals(1, roots.size());
        assertEquals(PredefinedResourceNames.CONTENT, roots.get(0).getName());

    }


    /**
     * Simple delay to overcome MySQL timestamp one second accuracy.
     * @param i
     */
    private void pause(final int i) {
        try {
            Thread.sleep(i);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
