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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ccc.api.core.Folder;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.ACL;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.ResourceName;
import ccc.api.types.SortOrder;
import ccc.api.types.ACL.Entry;


/**
 * Acceptance tests for Folders REST methods.
 *
 * @author Civic Computing Ltd.
 */
public class FolderAcceptanceTest extends AbstractAcceptanceTest {

    private static final int ONE_SECOND = 1000;


    /**
     * Test.
     */
    public void testGetFolderChildren() {

        // ARRANGE
        final String fName = UUID.randomUUID().toString();
        final String cn1 = "a-"+UUID.randomUUID().toString();
        final String cn2 = "b-"+UUID.randomUUID().toString();
        final ResourceSummary content =
            getCommands().resourceForPath("");
        final ResourceSummary testFolder =
            getFolders().create(
                new Folder(content.getId(), new ResourceName(fName)));
        final ResourceSummary child1 =
            getFolders().create(
                new Folder(testFolder.getId(), new ResourceName(cn1)));
        final ResourceSummary child2 =
            getFolders().create(
                new Folder(testFolder.getId(), new ResourceName(cn2)));

        // ACT
        final List<ResourceSummary> folders =
            getCommands().list(testFolder.getId(),
                null,
                null,
                null,
                null,
                "folder",
                null,
                null,
                "name",
                SortOrder.ASC,
                1,
                1000).getElements();

        // ASSERT
        assertEquals(2, folders.size());
        assertEquals(child1.getId(), folders.get(0).getId());
        assertEquals(child2.getId(), folders.get(1).getId());
    }


    /**
     * Test.
     */
    public void testNameExistsInFolder() {

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
     */
    public void testGetChildrenManualOrder() {

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

        final Folder fd = new Folder();
        fd.setSortList(sl);

        getFolders().update(folder.getId(), fd);
        final ResourceSummary updated = getCommands().retrieve(folder.getId());

        final PagedCollection<ResourceSummary> children =
            getCommands().list(folder.getId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "manual",
                SortOrder.ASC,
                1,
                100);
        final List<ResourceSummary> list =
            new ArrayList<ResourceSummary>(children.getElements());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(page2.getId(), list.get(0).getId());
        assertEquals(page1.getId(), list.get(1).getId());
        assertEquals(page3.getId(), list.get(2).getId());
    }


    /**
     * Test.
     */
    public void testChangeFolderIndexPage() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final ResourceSummary template =
            dummyTemplate(getCommands().resourceForPath(""));
        final ResourceSummary page = tempPage(folder.getId(), template.getId());

        // ACT
        getCommands().lock(folder.getId());
        final List<String> sortList  = new ArrayList<String>();
        sortList.add(page.getId().toString());

        final Folder fd = new Folder();
        fd.setSortList(sortList);
        fd.setIndexPage(page.getId());

        getFolders().update(folder.getId(), fd);
        final ResourceSummary updated = getCommands().retrieve(folder.getId());

        // ASSERT
        assertNull(folder.getLockedBy());
        assertNotNull(updated.getLockedBy());
        assertEquals(page.getId(), updated.getIndexPageId());
        assertEquals(1, updated.getChildCount());
    }


    /**
     * Test.
     */
    public void testSecurityBlocksFolderRead() {

        // ARRANGE
        final ResourceSummary folder = tempFolder();
        final User user = tempUser();
        final User me = getUsers().retrieveCurrent();
        final ACL acl = new ACL();
        final Entry e = new Entry();
        e._canRead = true;
        e._canWrite = true;
        e._principal = user.getId();
        acl.setUsers(Collections.singleton(e));
        getCommands().lock(folder.getId());
        getCommands().changeAcl(folder.getId(), acl);

        // ACT
        try {
            final PagedCollection<ResourceSummary> list =
                getCommands().list(folder.getId(),
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
            fail();

        // ASSERT
        } catch (final UnauthorizedException ex) {
            assertEquals(folder.getId(), ex.getTarget());
            assertEquals(me.getId(), ex.getUser());
        }
    }


    /**
     * Test.
     */
    public void testRoots() {

        // ACT
        final PagedCollection<ResourceSummary> roots = getFolders().roots();

        // ASSERT
        assertEquals(1, roots.getTotalCount());
        assertEquals(PredefinedResourceNames.CONTENT,
            roots.getElements().get(0).getName());

    }

}
