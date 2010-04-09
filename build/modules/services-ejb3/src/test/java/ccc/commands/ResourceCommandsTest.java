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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.api.types.Duration;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;


/**
 * Tests for resource commands.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceCommandsTest
    extends
        AbstractCommandTest {

    /**
     * Test.
     */
    public void testIncludeInMainMenu() {

        // ARRANGE
        _r.lock(_user);
        expect(_repository.find(Resource.class, _r.getId())).andReturn(_r);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new IncludeInMainMenuCommand(_repoFactory).execute(
            _user, new Date(), _r.getId(), true);

        // ASSERT
        verifyAll();
        assertEquals(true, _r.isIncludedInMainMenu());

    }


    /**
     * Test.
     */
    public void testUpdateFullMetadata() {

        // ARRANGE
        _r.lock(_user);
        expect(_repository.find(Resource.class, _r.getId())).andReturn(_r);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        new UpdateResourceMetadataCommand(_repoFactory).execute(
            _user,
            new Date(),
            _r.getId(),
            "newTitle",
            "newDesc",
            "foo,bar",
            props);

        // ASSERT
        verifyAll();
        assertEquals("example", _r.getMetadatum("bodyId"));
        assertEquals("newTitle", _r.getTitle());
        assertEquals("newDesc", _r.getDescription());
        assertTrue(_r.getTags().contains("foo"));
        assertTrue(_r.getTags().contains("bar"));
    }


    /**
     * Test.
     */
    public void testSetDefaultTemplate() {

        // ARRANGE
        final Template defaultTemplate =
            new Template(
                "foo",
                "bar",
                "baz",
                "<fields/>",
                MimeType.HTML,
                new RevisionMetadata(
                    new Date(),
                    User.SYSTEM_USER,
                    true,
                    "Created."));
        _r.lock(_user);

        expect(_repository.find(Resource.class, _r.getId()))
            .andReturn(_r);
        expect(_repository.find(Template.class, defaultTemplate.getId()))
            .andReturn(defaultTemplate);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ChangeTemplateForResourceCommand(_repoFactory).execute(
            _user, new Date(), _r.getId(), defaultTemplate.getId());

        // ASSERT
        verifyAll();
        assertEquals(defaultTemplate, _r.getTemplate());
    }


    /**
     * Test.
     */
    public void testMove() {

        // ARRANGE
        final Folder oldParent = new Folder("old");
        final Folder newParent = new Folder("new");
        oldParent.add(_r);
        _r.lock(_user);

        expect(_repository.find(Resource.class, _r.getId()))
            .andReturn(_r);
        expect(_repository.find(Folder.class, newParent.getId()))
            .andReturn(newParent);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new MoveResourceCommand(_repository, _audit).execute(
            _user, new Date(), _r.getId(), newParent.getId());

        // ASSERT
        verifyAll();
        assertEquals(newParent, _r.getParent());
    }


    /**
     * Test.
     */
    public void testRename() {

        // ARRANGE
        _r.lock(_user);
        expect(_repository.find(Resource.class, _r.getId())).andReturn(_r);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new RenameResourceCommand(_repository, _audit, _r.getId(), "baz")
            .execute(_user, new Date());

        // ASSERT
        verifyAll();
        assertEquals("baz", _r.getName().toString());
    }


    /**
     * Test.
     * TODO: Broken.
     */
    public void testPublishWithUser() {
//
//        // ARRANGE
//        _r.lock(_regularUser);
//
//        expect(_dao.find(Resource.class, _r.id())).andReturn(_r);
//        expect(_users.loggedInUser()).andReturn(_regularUser);
//        expect(_users.find(_regularUser.id())).andReturn(_regularUser);
//        _al.recordPublish(eq(_r), eq(_regularUser), isA(Date.class));
//        replayAll();
//
//        // ACT
//        _rdao.publish(_r.id(), _regularUser.id(), new Date());
//
//        // ASSERT
//        verifyAll();
//        assertEquals(_regularUser, _r.publishedBy());
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testUpdateCache() {

        // ARRANGE
        final int expecteduration = 3727;
        _r.lock(_user);
        expect(_repository.find(Resource.class, _r.getId())).andReturn(_r);
        _audit.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateCachingCommand(
            _repository, _audit, _r.getId(), new Duration(0, 1, 2, 7))
            .execute(_user, new Date());

        // ASSERT
        verifyAll();
        assertEquals(expecteduration, _r.getCacheDuration().time());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _r = new Page(new ResourceName("foo"), "foo", null, _rm);
        _parent = new Folder("parent");
        _parent.add(_r);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        _parent = null;
        _r      = null;
    }


    private Resource _r;
    private Folder _parent;
}
