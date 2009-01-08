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

import junit.framework.TestCase;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.services.PageDao;
import ccc.services.ResourceDao;


/**
 * Tests for the {@link PageDaoImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class PageDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testUpdatePage() {

        // ARRANGE
        final Page page = new Page("test");
        page.addParagraph(Paragraph.fromText("abc", "def"));

        expect(_dao.findLocked(Page.class, page.id())).andReturn(page);
        _dao.update(page);
        replay(_dao);


        // ACT
        _cm.update(
            page.id(),
            "new title",
            Collections.singleton(Paragraph.fromText("foo", "bar")));


        // ASSERT
        verify(_dao);
        assertEquals("new title", page.title());
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().iterator().next().name());
        assertEquals("bar", page.paragraph("foo").text());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(ResourceDao.class);
        _cm = new PageDaoImpl(_dao);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _cm = null;
    }

    private ResourceDao _dao;
    private PageDao _cm;
}
