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

import junit.framework.TestCase;
import ccc.domain.Template;
import ccc.services.ResourceDao;
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

        expect(_dao.list("allTemplates", Template.class))
            .andReturn(Collections.singletonList(expected));
        replay(_dao);


        // ACT
        final List<Template> templates = _cm.allTemplates();


        // ASSERT
        verify(_dao);
        assertEquals(1, templates.size());
        assertEquals(expected, templates.get(0));
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(ResourceDao.class);
        _cm = new TemplateDaoImpl(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _cm = null;
    }

    private ResourceDao _dao;
    private TemplateDao _cm;
}
