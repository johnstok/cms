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
package ccc.persistence;

import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import ccc.commands.ChangeTemplateForResourceCommand;
import ccc.commands.IncludeInMainMenuCommand;
import ccc.commands.MoveResourceCommand;
import ccc.commands.RenameResourceCommand;
import ccc.commands.UpdateCachingCommand;
import ccc.commands.UpdateResourceMetadataCommand;
import ccc.domain.CccCheckedException;
import ccc.domain.EntityNotFoundException;
import ccc.domain.Folder;
import ccc.domain.Group;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.types.Duration;
import ccc.types.MimeType;
import ccc.types.Paragraph;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;
import ccc.types.Username;


/**
 * Tests for the {@link ResourceRepositoryImpl} class.
 *
 * TODO: testQueryAllLockedResources cannot be called by non-admin?
 *
 * @author Civic Computing Ltd.
 */
public class ResourceDaoImplTest
    extends
        TestCase {


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testLookupThrowsExceptionIfRootIsMissing() throws Exception {

        // ARRANGE
        expect(
            _repository.find(
                QueryNames.ROOT_BY_NAME,
                Folder.class,
                new ResourceName(PredefinedResourceNames.CONTENT)))
            .andThrow(new EntityNotFoundException(null));
        replayAll();

        // ACT
        try {
            _rdao.lookup(new ResourcePath("/foo/bar"));
            fail();

        // ASSERT
        } catch (final EntityNotFoundException e) {
            verifyAll();
        }
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testLookup() throws CccCheckedException {

        // ARRANGE
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder("foo");
        final Page bar =
            new Page(
                new ResourceName("bar"),
                "bar",
                null,
                _rm,
                Paragraph.fromText("default", "<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        expect(
            _repository.find(
                QueryNames.ROOT_BY_NAME,
                Folder.class,
                new ResourceName(PredefinedResourceNames.CONTENT)))
            .andReturn(contentRoot);
        replayAll();


        // ACT
        final Resource resource = _rdao.lookup(new ResourcePath("/foo/bar"));


        verifyAll();
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.currentRevision().getParagraphs().size());
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testIncludeInMainMenu()
    throws CccCheckedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_repository.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new IncludeInMainMenuCommand(_rdao, _al).execute(
            _regularUser, new Date(), _r.id(), true);

        // ASSERT
        verifyAll();
        assertEquals(true, _r.includeInMainMenu());

    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testUpdateFullMetadata()
    throws CccCheckedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_repository.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        final Map<String, String> props = new HashMap<String, String>();
        props.put("bodyId", "example");
        new UpdateResourceMetadataCommand(_rdao, _al).execute(
            _regularUser,
            new Date(),
            _r.id(),
            "newTitle",
            "newDesc",
            "foo,bar",
            props);

        // ASSERT
        verifyAll();
        assertEquals("example", _r.getMetadatum("bodyId"));
        assertEquals("newTitle", _r.getTitle());
        assertEquals("newDesc", _r.description());
        assertTrue(_r.tags().contains("foo"));
        assertTrue(_r.tags().contains("bar"));
    }


    /**
     * Test.
     */
    public void testQueryAllLockedResources() {

        // ARRANGE
        expect(_repository.list(QueryNames.LOCKED_RESOURCES, Resource.class))
            .andReturn(Collections.singletonList(_r));
        replayAll();

        // ACT
        final List<Resource> locked = _rdao.locked();

        // ASSERT
        assertNotNull("Shouldn't be null.", locked);
        verifyAll();
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testSetDefaultTemplate()
    throws CccCheckedException {

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
        _r.lock(_regularUser);

        expect(_repository.find(Resource.class, _r.id()))
            .andReturn(_r);
        expect(_repository.find(Template.class, defaultTemplate.id()))
            .andReturn(defaultTemplate);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new ChangeTemplateForResourceCommand(_rdao, _al).execute(
            _regularUser, new Date(), _r.id(), defaultTemplate.id());

        // ASSERT
        verifyAll();
        assertEquals(defaultTemplate, _r.template());
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testMove()
    throws CccCheckedException {

        // ARRANGE
        final Folder oldParent = new Folder("old");
        final Folder newParent = new Folder("new");
        oldParent.add(_r);
        _r.lock(_regularUser);

        expect(_repository.find(Resource.class, _r.id()))
            .andReturn(_r);
        expect(_repository.find(Folder.class, newParent.id()))
            .andReturn(newParent);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new MoveResourceCommand(_rdao, _al).execute(
            _regularUser, new Date(), _r.id(), newParent.id());

        // ASSERT
        verifyAll();
        assertEquals(newParent, _r.parent());
    }


    /**
     * Test.
     * @throws CccCheckedException If the command fails.
     */
    public void testRename()
    throws CccCheckedException {

        // ARRANGE
        _r.lock(_regularUser);
        expect(_repository.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new RenameResourceCommand(_rdao, _al, _r.id(), "baz")
            .execute(_regularUser, new Date());

        // ASSERT
        verifyAll();
        assertEquals("baz", _r.name().toString());
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFind() throws Exception {

        // ARRANGE
        final Page bar =
            new Page(
                new ResourceName("bar"),
                "bar",
                null,
                _rm,
                Paragraph.fromText("default", "<H1>Default</H1>"));

        expect(_repository.find(Page.class, bar.id())).andReturn(bar);
        replayAll();


        // ACT
        final Resource resource =
            _rdao.find(Page.class, bar.id());


        // ASSERT
        verifyAll();
        assertEquals(ResourceType.PAGE, resource.type());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.currentRevision().getParagraphs().size());
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
    public void testUpdateCache()
    throws CccCheckedException {

        // ARRANGE
        final int expecteduration = 3727;
        _r.lock(_regularUser);
        expect(_repository.find(Resource.class, _r.id())).andReturn(_r);
        _al.record(isA(LogEntry.class));
        replayAll();

        // ACT
        new UpdateCachingCommand(_rdao, _al, _r.id(), new Duration(0, 1, 2, 7))
            .execute(_regularUser, new Date());

        // ASSERT
        verifyAll();
        assertEquals(expecteduration, _r.cache().time());
    }


    private void replayAll() {
        replay(_repository, _al);
    }

    private void verifyAll() {
        verify(_repository, _al);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _repository = createStrictMock(Repository.class);
        _al = createStrictMock(LogEntryRepository.class);
        _rdao = new ResourceRepositoryImpl(_repository);
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
        _repository    = null;
    }


    private Repository _repository;
    private LogEntryRepository _al;
    private ResourceRepositoryImpl _rdao;
    private Resource _r;
    private Folder _parent;

    private final User _regularUser =
        new User(new Username("regular"), "password");
    private final User _anotherUser =
        new User(new Username("another"), "password");
    private final User _adminUser =
        new User(new Username("admin"), "password"){{
       addRole(new Group("ADMINISTRATOR"));
    }};
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
