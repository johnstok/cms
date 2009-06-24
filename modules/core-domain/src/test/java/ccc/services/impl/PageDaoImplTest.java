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
package ccc.services.impl;

import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.Date;

import junit.framework.TestCase;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.commands.UpdatePageCommand;
import ccc.domain.Page;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.ResourceName;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.services.AuditLog;
import ccc.services.Dao;


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
     * @throws RemoteExceptionSupport If the command fails.
     */
    public void testUpdatePage() throws RemoteExceptionSupport {

        // ARRANGE
        final Page page =
            new Page(
                new ResourceName("test"),
                "test",
                null,
                _rm,
                Paragraph.fromText("abc", "def"));
        final PageDelta delta =
            new PageDelta(
                Collections.singleton(Paragraph.fromText("foo", "bar")));
        page.lock(_u);

        expect(_dao.find(Page.class, page.id())).andReturn(page);
        _al.recordUpdate(page, _u, _now, "comment text", false);
        replayAll();


        // ACT
        _updatePage.execute(
            _u, _now, page.id(), delta, "comment text", false);


        // ASSERT
        verifyAll();
        assertEquals(1, page.paragraphs().size());
        assertEquals("foo", page.paragraphs().iterator().next().name());
        assertEquals("bar", page.paragraph("foo").text());
        assertFalse("Page must not have working copy", page.hasWorkingCopy());
    }


    private void verifyAll() {
        verify(_dao, _al);
    }

    private void replayAll() {
        replay(_dao, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _dao = createStrictMock(Dao.class);
        _al = createStrictMock(AuditLog.class);
        _updatePage = new UpdatePageCommand(_dao, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _updatePage = null;
        _al = null;
        _dao = null;
    }

    private Dao _dao;
    private AuditLog _al;
    private UpdatePageCommand _updatePage;
    private final Date _now = new Date();
    private final User _u = new User("user");
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
