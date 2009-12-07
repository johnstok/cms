/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import java.util.Date;

import junit.framework.TestCase;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;
import ccc.types.ResourceName;
import ccc.types.Username;


/**
 * Tests for working copy management.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyManagerTest
    extends
        TestCase {


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testClearWorkingCopy() throws CccCheckedException {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
        p.lock(_user);
        p.setOrUpdateWorkingCopy(p.createSnapshot());

        expect(_repository.find(Resource.class, p.id())).andReturn(p);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ClearWorkingCopyCommand(_repository, _audit).execute(
            _user, _now, p.id());

        // ASSERT
        verifyAll();
        assertFalse(p.hasWorkingCopy());
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testUpdateWorkingCopy() throws CccCheckedException {

        // ARRANGE
        final Page page =
            new Page(
                new ResourceName("test"),
                "test",
                null,
                _rm,
                Paragraph.fromText("abc", "def"));
        page.lock(_user);
        final PageDelta before = page.createSnapshot();

        expect(_repository.find(Page.class, page.id())).andReturn(page);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateWorkingCopyCommand(_repository, _audit).execute(
            _user, _now, page.id(), before);

        // ASSERT
        verifyAll();
        assertNotNull(
            "Page must have a working copy", page.getOrCreateWorkingCopy());
    }


    private void verifyAll() {
        verify(_repository, _audit);
    }

    private void replayAll() {
        replay(_repository, _audit);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _repository = createStrictMock(ResourceRepository.class);
        _audit = createStrictMock(LogEntryRepository.class);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _repository = null;
        _audit = null;
    }

    private ResourceRepository _repository;
    private LogEntryRepository _audit;
    private final User _user =
        new User(new Username("currentUser"), "password");
    private final Date _now = new Date();
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
