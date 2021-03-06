/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.plugins.search.lucene;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.api.core.ACL;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.SearchResult;
import ccc.api.types.SortOrder;
import ccc.plugins.search.TextExtractor;


/**
 * Tests for the {@link SimpleLuceneFS} class.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleLuceneFSTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testSimilarHandlesMissingUuid() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test7/lucene").getAbsolutePath());

        // ACT
        final SearchResult result = searchEngine.similar(null, 5, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(result.hits().size(), 0);
    }

    /**
     * Test.
     */
    public void testFindHandlesMissingIndex() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test6/lucene").getAbsolutePath());

        // ACT
        final SearchResult result = searchEngine.find("foo", 5, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(result.hits().size(), 0);
    }

    /**
     * Test.
     */
    public void testFindHandlesInvalidSearchTerm() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test8/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            id,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            Collections.singleton("foo"),
            "foo",
            new HashSet<Paragraph>());
        searchEngine.commitUpdate();
        final SearchResult result = searchEngine.find("hi there!", 5, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(result.hits().size(), 0);
    }

    /**
     * Test.
     */
    public void testFindHandlesEmptySearchTerm() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test5/lucene").getAbsolutePath());

        // ACT
        final SearchResult result = searchEngine.find("", 5, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(result.hits().size(), 0);
    }


    public void testSecureResourcesHiddenFromAnonymousUser() {

        // ARRANGE
        final UUID allowedPrincipal = UUID.randomUUID();
        final ACL allowedAcl = createAcl(allowedPrincipal);

        final UUID id = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test14/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            UUID.randomUUID(),
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
            "foo",
            new HashSet<Paragraph>(),
            Collections.singletonList(allowedAcl));
        searchEngine.createDocument(
            id,
            new ResourcePath("/bar"),
            new ResourceName("bar"),
            "foo",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
            "foo",
            new HashSet<Paragraph>(),
            Collections.<ACL>emptyList());
        searchEngine.commitUpdate();
        final SearchResult result = searchEngine.find("foo", 5, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testSecureResourcesVisibleToPermittedUser() {

        // ARRANGE
        final UUID allowedGroup = UUID.randomUUID();
        final ACL allowedAcl = createAcl(allowedGroup);

        final ACL userAcl = createAcl(allowedGroup);

        final UUID id = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test15/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            id,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
            "foo",
            new HashSet<Paragraph>(),
            Collections.singletonList(allowedAcl));
        searchEngine.commitUpdate();
        final SearchResult result =
            searchEngine.find("foo", "title", SortOrder.ASC, userAcl, 5, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testSecureResourcesHiddenFromNonpermittedUser() {

        // ARRANGE
        final UUID allowedGroup = UUID.randomUUID();
        final ACL allowedAcl = createAcl(allowedGroup);

        final UUID disallowedGroup = UUID.randomUUID();
        final ACL userAcl = createAcl(disallowedGroup);

        final UUID id = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test15/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            id,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
            "foo",
            new HashSet<Paragraph>(),
            Collections.singletonList(allowedAcl));
        searchEngine.commitUpdate();
        final SearchResult result =
            searchEngine.find("foo", "title", SortOrder.ASC, userAcl, 5, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(result.hits().size(), 0);
    }


    /**
     * Test.
     */
    public void testSuccessfulIndexAndFind() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test1/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            id,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
            "foo",
            new HashSet<Paragraph>());
        searchEngine.commitUpdate();
        final SearchResult result = searchEngine.find("foo", 5, 1);

        // ASSERT
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testIndexAndSortedFind() {

        // ARRANGE
        final UUID capM = UUID.randomUUID();
        final UUID a = UUID.randomUUID();
        final UUID m = UUID.randomUUID();
        final UUID z = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/lucene/IndexAndSortedFind").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            m,
            new ResourcePath("/m"),
            new ResourceName("m"),
            "m",
            new HashSet<String>(),
            "foo,foo,foo",
            new HashSet<Paragraph>());
        searchEngine.createDocument(
            z,
            new ResourcePath("/z"),
            new ResourceName("z"),
            "z",
            new HashSet<String>(),
            "foo,foo",
            new HashSet<Paragraph>());
        searchEngine.createDocument(
            capM,
            new ResourcePath("/M"),
            new ResourceName("M"),
            "M",
            new HashSet<String>(),
            "foo",
            new HashSet<Paragraph>());
        searchEngine.createDocument(
            a,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo",
            new HashSet<Paragraph>());
        searchEngine.commitUpdate();
        final SearchResult result =
            searchEngine.find("foo", "_title", SortOrder.ASC, 5, 1);

        // ASSERT
        assertEquals(4, result.totalResults());
        assertEquals(4, result.hits().size());
        final Iterator<UUID> i = result.hits().iterator();
        assertEquals(a, i.next());
        assertEquals(m, i.next()); // More relevant than capM.
        assertEquals(capM, i.next());
        assertEquals(z, i.next());
    }


    /**
     * Test.
     */
    public void testIndexAndSortedDateFind() {

        // ARRANGE
        final UUID a = UUID.randomUUID();
        final UUID m = UUID.randomUUID();
        final UUID z = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/lucene/IndexAndSortedDateFind")
                .getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            m,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo,foo,foo",
            Collections.singleton(Paragraph.fromDate("bar", new Date())));
        searchEngine.createDocument(
            z,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo,foo",
            Collections.singleton(
                Paragraph.fromDate("bar", new Date(Long.MAX_VALUE))));
        searchEngine.createDocument(
            a,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo",
            Collections.singleton(
                Paragraph.fromDate("bar", new Date(Long.MIN_VALUE))));
        searchEngine.commitUpdate();
        final SearchResult result =
            searchEngine.find("foo", "_bar", SortOrder.ASC, 5, 1);

        final SearchResult reversedResult =
            searchEngine.find("foo", "_bar", SortOrder.DESC, 5, 1);

        // ASSERT
        assertEquals(3, result.totalResults());
        assertEquals(result.hits().size(), 3);
        final Iterator<UUID> i = result.hits().iterator();
        assertEquals(a, i.next());
        assertEquals(m, i.next());
        assertEquals(z, i.next());

        assertEquals(3, reversedResult.totalResults());
        assertEquals(reversedResult.hits().size(), 3);
        final Iterator<UUID> u = reversedResult.hits().iterator();
        assertEquals(z, u.next());
        assertEquals(m, u.next());
        assertEquals(a, u.next());
    }


    /**
     * Test.
     */
    public void testIndexAndSortedBooleanFind() {

        // ARRANGE
        final UUID a = UUID.randomUUID();
        final UUID m = UUID.randomUUID();
        final UUID z = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/lucene/IndexAndSortedBooleanFind")
                .getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            m,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo,foo,foo",
            Collections.singleton(Paragraph.fromBoolean("bar", Boolean.TRUE)));
        searchEngine.createDocument(
            z,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo,foo",
            Collections.singleton(Paragraph.fromBoolean("bar", Boolean.FALSE)));
        searchEngine.createDocument(
            a,
            new ResourcePath("/a"),
            new ResourceName("a"),
            "a",
            new HashSet<String>(),
            "foo",
            Collections.singleton(Paragraph.fromBoolean("bar", Boolean.TRUE)));
        searchEngine.commitUpdate();
        final SearchResult result =
            searchEngine.find("foo", "_bar", SortOrder.ASC, 5, 1);

        // ASSERT
        assertEquals(3, result.totalResults());
        assertEquals(result.hits().size(), 3);
        final Iterator<UUID> i = result.hits().iterator();
        assertEquals(z, i.next());
        assertEquals(m, i.next());
        assertEquals(a, i.next());

        // Note: 'false' sorts before 'true' lexicographically.
    }


    /**
     * Test.
     */
    public void testCreatePlainTextExtractor() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test9/lucene").getAbsolutePath());

        // ACT
        final TextExtractor extractor =
            searchEngine.createExtractor(new MimeType("text", "plain"));

        // ASSERT
        assertEquals(TxtExtractor.class, extractor.getClass());
    }


    /**
     * Test.
     */
    public void testCreatePdfExtractor() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test10/lucene").getAbsolutePath());

        // ACT
        final TextExtractor extractor =
            searchEngine.createExtractor(new MimeType("application", "pdf"));

        // ASSERT
        assertEquals(PdfLoader.class, extractor.getClass());
    }


    /**
     * Test.
     */
    public void testCreateMswordExtractor() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test11/lucene").getAbsolutePath());

        // ACT
        final TextExtractor extractor =
            searchEngine.createExtractor(new MimeType("application", "msword"));

        // ASSERT
        assertEquals(WordExtractor.class, extractor.getClass());
    }


    /**
     * Test.
     */
    public void testCreateExtractorHandlesUnknownMimetype() {

        // ARRANGE
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test12/lucene").getAbsolutePath());

        // ACT
        final TextExtractor extractor =
            searchEngine.createExtractor(new MimeType("application", "foo"));

        // ASSERT
        assertNull(extractor);
    }


    /**
     * Test.
     */
    public void testSuccessfulIndexAndFindSimilar() {

        // ARRANGE
        final UUID id1 = UUID.randomUUID();
        final UUID id2 = UUID.randomUUID();
        final UUID id3 = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test4/lucene").getAbsolutePath());

        // ACT
        final Paragraph p1 = Paragraph.fromText("content",
         "Test text that is similar to the other text in the same test case.");
        final HashSet<Paragraph> paras1 = new HashSet<Paragraph>();
        paras1.add(p1);

        final Paragraph p2 = Paragraph.fromText("content",
        "Test text that is quite similar to the other text in the same  case.");
        final HashSet<Paragraph> paras2 = new HashSet<Paragraph>();
        paras2.add(p2);

        final Paragraph p3 = Paragraph.fromText("content",
        "Totally different store here with no common parts.");
        final HashSet<Paragraph> paras3 = new HashSet<Paragraph>();
        paras3.add(p3);

        searchEngine.startUpdate();
        searchEngine.createDocument(
            id1,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            Collections.singleton("foo"),
            p1.getText(),
            paras1);
        searchEngine.createDocument(
            id2,
            new ResourcePath("/bar"),
            new ResourceName("foo"),
            "foo",
            Collections.singleton("foo"),
            p2.getText(),
            paras2);
        searchEngine.createDocument(
            id3,
            new ResourcePath("/baz"),
            new ResourceName("foo"),
            "foo",
            Collections.singleton("foo"),
            p3.getText(),
            paras3);
        searchEngine.commitUpdate();
        final SearchResult result =
            searchEngine.similar(id1.toString(), 5, 1);

        // ASSERT
        assertEquals(2, result.totalResults());
        assertEquals(result.hits().size(), 2);
        assertEquals(true, result.hits().contains(id1));
        assertEquals(true, result.hits().contains(id2));
        assertEquals(false, result.hits().contains(id3));
    }


    /**
     * Test.
     */
    public void testRollbackOfIndex() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test2/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            id,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            Collections.singleton("foo"),
            "foo",
            new HashSet<Paragraph>());
        searchEngine.rollbackUpdate();
        final SearchResult result = searchEngine.find("foo", 5, 1);

        // ASSERT
        assertEquals(0, result.totalResults());
        assertEquals(result.hits().size(), 0);
    }

    /**
     * Test.
     */
    public void testIndexParagraphs() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final Date date = new Date();
        final Set<Paragraph> paras =
            new HashSet<Paragraph>(){{
                add(Paragraph.fromBoolean("bool", Boolean.TRUE));
                add(Paragraph.fromDate("date", date));
                add(Paragraph.fromText("text", "text"));
                add(Paragraph.fromNumber("num", 1));
            }};
        final SimpleLuceneFS searchEngine =
            new SimpleLuceneFS(
                new File("target/test3/lucene").getAbsolutePath());

        // ACT
        searchEngine.startUpdate();
        searchEngine.createDocument(
            id,
            new ResourcePath("/foo"),
            new ResourceName("foo"),
            "foo",
            Collections.singleton("foo"),
            "foo",
            paras);
        searchEngine.commitUpdate();

        // ASSERT
        SearchResult result = searchEngine.find("text:text", 5, 1);
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());

        result = searchEngine.find("bool:true", 5, 1);
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());

        final StringBuilder sb = new StringBuilder();
        sb.append("date:[");
        sb.append(date.getTime());
        sb.append(" TO ");
        sb.append(date.getTime());
        sb.append("]");

        result = searchEngine.find(sb.toString(), 5, 1);
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());

        result = searchEngine.find("num:[1.0 TO 1.0]", 5, 1);
        assertEquals(1, result.totalResults());
        assertEquals(result.hits().size(), 1);
        assertEquals(id, result.hits().iterator().next());
    }


    /**
     * Test.
     */
    public void testIndexTaxonomy() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final UUID id2 = UUID.randomUUID();
        final Set<Paragraph> paras =
            new HashSet<Paragraph>(){{
                final List<String> terms =
                    Arrays.asList("1.2,1.7,1.8,2".split(","));
                add(Paragraph.fromTaxonomy("text", terms));
            }};
            final Set<Paragraph> paras2 =
                new HashSet<Paragraph>(){{
                    final List<String> terms =
                        Arrays.asList("1.3,1.1,1.8,2".split(","));
                    add(Paragraph.fromTaxonomy("text", terms));
                }};
            final SimpleLuceneFS searchEngine =
                new SimpleLuceneFS(
                    new File("target/test13/lucene").getAbsolutePath());

            // ACT
            searchEngine.startUpdate();
            searchEngine.createDocument(
                id,
                new ResourcePath("/foo"),
                new ResourceName("foo"),
                "foo",
                Collections.singleton("foo"),
                "foo",
                paras);
            searchEngine.createDocument(
                id2,
                new ResourcePath("/foo2"),
                new ResourceName("foo2"),
                "foo2",
                Collections.singleton("foo2"),
                "foo2",
                paras2);
            searchEngine.commitUpdate();

            // ASSERT
            final SearchResult result = searchEngine.find("text:1.8", 5, 1);
            assertEquals(2, result.totalResults());
            assertEquals(result.hits().size(), 2);
            assertEquals(id, result.hits().iterator().next());

    }


    private ACL createAcl(final UUID principal) {
        final ACL.Entry entry = new ACL.Entry();
        entry.setPrincipal(principal);
        entry.setReadable(true);
        final ACL acl = new ACL();
        acl.setGroups(Collections.singletonList(entry));
        return acl;
    }

}
