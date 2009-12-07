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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.commands.UpdateFolderCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.ResourceName;
import ccc.types.ResourceOrder;
import ccc.types.Username;


/**
 * Tests for the folder commands.
 *
 * @author Civic Computing Ltd.
 */
public class FolderDaoImplTest
    extends
        TestCase {

    /**
     * Test.
     * @throws Exception If the test fails.
     */
    public void testUpdateSortOrder() throws Exception {

        // ARRANGE
        _f.lock(_regularUser);
        expect(_rdao.find(Folder.class, _f.id()))
            .andReturn(_f);
        _al.record(isA(LogEntry.class));
        replayAll();

        final UpdateFolderCommand uf =
            new UpdateFolderCommand(
                _rdao,
                _al,
                _f.id(),
                ResourceOrder.NAME_ALPHANUM_ASC,
                null,
                null);

        // ACT
        uf.execute(_regularUser, new Date());

        // ASSERT
        verifyAll();
        assertEquals(ResourceOrder.NAME_ALPHANUM_ASC, _f.sortOrder());
    }

    /**
     * Test.
     * @throws CccCheckedException If the test fails.
     */
    public void testReorder() throws CccCheckedException {

        // ARRANGE
        _f.lock(_regularUser);
        final Page foo = new Page(new ResourceName("foo"), "foo", null, _rm);
        final Page bar = new Page(new ResourceName("bar"), "bar", null, _rm);
        final Page baz = new Page(new ResourceName("baz"), "baz", null, _rm);
        _f.add(foo);
        _f.add(bar);
        _f.add(baz);

        expect(_rdao.find(Folder.class, _f.id()))
            .andReturn(_f);
        _al.record(isA(LogEntry.class));
        replayAll();

        final List<UUID> order = new ArrayList<UUID>();
        order.add(baz.id());
        order.add(foo.id());
        order.add(bar.id());

        // ACT
        final UpdateFolderCommand uf =
            new UpdateFolderCommand(
                _rdao,
                _al,
                _f.id(),
                ResourceOrder.MANUAL,
                null,
                order);

        uf.execute(_regularUser, new Date());

        // ASSERT
        verifyAll();
        final List<Resource> entries = _f.entries();
        assertEquals(3, entries.size());
        assertEquals(baz, entries.get(0));
        assertEquals(foo, entries.get(1));
        assertEquals(bar, entries.get(2));

    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _al = createStrictMock(LogEntryRepository.class);
        _rdao = createStrictMock(ResourceRepository.class);

        _f = new Folder("foo");
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _rdao = null;
        _al = null;
    }

    private void replayAll() {
        replay(_rdao, _al);
    }

    private void verifyAll() {
        verify(_rdao, _al);
    }
    private final User _regularUser =
        new User(new Username("regular"), "password");

    private Folder _f;

    private LogEntryRepository _al;
    private ResourceRepository _rdao;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
