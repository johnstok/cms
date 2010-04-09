/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commands;

import static org.easymock.EasyMock.*;

import java.util.Date;

import junit.framework.TestCase;
import ccc.api.exceptions.InsufficientPrivilegesException;
import ccc.api.exceptions.LockMismatchException;
import ccc.domain.Folder;
import ccc.domain.Group;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.persistence.LogEntryRepository;
import ccc.persistence.ResourceRepository;
import ccc.types.CommandType;
import ccc.types.Permission;
import ccc.types.ResourceName;
import ccc.types.Username;


/**
 * Tests for lock & unlock commands.
 * <p>
 * TODO: Test lock(null) fails with illegal arg exception.
 * TODO: Test unlock(null) fails with illegal arg exception.
 * TODO: Test unlock() behaviour for an unlocked resource.
 * TODO: Test lock behaviour if called when by the user that already holds the
 *  lock.
 *
 * @author Civic Computing Ltd.
 */
public class LockingTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testResourceCannotBeUnlockedByNonlockerNonAdmin() {

        // ARRANGE
        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        replayAll();

        _r.lock(_anotherUser);

        // ACT
        try {
            new UnlockResourceCommand(_rdao, _al, _r.getId()).execute(
                _regularUser, new Date());
            fail("Should fail.");

        // ASSERT
        } catch (final InsufficientPrivilegesException e) {
            assertEquals(CommandType.RESOURCE_UNLOCK, e.getAction());
            assertEquals(_regularUser.getId(), e.getUser());
        }
        assertEquals(_anotherUser, _r.getLockedBy());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testResourceCanBeUnlockedByNonlockerAdmin() {

        // ARRANGE
        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        _r.lock(_regularUser);

        // ACT
        new UnlockResourceCommand(_rdao, _al, _r.getId()).execute(
            _adminUser, new Date());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testUnlockedResourceCanBeLocked() {

        // ARRANGE
        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new LockResourceCommand(_rdao, _al, _r.getId()).execute(
            _regularUser, new Date());

        // ASSERT
        assertEquals(_regularUser, _r.getLockedBy());
        verifyAll();
    }

    /**
     * Test.
     */
    public void testLockedResourceCannotBeRelockedBySomeoneElse() {

        // ARRANGE
        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        replayAll();
        _r.lock(_anotherUser);

        // ACT
        try {
            new LockResourceCommand(_rdao, _al, _r.getId()).execute(
                _regularUser, new Date());
            fail("Lock should fail.");

        // ASSERT
        } catch (final LockMismatchException e) {
            assertEquals(_r.getId(), e.getResource());
        }
        verifyAll();
    }

    /**
     * Test.
     */
    public void testUnpublishWithUser() {

        // ARRANGE
        _r.lock(_regularUser);
        _r.publish(_regularUser);

        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UnpublishResourceCommand(_rdao, _al, _r.getId()).execute(
            _regularUser, new Date());

        // ASSERT
        verifyAll();
        assertEquals(null, _r.getPublishedBy());
    }


    /**
     * Test.
     */
    public void testPublish() {

        // ARRANGE
        _r.lock(_regularUser);

        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new PublishCommand(_rdao, _al, _r.getId())
        .execute(_regularUser, new Date());

        // ASSERT
        verifyAll();
        assertEquals(_regularUser, _r.getPublishedBy());
    }


    /**
     * Test.
     */
    public void testResourceCanBeUnlockedByLockerNonadmin() {

        // ARRANGE
        expect(_rdao.find(Resource.class, _r.getId())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        _r.lock(_regularUser);

        // ACT
        new UnlockResourceCommand(_rdao, _al, _r.getId()).execute(
            _regularUser, new Date());

        // ASSERT
        assertFalse("Should be unlocked.", _r.isLocked());
        verifyAll();
    }


    private void replayAll() {
        replay(_rdao, _al);
    }

    private void verifyAll() {
        verify(_rdao, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _al = createStrictMock(LogEntryRepository.class);
        _rdao = createStrictMock(ResourceRepository.class);
        _r = new Page(new ResourceName("foo"), "foo", null, _rm);
        _parent = new Folder("parent");
        _parent.add(_r);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _parent = null;
        _r      = null;
        _rdao   = null;
        _al     = null;
    }


    private LogEntryRepository _al;
    private ResourceRepository _rdao;
    private Resource _r;
    private Folder _parent;

    private final User _regularUser =
        new User(new Username("regular"), "password");
    private final User _anotherUser =
        new User(new Username("another"), "password");
    private final User _adminUser =
        new User(new Username("admin"), "password"){{
       addGroup(
           new Group("ADMINISTRATOR", Permission.RESOURCE_UNLOCK));
    }};
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
