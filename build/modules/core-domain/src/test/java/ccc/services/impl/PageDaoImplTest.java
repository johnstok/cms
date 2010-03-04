/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;
import ccc.types.ResourceName;
import ccc.types.Username;


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
        final UpdatePageCommand updatePage =
            new UpdatePageCommand(
                _repository, _al, page.getId(), delta, "comment text", false);

        expect(_repository.find(Page.class, page.getId())).andReturn(page);
        _al.record(isA(LogEntry.class));
        replayAll();


        // ACT
        updatePage.execute(_u, _now);


        // ASSERT
        verifyAll();
        assertEquals(1, page.currentRevision().getParagraphs().size());
        assertEquals(
            "foo",
            page.currentRevision().getParagraphs().iterator().next().name());
        assertEquals(
            "bar",
            page.currentRevision().getParagraph("foo").text());
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
        _repository = createStrictMock(ResourceRepository.class);
        _al = createStrictMock(LogEntryRepository.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _al = null;
        _repository = null;
    }

    private ResourceRepository _repository;
    private LogEntryRepository _al;
    private final Date _now = new Date();
    private final User _u = new User(new Username("user"), "password");
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
