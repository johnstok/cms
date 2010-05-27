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
import java.util.HashSet;
import java.util.Map;

import ccc.api.types.Duration;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.domain.FolderEntity;
import ccc.domain.LogEntry;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.TemplateEntity;
import ccc.domain.UserEntity;


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
        _r.lock(getUser());
        expect(
            getRepository().find(
                ResourceEntity.class, _r.getId())).andReturn(_r);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new IncludeInMainMenuCommand(getRepoFactory()).execute(
            getUser(), new Date(), _r.getId(), true);

        // ASSERT
        verifyAll();
        assertEquals(true, _r.isIncludedInMainMenu());

    }


    /**
     * Test.
     */
    public void testUpdateFullMetadata() {

        // ARRANGE
        _r.lock(getUser());
        expect(
            getRepository().find(
                ResourceEntity.class, _r.getId())).andReturn(_r);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        new UpdateResourceMetadataCommand(getRepoFactory()).execute(
            getUser(),
            new Date(),
            _r.getId(),
            "newTitle",
            "newDesc",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
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
        final TemplateEntity defaultTemplate =
            new TemplateEntity(
                "foo",
                "bar",
                "baz",
                "<fields/>",
                MimeType.HTML,
                new RevisionMetadata(
                    new Date(),
                    UserEntity.SYSTEM_USER,
                    true,
                    "Created."));
        _r.lock(getUser());

        expect(getRepository().find(ResourceEntity.class, _r.getId()))
            .andReturn(_r);
        expect(getRepository().find(
            TemplateEntity.class, defaultTemplate.getId()))
            .andReturn(defaultTemplate);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ChangeTemplateForResourceCommand(getRepoFactory()).execute(
            getUser(), new Date(), _r.getId(), defaultTemplate.getId());

        // ASSERT
        verifyAll();
        assertEquals(defaultTemplate, _r.getTemplate());
    }


    /**
     * Test.
     */
    public void testMove() {

        // ARRANGE
        final FolderEntity oldParent = new FolderEntity("old");
        final FolderEntity newParent = new FolderEntity("new");
        oldParent.add(_r);
        _r.lock(getUser());

        expect(getRepository().find(ResourceEntity.class, _r.getId()))
            .andReturn(_r);
        expect(getRepository().find(FolderEntity.class, newParent.getId()))
            .andReturn(newParent);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new MoveResourceCommand(getRepository(), getAudit()).execute(
            getUser(), new Date(), _r.getId(), newParent.getId());

        // ASSERT
        verifyAll();
        assertEquals(newParent, _r.getParent());
    }


    /**
     * Test.
     */
    public void testRename() {

        // ARRANGE
        _r.lock(getUser());
        expect(
            getRepository().find(
                ResourceEntity.class, _r.getId())).andReturn(_r);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new RenameResourceCommand(
            getRepository(), getAudit(), _r.getId(), "baz")
            .execute(getUser(), new Date());

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
     */
    public void testUpdateCache() {

        // ARRANGE
        final int expecteduration = 3727;
        _r.lock(getUser());
        expect(
            getRepository().find(
                ResourceEntity.class, _r.getId())).andReturn(_r);
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateCachingCommand(
            getRepository(), getAudit(), _r.getId(), new Duration(0, 1, 2, 7))
            .execute(getUser(), new Date());

        // ASSERT
        verifyAll();
        assertEquals(expecteduration, _r.getCacheDuration().time());
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        super.setUp();
        _r = new PageEntity(
            new ResourceName("foo"), "foo", null, getRevisionMetadata());
        _parent = new FolderEntity("parent");
        _parent.add(_r);
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        super.tearDown();
        _parent = null;
        _r      = null;
    }


    private ResourceEntity _r;
    private FolderEntity _parent;
}
