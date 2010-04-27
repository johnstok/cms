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
import ccc.api.types.Paragraph;
import ccc.api.types.PredefinedResourceNames;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourcePath;
import ccc.api.types.ResourceType;
import ccc.domain.FolderEntity;
import ccc.domain.PageEntity;
import ccc.domain.ResourceEntity;
import ccc.domain.RevisionMetadata;
import ccc.domain.UserEntity;


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
                FolderEntity.class,
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
        final FolderEntity contentRoot = new FolderEntity(PredefinedResourceNames.CONTENT);
        final FolderEntity foo = new FolderEntity("foo");
        final PageEntity bar =
            new PageEntity(
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
                FolderEntity.class,
                new ResourceName(PredefinedResourceNames.CONTENT)))
            .andReturn(contentRoot);
        replayAll();


        // ACT
        final ResourceEntity resource = _rdao.lookup(new ResourcePath("/foo/bar"));


        verifyAll();
        assertEquals(ResourceType.PAGE, resource.getType());
        final PageEntity page = resource.as(PageEntity.class);
        assertEquals(1, page.currentRevision().getParagraphs().size());
    }


    /**
     * Test.
     */
    public void testQueryAllLockedResources() {

        // ARRANGE
        expect(_repository.list(QueryNames.LOCKED_RESOURCES, ResourceEntity.class))
            .andReturn(Collections.singletonList(_r));
        replayAll();

        // ACT
        final List<ResourceEntity> locked = _rdao.locked();

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
        final PageEntity bar =
            new PageEntity(
                new ResourceName("bar"),
                "bar",
                null,
                _rm,
                Paragraph.fromText("default", "<H1>Default</H1>"));

        expect(_repository.find(PageEntity.class, bar.getId())).andReturn(bar);
        replayAll();


        // ACT
        final ResourceEntity resource =
            _rdao.find(PageEntity.class, bar.getId());


        // ASSERT
        verifyAll();
        assertEquals(ResourceType.PAGE, resource.getType());
        final PageEntity page = resource.as(PageEntity.class);
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
        _r = new PageEntity(new ResourceName("foo"), "foo", null, _rm);
        _parent = new FolderEntity("parent");
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
    private ResourceEntity _r;
    private FolderEntity _parent;

    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), UserEntity.SYSTEM_USER, true, "Created.");
}
