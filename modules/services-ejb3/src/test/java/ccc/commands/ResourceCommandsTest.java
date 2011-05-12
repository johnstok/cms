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

import ccc.api.types.CommandType;
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
        new IncludeInMainMenuCommand(getRepoFactory(), _r.getId(), true)
            .execute(getUser(), new Date());

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
        getProducer().broadcastMessage(
            eq(CommandType.SEARCH_INDEX_RESOURCE), isA(Map.class));
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        new UpdateResourceMetadataCommand(
            getRepoFactory(),
            getProducer(),
            _r.getId(),
            "newTitle",
            "newDesc",
            new HashSet<String>() {{ add("foo"); add("bar"); }},
            props)
            .execute(getUser(), new Date());

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
                MimeType.VELOCITY,
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
        new ChangeTemplateForResourceCommand(
            getRepoFactory(), _r.getId(), defaultTemplate.getId())
            .execute(getUser(), new Date());

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
        getProducer().broadcastMessage(
            eq(CommandType.SEARCH_INDEX_RESOURCE), isA(Map.class));
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new MoveResourceCommand(
            getRepoFactory(), getProducer(), _r.getId(), newParent.getId())
            .execute(getUser(), new Date());

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
        getProducer().broadcastMessage(
            eq(CommandType.SEARCH_INDEX_RESOURCE), isA(Map.class));
        getAudit().record(isA(LogEntry.class));
        replayAll();

        // ACT
        new RenameResourceCommand(
            getRepository(), getAudit(), getProducer(), _r.getId(), "baz")
            .execute(getUser(), new Date());

        // ASSERT
        verifyAll();
        assertEquals("baz", _r.getName().toString());
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
