/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.local;

import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Template;
import ccc.services.AuditLog;
import ccc.services.TemplateDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testLookupTemplates() {

        // ARRANGE
        final Template expected =
            new Template("title", "description", "body", "<fields/>");

        expect(_q.getResultList())
            .andReturn(Collections.singletonList(expected));
        expect(_em.createNamedQuery("allTemplates"))
            .andReturn(_q);
        replay(_q, _em, _al);


        // ACT
        final List<Template> templates = _cm.allTemplates();


        // ASSERT
        verify(_em, _q, _al);
        assertEquals(1, templates.size());
        assertEquals(expected, templates.get(0));
    }

    /**
     * Test.
     */
    public void testCreateDisplayTemplateCreatesADisplayTemplate() {

        // ARRANGE
        final Folder assetRoot = new Folder(PredefinedResourceNames.ASSETS);
        final Folder templateFolder = new Folder("templates");
        assetRoot.add(templateFolder);
        final Template t =
            new Template("title", "description", "body", "<fields/>");

        expect(_em.find(Folder.class, templateFolder.id()))
            .andReturn(templateFolder);
        _em.persist(t);
        _al.recordCreate(t);
        replay(_em, _q, _al);


        // ACT
        _cm.create(templateFolder.id(), t);

        // ASSERT
        verify(_em, _q, _al);
        assertEquals(1, templateFolder.size());
        assertEquals(t, templateFolder.entries().get(0));
    }



    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _em = createStrictMock(EntityManager.class);
        _al = createStrictMock(AuditLog.class);
        _q = createStrictMock(Query.class);
        _cm = new TemplateDaoImpl(_em, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _em = null;
        _q = null;
        _al = null;
        _cm = null;
    }

    private EntityManager _em;
    private Query _q;
    private AuditLog _al;
    private TemplateDao _cm;
}
