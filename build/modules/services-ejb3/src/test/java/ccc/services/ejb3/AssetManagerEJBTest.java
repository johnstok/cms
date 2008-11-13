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

import java.util.List;

import javax.persistence.EntityManager;

import junit.framework.TestCase;

import org.easymock.Capture;

import ccc.commons.Maybe;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.Template;
import ccc.services.AssetManagerLocal;
import ccc.services.AuditLogLocal;
import ccc.services.QueryManagerLocal;


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
        final Template t =
            new Template("title", "description", "body", "<fields/>");
        assetRoot.add(templateFolder);

        expect(_qm.findAssetsRoot())
            .andReturn(new Maybe<Folder>(assetRoot)).times(2);
        _em.persist(t);
        _al.recordCreate(t);
        replay(_em, _qm, _al);


        // ACT
        final Template created = _am.createOrRetrieve(t);
        final Template retrieved = _am.createOrRetrieve(t);


        // ASSERT
        verify(_em, _qm, _al);
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
        final Template expected =
            new Template("title", "description", "body", "<fields/>");
        assetRoot.add(templateFolder);
        templateFolder.add(expected);

        expect(_qm.findAssetsRoot()).andReturn(new Maybe<Folder>(assetRoot));
        replay(_qm, _em, _al);


        // ACT
        final List<Template> templates = _am.lookupTemplates();


        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, templates.size());
        assertEquals(expected, templates.get(0));
    }

    /**
     * Test.
     */
    public void testLookupFromUuid() {

        // ARRANGE
        final Template t =
            new Template("title", "description", "body", "<fields/>");

        expect(_em.find(Resource.class, t.id())).andReturn(t);
        replay(_em, _qm, _al);


        // ACT
        final Template actual = _am.lookup(t.id()).as(Template.class);


        // ASSERT
        assertEquals(t, actual);
        verify(_em, _qm, _al);
    }

    /**
     * Test.
     */
    public void testCreateDisplayTemplateCreatesADisplayTemplate() {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final Folder templateFolder = new Folder(new ResourceName("templates"));
        assetRoot.add(templateFolder);
        final Template t =
            new Template("title", "description", "body", "<fields/>");

        expect(_qm.findAssetsRoot()).andReturn(new Maybe<Folder>(assetRoot));
        _em.persist(t);
        _al.recordCreate(t);
        replay(_em, _qm, _al);


        // ACT
        _am.createDisplayTemplate(t);

        // ASSERT
        verify(_em, _qm, _al);
        assertEquals(1, templateFolder.size());
        assertEquals(t, templateFolder.entries().get(0));
    }

    /**
     * Test.
     */
    public void testCreateRoot() {

        // ARRANGE
        expect(_qm.findAssetsRoot()).andReturn(new Maybe<Folder>());

        final Capture<Folder> assetsRoot = new Capture<Folder>();
        _em.persist(capture(assetsRoot));
        _em.persist(isA(Folder.class));
        _em.persist(isA(Setting.class));

        _al.recordCreate(isA(Folder.class));
        _al.recordCreate(isA(Folder.class));

        replay(_em, _qm, _al);


        // ACT
        _am.createRoot();


        // VERIFY
        verify(_qm, _em, _al);
        assertEquals(ASSETS, assetsRoot.getValue().name());
        assertEquals(
            "templates",
            assetsRoot.getValue()
                .entries().get(0).as(Folder.class).name().toString());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _qm = createStrictMock(QueryManagerLocal.class);
        _al = createStrictMock(AuditLogLocal.class);
        _am = new AssetManagerEJB(_em, _qm, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _qm = null;
        _al = null;
        _am = null;
    }

    private EntityManager _em;
    private QueryManagerLocal _qm;
    private AuditLogLocal _al;
    private AssetManagerLocal _am;
}
