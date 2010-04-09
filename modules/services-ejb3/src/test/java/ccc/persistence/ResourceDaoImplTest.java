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
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.types.Paragraph;
import ccc.types.PredefinedResourceNames;
import ccc.types.ResourceName;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


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
            .andThrow(new EntityNotFoundException((UUID) null));
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
     */
    public void testLookup() {

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
        assertEquals(ResourceType.PAGE, resource.getType());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.currentRevision().getParagraphs().size());
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

        expect(_repository.find(Page.class, bar.getId())).andReturn(bar);
        replayAll();


        // ACT
        final Resource resource =
            _rdao.find(Page.class, bar.getId());


        // ASSERT
        verifyAll();
        assertEquals(ResourceType.PAGE, resource.getType());
        final Page page = resource.as(Page.class);
        assertEquals(1, page.currentRevision().getParagraphs().size());
    }


    private void replayAll() {
        replay(_repository);
    }

    private void verifyAll() {
        verify(_repository);
    }

    /** {@inheritDoc} */
    @Override
    protected void setUp() throws Exception {
        _repository = createStrictMock(Repository.class);
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
        _repository    = null;
    }


    private Repository _repository;
    private ResourceRepositoryImpl _rdao;
    private Resource _r;
    private Folder _parent;

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
