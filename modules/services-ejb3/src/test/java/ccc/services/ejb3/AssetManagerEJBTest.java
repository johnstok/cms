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

import ccc.domain.CCCException;
import ccc.domain.File;
import ccc.domain.FileData;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.AssetManager;


/**
 * Tests for the {@link AssetManagerEJB} class.
 *
 * @author Civic Computing Ltd
 */
public final class AssetManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testCreateOrRetrieveTemplate() {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final Folder templateFolder = new Folder(new ResourceName("templates"));
        final Template t = new Template("title", "description", "body");
        assetRoot.add(templateFolder);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery(Queries.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /**  {@inheritDoc} */ @Override
                public Object getSingleResult() { return assetRoot; }
            });
        em.persist(t);
        expect(em.createNamedQuery(Queries.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /**  {@inheritDoc} */ @Override
                public Object getSingleResult() { return assetRoot; }
            });
        replay(em);

        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        final Template created = am.createOrRetrieve(t);
        final Template retrieved = am.createOrRetrieve(t);

        // ASSERT
        verify(em);
        assertSame(t, created);
        assertSame(t, retrieved);
        assertTrue(
            "Templates folder should contain template.",
            templateFolder.entries().contains(t));
    }

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

    /**
     * Test.
     *
     */
    public void testCreateFileData() {

        // ARRANGE
        final FileData fileData = new FileData("test".getBytes());
        final File file = new File(
            new ResourceName("file"), "title", "desc", fileData);

        final EntityManager em = createMock(EntityManager.class);
        em.persist(fileData);
        em.persist(file);
        replay(em);

        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        am.createFile(file);

        // VERIFY
        verify(em);
    }

    /**
     * Test.
     *
     */
    public void testRejectTooBigFileData() {

        // ARRANGE
        final byte[] fatData = new byte[AssetManagerEJB.MAX_FILE_SIZE_BYTES+1];
        final FileData fileData = new FileData(fatData);
        final File file = new File(
            new ResourceName("file"), "title", "desc", fileData);
        final EntityManager em = createMock(EntityManager.class);
        replay(em);
        final AssetManager am = new AssetManagerEJB(em);

        // ACT
        try {
            am.createFile(file);
            fail("Creation of file data over 32MB should fail");
        } catch (final CCCException e) {
            assertEquals(
                "File data is too large.",
                e.getMessage());
        }

        // VERIFY
        verify(em);
    }
}
