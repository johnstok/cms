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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.AssetManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public final class AssetManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testLookupTemplates() {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final Folder templateFolder = new Folder(new ResourceName("templates"));
        final Template expected = new Template("title", "description", "body");
        assetRoot.add(templateFolder);
        templateFolder.add(expected);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery(Queries.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /**  {@inheritDoc} */ @Override
                public Object getSingleResult() { return assetRoot; }
            });
        replay(em);

        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        final List<Template> templates = am.lookupTemplates();

        // ASSERT
        verify(em);
        assertEquals(1, templates.size());
        assertEquals(expected, templates.get(0));
    }

    /**
     * Test.
     */
    public void testLookupFromUuid() {

        // ARRANGE
        final Template t = new Template("title", "description", "body");
        final EntityManager em = createMock(EntityManager.class);
        expect(em.find(Resource.class, t.id())).andReturn(t);
        replay(em);
        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        final Template actual = am.lookup(t.id());

        // ASSERT
        assertEquals(t, actual);
        verify(em);
    }

    /**
     * Test.
     */
    public void testCreateDisplayTemplateCreatesADisplayTemplate() {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final Folder templateFolder = new Folder(new ResourceName("templates"));
        assetRoot.add(templateFolder);
        final Template t = new Template("title", "description", "body");

        final EntityManager em = createMock(EntityManager.class);
        expect(em.createNamedQuery(Queries.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                @Override
                public Object getSingleResult() { return assetRoot; }
            });
        em.persist(t);
        replay(em);

        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        am.createDisplayTemplate(t);

        // ASSERT
        verify(em);
        assertEquals(1, templateFolder.size());
        assertEquals(t, templateFolder.entries().get(0));
    }

    /**
     * Test.
     */
    public void testCreateRoot() {

        // ARRANGE
        final Capture<Folder> assetsRoot = new Capture<Folder>();
        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery(RESOURCE_BY_URL))
            .andThrow(new NoResultException());
        em.persist(capture(assetsRoot));
        em.persist(isA(Folder.class));
        replay(em);


        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        am.createRoot();

        // VERIFY
        verify(em);
        assertEquals(ASSETS, assetsRoot.getValue().name());
        assertEquals(
            "templates",
            assetsRoot.getValue()
                .entries().get(0).as(Folder.class).name().toString());
    }
}
