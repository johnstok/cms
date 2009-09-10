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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.rest.RestException;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceSummary;
import ccc.types.PredefinedResourceNames;
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
        final String cn1 = UUID.randomUUID().toString();
        final String cn2 = UUID.randomUUID().toString();
        final ResourceSummary content = resourceForPath("/content");
        final ResourceSummary testFolder =
            _folders.createFolder(new FolderDto(content.getId(), fName));
        final ResourceSummary child1 =
            _folders.createFolder(new FolderDto(testFolder.getId(), cn1));
        final ResourceSummary child2 =
            _folders.createFolder(new FolderDto(testFolder.getId(), cn2));

        // ACT
        final List<ResourceSummary> folders = new ArrayList<ResourceSummary>(
            _folders.getFolderChildren(testFolder.getId()));

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
            dummyTemplate(resourceForPath("/content"));
        final ResourceSummary page = tempPage(f.getId(), template.getId());

        // ACT
        final Boolean exists =
            _folders.nameExistsInFolder(f.getId(), page.getName());

        // ASSERT
        assertTrue("Name should exists in the folder", exists.booleanValue());
        assertFalse("Name should not exists in the folder",
            _folders.nameExistsInFolder(f.getId(), "foo").booleanValue());
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
            dummyTemplate(resourceForPath("/content"));
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
            dummyTemplate(resourceForPath("/content"));
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
    public void testRoots() {

        // ACT
        final List<ResourceSummary> roots =
            new ArrayList<ResourceSummary>(_folders.roots());

        // ASSERT
        assertEquals(2, roots.size());
        assertEquals(PredefinedResourceNames.ASSETS, roots.get(0).getName());
        assertEquals(PredefinedResourceNames.CONTENT, roots.get(1).getName());

    }
}