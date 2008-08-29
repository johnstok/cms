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
import static org.easymock.EasyMock.*;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.easymock.Capture;
import org.hibernate.lob.BlobImpl;

import ccc.commons.Maybe;
import ccc.domain.File;
import ccc.domain.FileData;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.AssetManager;
import ccc.services.QueryManager;


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
        expect(em.createNamedQuery(QueryManagerEJB.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /**  {@inheritDoc} */ @Override
                public Object getSingleResult() { return assetRoot; }
            });
        em.persist(t);
        expect(em.createNamedQuery(QueryManagerEJB.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /**  {@inheritDoc} */ @Override
                public Object getSingleResult() { return assetRoot; }
            });
        replay(em);

        final AssetManager am = new AssetManagerEJB(em, new QueryManagerEJB(em));

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
        expect(em.createNamedQuery(QueryManagerEJB.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /**  {@inheritDoc} */ @Override
                public Object getSingleResult() { return assetRoot; }
            });
        replay(em);

        final AssetManager am = new AssetManagerEJB(em, new QueryManagerEJB(em));

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
        final AssetManager am = new AssetManagerEJB(em, new QueryManagerEJB(em));

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
        expect(em.createNamedQuery(QueryManagerEJB.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                @Override
                public Object getSingleResult() { return assetRoot; }
            });
        em.persist(t);
        replay(em);

        final AssetManager am =
            new AssetManagerEJB(em, new QueryManagerEJB(em));

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
        final QueryManager qm = createStrictMock(QueryManager.class);
        expect(qm.lookupRoot(PredefinedResourceNames.ASSETS))
            .andReturn(new Maybe<Folder>());
        replay(qm);

        final Capture<Folder> assetsRoot = new Capture<Folder>();
        final EntityManager em = createStrictMock(EntityManager.class);
        em.persist(capture(assetsRoot));
        em.persist(isA(Folder.class));
        replay(em);


        final AssetManager am = new AssetManagerEJB(em, qm);

        // ACT
        am.createRoot();

        // VERIFY
        verify(qm, em);
        assertEquals(ASSETS, assetsRoot.getValue().name());
        assertEquals(
            "templates",
            assetsRoot.getValue()
                .entries().get(0).as(Folder.class).name().toString());
    }

    /**
     * Test.
     * @throws SQLException
     *
     */
    public void testCreateFileData() throws SQLException {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        FileData fileData;
        fileData = new FileData(
            new BlobImpl("test".getBytes()).getBinaryStream(), "test".length());

        final File file = new File(
            new ResourceName("file"), "title", "desc", fileData);

        final EntityManager em = createMock(EntityManager.class);
        em.persist(fileData);
        em.persist(file);
        expect(em.createNamedQuery(QueryManagerEJB.RESOURCE_BY_URL)).andReturn(
            new QueryAdaptor() {
                /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                @Override
                public Object getSingleResult() { return assetRoot; }
            });
        replay(em);

        final AssetManager am = new AssetManagerEJB(em, new QueryManagerEJB(em));

        // ACT
        am.createFile(file, "/");

        // VERIFY
        verify(em);
    }
}
