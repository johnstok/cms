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
import ccc.commands.AbstractCommandTest;
import ccc.commands.ClearWorkingCopyCommand;
import ccc.commands.UpdateWorkingCopyCommand;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;
import ccc.types.ResourceName;


/**
 * Tests for working copy management.
 *
 * @author Civic Computing Ltd.
 */
public class WorkingCopyManagerTest
    extends
        AbstractCommandTest {


    /**
     * Test.
     */
    public void testClearWorkingCopy() {

        // ARRANGE
        final Page p = new Page(new ResourceName("foo"), "foo", null, _rm);
        p.lock(_user);
        p.setOrUpdateWorkingCopy(p.createSnapshot());

        expect(_repository.find(Resource.class, p.getId())).andReturn(p);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ClearWorkingCopyCommand(_repository, _audit).execute(
            _user, _now, p.getId());

        // ASSERT
        verifyAll();
        assertFalse(p.hasWorkingCopy());
    }


    /**
     * Test.
     */
    public void testUpdateWorkingCopy() {

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

        expect(_repository.find(Page.class, page.getId())).andReturn(page);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateWorkingCopyCommand(_repoFactory).execute(
            _user, _now, page.getId(), before);

        // ASSERT
        verifyAll();
        assertNotNull(
            "Page must have a working copy", page.getOrCreateWorkingCopy());
    }
}
