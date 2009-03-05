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
import ccc.services.ISearch;
import ccc.services.PageDao;
import ccc.services.ResourceDao;


/**
 * Tests for the {@link PageDaoImpl} class.
 * TODO: Test create() method.
 *
 * @author Civic Computing Ltd.
 */
public class PageDaoImplTest
    extends
        TestCase {


    /**
     * Test.
     */
    public void testClearWorkingCopy() {

        // ARRANGE
        final Page p = new Page("foo");
        p.createWorkingCopy();

        expect(_dao.findLocked(Page.class, p.id())).andReturn(p);
        replayAll();

        // ACT
        _cm.clearWorkingCopy(p.id());

        // ASSERT
        verifyAll();
        assertNull(p.workingCopy());
    }


    /**
     * Test.
     */
    public void testUpdatePage() {

        // ARRANGE
        final Page page = new Page("test");
        page.addParagraph(Paragraph.fromText("abc", "def"));

        expect(_dao.findLocked(Page.class, page.id())).andReturn(page);
        _dao.update(page, "comment text", false);
        _se.update(page);
        replayAll();


        // ACT
        _cm.update(
            page.id(),
            "new title",
            Collections.singleton(Paragraph.fromText("foo", "bar")),
            "comment text", false);


        // ASSERT
        verifyAll();
        assertEquals("new title", page.title());
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().iterator().next().name());
        assertEquals("bar", page.paragraph("foo").text());
        assertNull("Page must not have working copy", page.workingCopy());
    }


    /**
     * Test.
     */
    public void testUpdateWorkingCopy() {

        // ARRANGE
        final Page page = new Page("test");
        page.addParagraph(Paragraph.fromText("abc", "def"));

        expect(_dao.findLocked(Page.class, page.id())).andReturn(page);
        replayAll();

        // ACT
        _cm.updateWorkingCopy(page.id(),
            "working title",
            Collections.singleton(Paragraph.fromText("foo", "bar")));

        // ASSERT
        verifyAll();
        assertNotNull("Page must have a working copy", page.workingCopy());
        assertEquals("working title", page.workingCopy().getString("title"));

    }


    private void verifyAll() {
        verify(_dao, _se);
    }

    private void replayAll() {
        replay(_dao, _se);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _dao = createStrictMock(ResourceDao.class);
        _se = createStrictMock(ISearch.class);
        _cm = new PageDaoImpl(_dao, _se);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        _dao = null;
        _se = null;
        _cm = null;
    }

    private ResourceDao _dao;
    private PageDao _cm;
    private ISearch _se;
}
