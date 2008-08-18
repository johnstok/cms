/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3;

import static ccc.domain.PredefinedResourceNames.*;
import static ccc.services.ejb3.Queries.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.services.ContentManager;


/**
 * Tests for the {@link ContentManagerEJB} class.
 *
 * @author Civic Computing Ltd
 */
public final class ContentManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Template defaultTemplate = new Template("foo", "bar", "baz");

        final EntityManager em = new EntityManagerAdaptor() {
            /** {@inheritDoc} */ @Override
            public Query createNamedQuery(final String arg0) {
                return new QueryAdaptor() {
                    /** {@inheritDoc} */ @Override
                    public Object getSingleResult() { return contentRoot; }
                };
            }
        };

        final ContentManagerEJB resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.setDefaultTemplate(defaultTemplate);

        // ASSERT
        assertEquals(
            defaultTemplate,
            contentRoot.displayTemplateName());
    }

    /**
     * Test.
     */
    public void testLookup() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Page bar = new Page(new ResourceName("bar"))
                                    .addParagraph(
                                        "default",
                                        new Paragraph("<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        final EntityManager em = new EntityManagerAdaptor() {
            /**@see EntityManagerAdaptor#createQuery(java.lang.String)*/
            @Override
            public Query createNamedQuery(final String arg0) {
                return new QueryAdaptor() {
                    /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                    @Override
                    public Object getSingleResult() { return contentRoot; }
                };
            }
        };

        final ContentManagerEJB resourceMgr = new ContentManagerEJB(em);

        // ACT
        final Resource resource =
            resourceMgr.lookup(new ResourcePath("/foo/bar/"));

        // ASSERT
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.paragraphs().size());
    }

    /**
     * Test.
     */
    public  void testCreateFolder() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder bar = new Folder(new ResourceName("bar"));
        final Folder baz = new Folder(new ResourceName("baz"));

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        em.persist(foo);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        em.persist(bar);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        em.persist(baz);
        replay(em);

        final ContentManager resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.create(contentRoot.id(), foo);
        resourceMgr.create(foo.id(), bar);
        resourceMgr.create(foo.id(), baz);

        // VERIFY
        verify(em);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(2, contentRoot.entries().get(0).as(Folder.class).size());
        assertEquals(
            "baz",
            contentRoot
                .entries().get(0).as(Folder.class)
                .entries().get(1).name().toString());

    }

    /**
     * Test.
     */
    public void testCreateRoot() {

        // ARRANGE
        final Capture<Folder> contentRoot = new Capture<Folder>();
        final EntityManager em = createMock(EntityManager.class);
        expect(em.createNamedQuery(RESOURCE_BY_URL))
            .andThrow(new NoResultException());
        em.persist(capture(contentRoot));
        replay(em);


        final ContentManager resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.createRoot();

        // VERIFY
        verify(em);
        assertEquals(CONTENT, contentRoot.getValue().name());
    }

    /**
     * Test.
     */
    public void testCreateRootIsIdempotent() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);

        final EntityManager em = createMock(EntityManager.class);
        expect(em.createNamedQuery(RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                @Override
                public Object getSingleResult() { return contentRoot; }
            });
        replay(em);

        final ContentManager resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.createRoot();

        // VERIFY
        verify(em);
    }

    /**
     * Test.
     */
    public void testCreateContent() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Page page1 = new Page(new ResourceName("page1"));
        final Page page2 = new Page(new ResourceName("page2"));

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        em.persist(foo);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        em.persist(page1);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        em.persist(page2);
        replay(em);

        final ContentManager resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.create(contentRoot.id(), foo);
        resourceMgr.create(foo.id(), page1);
        resourceMgr.create(foo.id(), page2);

        // VERIFY
        verify(em);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(2, contentRoot.entries().get(0).as(Folder.class).size());

        final List<Resource> entries =
            contentRoot.entries().get(0).as(Folder.class).entries();
        assertEquals(ResourceType.PAGE, entries.get(0).type());
        assertEquals(ResourceType.PAGE, entries.get(1).type());
        assertEquals("page1", entries.get(0).name().toString());
        assertEquals("page2", entries.get(1).name().toString());
    }

    /**
     * Test.
     */
    public void testCreateContentFailsWhenFolderExists() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Folder page1F = new Folder(new ResourceName("page1"));
        final Page page1P = new Page(new ResourceName("page1"));

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        em.persist(foo);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        em.persist(page1F);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        replay(em);

        final ContentManager resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.create(contentRoot.id(), foo);
        resourceMgr.create(foo.id(), page1F);
        try {
            resourceMgr.create(foo.id(), page1P);
            fail("Creation of page with"
                    + "same name as existing folder should fail.");
        } catch (final CCCException e) {
            assertEquals(
                "Folder already contains a resource with name 'page1'.",
                e.getMessage());
        }

        // VERIFY
        verify(em);
        assertEquals(1, contentRoot.size());
        assertEquals(1, contentRoot.entries().size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(
            "page1",
            contentRoot
                .entries().get(0).as(Folder.class)
                .entries().get(0).name().toString());
    }

    /**
     * Test.
     */
    public void testCreateParagraph() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Page page1 = new Page(new ResourceName("page1"));
        page1.addParagraph("HEADER", new Paragraph("test text"));

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, contentRoot.id()))
            .andReturn(contentRoot);
        em.persist(foo);
        expect(em.find(Resource.class, foo.id())).andReturn(foo);
        em.persist(page1);
        replay(em);

        final ContentManager resourceMgr = new ContentManagerEJB(em);

        // ACT
        resourceMgr.create(contentRoot.id(), foo);
        resourceMgr.create(foo.id(), page1);

        // VERIFY
        verify(em);
        assertEquals(1, contentRoot.size());
        assertEquals(1, contentRoot.entries().size());

        final Folder folder = contentRoot.entries().get(0).as(Folder.class);
        assertEquals("foo", folder.name().toString());

        final Page page = folder.entries().get(0).as(Page.class);
        assertEquals("page1", page.name().toString());

        assertEquals(1, page.paragraphs().size());
        assertEquals("test text", page.paragraphs().get("HEADER").body());
    }

    /**
     * Test.
     */
    public void testLookupFromId() {

        // ARRANGE
        final Page bar = new Page(new ResourceName("bar"))
                                    .addParagraph(
                                        "default",
                                        new Paragraph("<H1>Default</H1>"));

        final EntityManager em = new SimpleEM(bar);

        final ContentManagerEJB resourceMgr = new ContentManagerEJB(em);

        // ACT
        final Resource resource =
            resourceMgr.lookup(bar.id());

        // ASSERT
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.paragraphs().size());
    }

    /**
     * Test.
     */
    public void testSaveContent() {

        // ARRANGE
        final Page page = new Page(new ResourceName("test"));
        page.addParagraph("abc", new Paragraph("def"));

        final EntityManager em = new SimpleEM(page);

        final ContentManagerEJB resourceMgr = new ContentManagerEJB(em);

        // ACT
        final Map<String, String> paragraphs = new HashMap<String, String>();
        paragraphs.put("foo", "bar");
        resourceMgr.update(
            page.id(),
            "new title", paragraphs);

        // ASSERT
        assertEquals("new title", page.title());
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().keySet().iterator().next());
        assertEquals("bar", page.paragraphs().get("foo").body());
    }

    /**
     * Helper class for testing.
     *
     * @author Civic Computing Ltd
     */
    private final class SimpleEM extends EntityManagerAdaptor {

        /** _page : Page. */
        private final Page _page;

        /**
         * Constructor.
         *
         * @param page The page to return from find().
         */
        private SimpleEM(final Page page) {

            _page = page;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T find(final Class<T> type, final Object id) {
            return (T) _page;
        }
    }
}
