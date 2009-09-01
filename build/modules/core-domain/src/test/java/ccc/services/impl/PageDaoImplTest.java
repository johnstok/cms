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
import ccc.commands.UpdatePageCommand;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.CccCheckedException;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.AuditLog;
import ccc.persistence.Repository;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


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
     * @throws CccCheckedException If the command fails.
     */
    public void testUpdatePage() throws CccCheckedException {

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

        expect(_repository.find(Page.class, page.id())).andReturn(page);
        _al.record(isA(LogEntry.class));
        replayAll();


        // ACT
        _updatePage.execute(
            _u, _now, page.id(), delta, "comment text", false);


        // ASSERT
        verifyAll();
        assertEquals(1, page.currentRevision().paragraphs().size());
        assertEquals(
            "foo",
            page.currentRevision().paragraphs().iterator().next().name());
        assertEquals(
            "bar",
            page.currentRevision().paragraph("foo").text());
        assertFalse("Page must not have working copy", page.hasWorkingCopy());
    }


    private void verifyAll() {
        verify(_repository, _al);
    }

    private void replayAll() {
        replay(_repository, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _repository = createStrictMock(Repository.class);
        _al = createStrictMock(AuditLog.class);
        _updatePage = new UpdatePageCommand(_repository, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _updatePage = null;
        _al = null;
        _repository = null;
    }

    private Repository _repository;
    private AuditLog _al;
    private UpdatePageCommand _updatePage;
    private final Date _now = new Date();
    private final User _u = new User("user");
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
