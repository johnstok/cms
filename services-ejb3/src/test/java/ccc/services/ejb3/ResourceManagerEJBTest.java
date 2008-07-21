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
import static ccc.domain.Queries.*;
import static org.easymock.EasyMock.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.Content;
import ccc.domain.Folder;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Queries;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;
import ccc.services.ResourceManager;


/**
 * Tests for the {@link ResourceManagerEJB} class.
 *
 * @author Civic Computing Ltd
 */
public final class ResourceManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public final void testLookup() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Content bar = new Content(new ResourceName("bar"))
                                    .addParagraph(
                                        "default",
                                        new Paragraph("<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        final EntityManager em = new EntityManagerAdaptor() {
            /**@see EntityManagerAdaptor#createQuery(java.lang.String)*/
            @Override public Query createNamedQuery(String arg0) {
                return new QueryAdaptor() {
                    /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                    @Override
                    public Object getSingleResult() { return contentRoot; }
                };
            }
        };

        final ResourceManagerEJB resourceMgr = new ResourceManagerEJB(em);

        // ACT
        final Resource resource = resourceMgr.lookup(new ResourcePath("/foo/bar"));

        // ASSERT
        assertEquals(ResourceType.CONTENT, resource.type());
        final Content content = resource.asContent();
        assertEquals(1, content.paragraphs().size());
    }

    /**
     * Test.
     */
    public void testCreateFolder() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);

        final EntityManager em = createMock(EntityManager.class);
        expect(em.createNamedQuery(Queries.RESOURCE_BY_URL))
            .andReturn(new QueryAdaptor() {
                /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                @Override
                public Object getSingleResult() { return contentRoot; }
            })
            .anyTimes();
        em.persist(isA(Folder.class));
        em.persist(isA(Folder.class));
        em.persist(isA(Folder.class));
        replay(em);

        final ResourceManager resourceMgr = new ResourceManagerEJB(em);

        // ACT
        resourceMgr.createFolder("/foo/bar");
        resourceMgr.createFolder("/foo/baz");

        // VERIFY
        verify(em);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
        assertEquals(2, contentRoot.entries().get(0).asFolder().size());
        assertEquals(
            "baz",
            contentRoot
                .entries().get(0).asFolder()
                .entries().get(1).name().toString());

    }

    /**
     * Test.
     */
    public void testCreateFolderIsIdempotent() {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);

        final EntityManager em = createMock(EntityManager.class);
        expect(em.createNamedQuery(Queries.RESOURCE_BY_URL))
            .andReturn(new QueryAdaptor() {
                /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                @Override
                public Object getSingleResult() { return contentRoot; }
            })
            .anyTimes();
        em.persist(isA(Folder.class));
        replay(em);

        final ResourceManager resourceMgr = new ResourceManagerEJB(em);

        // ACT
        resourceMgr.createFolder("/foo");
        resourceMgr.createFolder("/foo");
        resourceMgr.createFolder("/foo");

        // VERIFY
        verify(em);
        assertEquals(1, contentRoot.size());
        assertEquals("foo", contentRoot.entries().get(0).name().toString());
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


        final ResourceManager resourceMgr = new ResourceManagerEJB(em);

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

        final ResourceManager resourceMgr = new ResourceManagerEJB(em);

        // ACT
        resourceMgr.createRoot();

        // VERIFY
        verify(em);
    }

    // testNullEntittMgrRejected

}
