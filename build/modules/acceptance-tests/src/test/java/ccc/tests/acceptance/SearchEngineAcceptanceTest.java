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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.ACL;
import ccc.api.core.File;
import ccc.api.core.Folder;
import ccc.api.core.Group;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.User;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;



/**
 * Tests for the action scheduler.
 *
 * @author Civic Computing Ltd.
 */
public class SearchEngineAcceptanceTest
    extends
        AbstractAcceptanceTest {


    /**
     * Test.
     */
    public void testStartStopSearchScheduler() {

        // ARRANGE

        // ACT
        final boolean startedAtFirst = getSearch().isRunning();

        getSearch().start();
        final boolean startedAfterStart = getSearch().isRunning();

        getSearch().stop();
        final boolean startedAfterStop = getSearch().isRunning();

        // ASSERT
        assertFalse(startedAtFirst);
        assertTrue(startedAfterStart);
        assertFalse(startedAfterStop);

        // FINALLY
        delay(); // Allow indexing to complete.
    }


    /**
     * Test.
     */
    public void testStartSchedulerIsIdempotent() {

        // ARRANGE

        // ACT
        try {
            getSearch().start();
            getSearch().start();
            getSearch().start();
        } finally {
            getSearch().stop();
        }

        // ASSERT

        // FINALLY
        delay(); // Allow indexing to complete.
    }


    /**
     * Test.
     */
    public void testFind() {

        // ARRANGE
        final String searchTerm = "veryunlikelysearchterm"+uid();
        final Resource parent = getCommands().resourceForPath("");
        final Page page   = tempPage(parent.getId(), null);

        final Resource metadata = new Resource();
        metadata.setTitle(searchTerm);
        metadata.setDescription(searchTerm);
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);
        getCommands().publish(page.getId());

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testFindChinese() {

        // ARRANGE
        final String searchTerm = "案例学习"+uid();
        final Resource parent = getCommands().resourceForPath("");
        final Page page   = tempPage(parent.getId(), null);

        final Resource metadata = new Resource();
        metadata.setTitle(searchTerm);
        metadata.setDescription(searchTerm);
        metadata.setTags(Collections.singleton(searchTerm));
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);
        getCommands().publish(page.getId());

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testFindByDate() {

        // ARRANGE
        final ResourceSummary parent = getCommands().resourceForPath("");
        final Page page   = tempPage(parent.getId(), null);

        updateMetadata(page);

        getSearch().index();
        delay();

        // ACT
        final SearchResult result =
            getSearch().find(
                "date_created:["+(page.getDateCreated().getTime()-1000)+" TO "+(page.getDateCreated().getTime()+1000)+"]", 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testFindPageById() {

        // ARRANGE
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary page   = tempPage(parent.getId(), null);

        getCommands().lock(page.getId());
        getCommands().publish(page.getId());

        getSearch().index();
        delay();

        // ACT
        final SearchResult result =
            getSearch().find("id:\""+page.getId()+"\" AND type:page", 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testFindFileById() {

        // ARRANGE
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary res   =
            getFiles().createTextFile(
                new File(
                    parent.getId(),
                    uid(),
                    MimeType.TEXT,
                    true,
                    "",
                    "Hello!"));

        getCommands().lock(res.getId());
        getCommands().publish(res.getId());

        getSearch().index();
        delay();

        // ACT
        final SearchResult result =
            getSearch().find("id:\""+res.getId()+"\" AND type:file", 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(res.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testFindAliasesFindsNothing() {

        // ARRANGE
        getSearch().index();

        // ACT
        final SearchResult result =
            getSearch().find("type:alias", 10, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
    }


    /**
     * Test.
     */
    public void testFindFileByContent() {

        // ARRANGE
        final ResourceSummary parent = getCommands().resourceForPath("");
        final String content = word();
        final ResourceSummary res   =
            getFiles().createTextFile(
                new File(
                    parent.getId(),
                    uid(),
                    MimeType.TEXT,
                    true,
                    "",
                    content));

        getCommands().lock(res.getId());
        getCommands().publish(res.getId());

        getSearch().index();
        delay();

        // ACT
        final SearchResult result =
            getSearch().find(content+" AND type:file", 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(res.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testSearchFindsSecureResourcesForUser() {

        // ARRANGE
        final User u = getUsers().retrieveCurrent();

        final String searchTerm = "bbbbbbbb"+uid();
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary page   = tempPage(parent.getId(), null);

        final Resource metadata = new Resource();
        metadata.setTitle(searchTerm);
        metadata.setDescription(searchTerm);
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);


        final ACL acl = getCommands().acl(page.getId());
        final ACL.Entry aclEntry = new ACL.Entry();
        aclEntry.setPrincipal(u.getId());
        aclEntry.setReadable(true);
        aclEntry.setWriteable(true);
        acl.setUsers(Collections.singletonList(aclEntry));

        getCommands().changeAcl(page.getId(), acl);
        getCommands().publish(page.getId());

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(1, result.hits().size());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testSearchFindsSecureResourcesForGroup() {

        // ARRANGE
        final Group g = tempGroup();

        final User u = getUsers().retrieveCurrent();
        final Set<UUID> groups = u.getGroups();
        groups.add(g.getId());
        u.setGroups(groups);
        getUsers().update(u.getId(), u);

        final String searchTerm = "dddddddd"+uid();
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary page   = tempPage(parent.getId(), null);

        final Resource metadata = new Resource();
        metadata.setTitle(searchTerm);
        metadata.setDescription(searchTerm);
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);


        final ACL acl = getCommands().acl(page.getId());
        final ACL.Entry aclEntry = new ACL.Entry();
        aclEntry.setPrincipal(g.getId());
        aclEntry.setReadable(true);
        aclEntry.setWriteable(true);
        acl.setGroups(Collections.singletonList(aclEntry));

        getCommands().changeAcl(page.getId(), acl);
        getCommands().publish(page.getId());

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result = getSearch().find(searchTerm, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(1, result.hits().size());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Create a temporary group for testing.
     *
     * @return The new group.
     */
    private Group tempGroup() {
        final Group group = new Group();
        group.setName(uid());
        return getGroups().create(group);
    }


    /**
     * Test.
     */
    public void testSearchRejectsSecureResourcesForUnauthorisedUsers() {

        // ARRANGE
        final User u = tempUser();

        final String searchTerm = "cccccccc"+uid();
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary page   = tempPage(parent.getId(), null);

        final Resource metadata = new Resource();
        metadata.setTitle(searchTerm);
        metadata.setDescription(searchTerm);
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);


        final ACL acl = getCommands().acl(page.getId());
        final ACL.Entry aclEntry = new ACL.Entry();
        aclEntry.setPrincipal(u.getId());
        aclEntry.setReadable(true);
        aclEntry.setWriteable(true);
        acl.setUsers(Collections.singletonList(aclEntry));

        getCommands().publish(page.getId());
        getCommands().changeAcl(page.getId(), acl);

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(0, result.hits().size());
    }


    /**
     * Test.
     */
    public void testIdSearch() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page page = tempPage(parent.getId(), null);
        final String searchTerm = "id:"+page.getId();

        updateMetadata(page);

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testPathSearch() {

        // ARRANGE
        final Folder parent = tempFolder();
        final Page page = tempPage(parent.getId(), null);
        final String searchTerm = "path:/content"+parent.getAbsolutePath()+"*";

        final Resource metadata = new Resource();
        metadata.setTitle("pathTest1");
        metadata.setDescription("");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));

        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);

        getCommands().lock(parent.getId());
        getCommands().updateMetadata(parent.getId(), metadata);

        getCommands().publish(parent.getId());
        getCommands().publish(page.getId());

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testNameSearch() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page page = tempPage(parent.getId(), null);
        final String searchTerm = "name:"+page.getName();

        updateMetadata(page);

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testTitleSearch() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page page = tempPage(parent.getId(), null);
        page.setTitle("searchTitle"+page.getId());
        updateMetadata(page);
        final String searchTerm = "title:"+page.getTitle();

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }

    /**
     * Test.
     */
    public void testTagSearch() {

        // ARRANGE
        final ResourceSummary parent = getCommands().resourceForPath("");
        final ResourceSummary page   = tempPage(parent.getId(), null);

        final String term = "sampleword"+uid();

        final Resource metadata = new Resource();
        metadata.setTitle(page.getTitle());
        metadata.setDescription("");

        final HashSet<String> tags = new HashSet<String>();
        tags.add("not important");
        tags.add(term);
        tags.add("test");

        metadata.setTags(tags);
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);
        getCommands().publish(page.getId());

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find("tags:"+term, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     *
     */
    public void testDateSearch() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page page = tempPage(parent.getId(), null);

        final Date testDate = new Date();

        updateMetadata(page);
        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        paragraphs.add(Paragraph.fromDate("testDate", testDate));

        page.setParagraphs(paragraphs);
        getPages().update(page.getId(), page);

        final String searchTerm = "testDate:["+(testDate.getTime()-1000)
        +" TO "+(testDate.getTime()+1000)+"]";

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testNumericSearch() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page page = tempPage(parent.getId(), null);

        final int testInt = new Random().nextInt();

        final BigDecimal testNumber = new BigDecimal(testInt);

        updateMetadata(page);
        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        paragraphs.add(Paragraph.fromNumber("testNumber", testNumber));

        page.setParagraphs(paragraphs);
        getPages().update(page.getId(), page);

        final String searchTerm = "testNumber:["+testInt+".0 TO "+testInt+".0]";

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testBooleanSearch() {

        // ARRANGE
        final Resource parent = getCommands().resourceForPath("");
        final Page page = tempPage(parent.getId(), null);

        final int testInt = new Random().nextInt();

        updateMetadata(page);
        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        paragraphs.add(Paragraph.fromBoolean("testBoolean"+testInt, false));

        page.setParagraphs(paragraphs);
        getPages().update(page.getId(), page);

        final String searchTerm = "testBoolean"+testInt+":false";

        getSearch().index();
        delay(); // Allow indexing to complete.

        // ACT
        final SearchResult result =
            getSearch().find(searchTerm, "title", SortOrder.ASC, 10, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(page.getId(), result.hits().iterator().next());
    }


    private void updateMetadata(final Resource page) {
        final Resource metadata = new Resource();
        metadata.setTitle(page.getTitle());
        metadata.setDescription("");
        metadata.setTags(new HashSet<String>());
        metadata.setMetadata(Collections.singletonMap("searchable", "true"));
        getCommands().lock(page.getId());
        getCommands().updateMetadata(page.getId(), metadata);
        getCommands().publish(page.getId());
    }


    private void delay() {
        try {
            final int twentySecs = 20000;
            Thread.sleep(twentySecs);
        } catch (final InterruptedException e) {
            throw new RuntimeException("Delay failed.", e);
        }
    }
}
