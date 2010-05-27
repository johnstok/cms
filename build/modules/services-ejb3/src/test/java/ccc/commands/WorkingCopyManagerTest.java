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
package ccc.commands;

import static org.easymock.EasyMock.*;
import ccc.api.core.Page;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.domain.LogEntry;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;


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
        final PageEntity p =
            new PageEntity(
                new ResourceName("foo"), "foo", null, getRevisionMetadata());
        p.lock(getUser());
        p.setOrUpdateWorkingCopy(p.forCurrentRevision());

        expect(
            getRepository().find(ResourceEntity.class, p.getId())).andReturn(p);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ClearWorkingCopyCommand(getRepository(), getAudit()).execute(
            getUser(), getNow(), p.getId());

        // ASSERT
        verifyAll();
        assertFalse(p.hasWorkingCopy());
    }


    /**
     * Test.
     */
    public void testUpdateWorkingCopy() {

        // ARRANGE
        final PageEntity page =
            new PageEntity(
                new ResourceName("test"),
                "test",
                null,
                getRevisionMetadata(),
                Paragraph.fromText("abc", "def"));
        page.lock(getUser());
        final Page before = page.forCurrentRevision();

        expect(
            getRepository().find(
                PageEntity.class, page.getId())).andReturn(page);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateWorkingCopyCommand(getRepoFactory()).execute(
            getUser(), getNow(), page.getId(), before);

        // ASSERT
        verifyAll();
        assertNotNull(
            "Page must have a working copy", page.getOrCreateWorkingCopy());
    }
}
