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
package ccc.commands;

import static org.easymock.EasyMock.*;
import ccc.domain.Alias;
import ccc.domain.CccCheckedException;
import ccc.domain.LogEntry;
import ccc.domain.Resource;
import ccc.domain.Search;


/**
 * Tests for the {@link UpdateAliasCommand} class.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasCommandTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testUpdateAlias() throws CccCheckedException {

        // ARRANGE
        final Search foo = new Search("foo");
        final Search bar = new Search("bar");
        final Alias alias = new Alias("alias", foo);
        alias.lock(getUser());

        expect(getRepository().find(Alias.class, alias.id())).andReturn(alias);
        expect(getRepository().find(Resource.class, bar.id())).andReturn(bar);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        final UpdateAliasCommand c =
            new UpdateAliasCommand(
                getRepository(), getAudit(), bar.id(), alias.id());

        // ACT
        c.execute(getUser(), getNow());

        // ASSERT
        verifyAll();
        assertEquals(bar, alias.target());
    }
}
