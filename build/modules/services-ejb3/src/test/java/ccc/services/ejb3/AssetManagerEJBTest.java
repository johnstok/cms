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

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
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
        final Capture<Folder> templates = new Capture<Folder>();
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
        assertEquals("templates", assetsRoot.getValue().entries().get(0).asFolder().name().toString());
    }
}
