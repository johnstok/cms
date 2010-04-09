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

import java.util.ArrayList;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.api.types.ResourceName;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;


/**
 * Tests for the {@link JpaRepository} class.
 *
 * @author Civic Computing Ltd.
 */
public class JpaRepositoryTest
    extends
        TestCase {

    /**
     * Test.
     */
    public void testList() {

        // ARRANGE
        final Long p1 = Long.valueOf(1L);
        final Float p2 = Float.valueOf(2F);
        final Query q = createStrictMock(Query.class);
        expect(q.setParameter(1, p1)).andReturn(q);
        expect(q.setParameter(2, p2)).andReturn(q);
        expect(q.getResultList()).andReturn(new ArrayList<String>());
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery("queryName")).andReturn(q);
        replay(em);

        final JpaRepository qs = new JpaRepository(em);

        // ACT
        qs.list("queryName", Object.class, p1, p2);

        // ASSERT
        verify(q, em);
    }


    /**
     * Test.
     *
     * @throws Exception If the test fails.
     */
    public void testFindByUuid() throws Exception {

        // ARRANGE
        final Query q = createStrictMock(Query.class);
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, _r.getId())).andReturn(_r);
        replay(em);

        final JpaRepository qs = new JpaRepository(em);

        // ACT
        qs.find(Resource.class, _r.getId());

        // ASSERT
        verify(q, em);
    }


    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
    private Resource _r = new Page(new ResourceName("foo"), "foo", null, _rm);
}
