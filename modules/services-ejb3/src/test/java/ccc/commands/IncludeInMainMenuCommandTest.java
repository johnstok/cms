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
import ccc.domain.LogEntry;
import ccc.domain.ResourceEntity;
import ccc.domain.Search;



/**
 * Tests for the {@link IncludeInMainMenuCommand} class.
 *
 * @author Civic Computing Ltd.
 */
public class IncludeInMainMenuCommandTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        final Search s = new Search("foo");
        s.lock(getUser());

        expect(getRepository().find(ResourceEntity.class, s.getId())).andReturn(s);
        getAudit().record(isA(LogEntry.class));

        replayAll();

        final IncludeInMainMenuCommand c =
            new IncludeInMainMenuCommand(getRepository(), getAudit());

        // ACT
        c.execute(getUser(), getNow(), s.getId(), true);

        // ASSERT
        verifyAll();
        assertTrue(s.isIncludedInMainMenu());
    }

    /**
     * Test.
     */
    public void testRemoveFromMainMenu() {

        // ARRANGE
        final Search s = new Search("foo");
        s.lock(getUser());

        expect(getRepository().find(ResourceEntity.class, s.getId())).andReturn(s);
        getAudit().record(isA(LogEntry.class));

        replayAll();

        final IncludeInMainMenuCommand c =
            new IncludeInMainMenuCommand(getRepository(), getAudit());

        // ACT
        c.execute(getUser(), getNow(), s.getId(), false);

        // ASSERT
        verifyAll();
        assertFalse(s.isIncludedInMainMenu());
    }
}
