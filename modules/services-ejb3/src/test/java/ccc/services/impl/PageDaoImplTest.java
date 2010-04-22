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

import ccc.api.dto.PageDto;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.commands.AbstractCommandTest;
import ccc.commands.UpdatePageCommand;
import ccc.domain.LogEntry;
import ccc.domain.Page;


/**
 * Tests for the {@link PageDaoImpl} class.
 * TODO: Test create() method.
 *
 * @author Civic Computing Ltd.
 */
public class PageDaoImplTest
    extends
        AbstractCommandTest {


    /**
     * Test.
     */
    public void testUpdatePage() {

        // ARRANGE
        final Page page =
            new Page(
                new ResourceName("test"),
                "test",
                null,
                _rm,
                Paragraph.fromText("abc", "def"));
        final PageDto delta =
            PageDto.delta(
                Collections.singleton(Paragraph.fromText("foo", "bar")));
        delta.setComment("comment text");
        delta.setMajorChange(false);

        page.lock(_user);
        final UpdatePageCommand updatePage =
            new UpdatePageCommand(
                _repoFactory, page.getId(), delta);

        expect(_repository.find(Page.class, page.getId())).andReturn(page);
        _audit.record(isA(LogEntry.class));
        replayAll();


        // ACT
        updatePage.execute(_user, _now);


        // ASSERT
        verifyAll();
        assertEquals(1, page.currentRevision().getParagraphs().size());
        assertEquals(
            "foo",
            page.currentRevision().getParagraphs().iterator().next().getName());
        assertEquals(
            "bar",
            page.currentRevision().getParagraph("foo").getText());
        assertFalse("Page must not have working copy", page.hasWorkingCopy());
    }
}
