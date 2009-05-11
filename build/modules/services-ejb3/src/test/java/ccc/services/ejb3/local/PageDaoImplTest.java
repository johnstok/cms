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
import java.util.Date;

import junit.framework.TestCase;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.User;
import ccc.services.PageDao;
import ccc.services.ResourceDao;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ParagraphType;


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
    public void testUpdatePage() {

        // ARRANGE
        final PageDelta delta =
            new PageDelta(
                "new title",
                Collections.singletonList(
                    new ParagraphDelta(
                        "foo", ParagraphType.TEXT, null, "bar", null, null)));
        final Page page = new Page("test");
        final User u = new User("user");
        page.addParagraph(Paragraph.fromText("abc", "def"));

        expect(_dao.findLocked(Page.class, page.id(), u)).andReturn(page);
        _dao.update(
            eq(page), eq("comment text"), eq(false), eq(u), isA(Date.class));
        replayAll();


        // ACT
        _cm.update(u, new Date(), page.id(), delta, "comment text", false);


        // ASSERT
        verifyAll();
        assertEquals("new title", page.title());
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().iterator().next().name());
        assertEquals("bar", page.paragraph("foo").text());
        assertNull("Page must not have working copy", page.workingCopy());
    }


    private void verifyAll() {
        verify(_dao);
    }

    private void replayAll() {
        replay(_dao);
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
