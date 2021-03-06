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

import java.util.Date;

import ccc.domain.FolderEntity;
import ccc.domain.LogEntry;


/**
 * Tests for the folder commands.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDaoImplTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testUpdateSortOrder() throws Exception {

        // ARRANGE
        _f.lock(getUser());
        expect(getRepository().find(FolderEntity.class, _f.getId()))
            .andReturn(_f);
        expect(getRepository().find(FolderEntity.class, _f.getId()))
            .andReturn(_f);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        final UpdateFolderCommand uf =
            new UpdateFolderCommand(
                getRepoFactory(),
                _f.getId(),
                null,
                null);

        // ACT
        uf.execute(getUser(), new Date());

        // ASSERT
        verifyAll();
    }


    /** {@inheritDoc} */
    @Override protected void setUp() {
        super.setUp();
        _f = new FolderEntity("foo");
    }


    /** {@inheritDoc} */
    @Override protected void tearDown() {
        super.tearDown();
        _f = null;
    }


    private FolderEntity _f;
}
