/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.support;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.domain.Page;
import ccc.domain.Resource;


/**
 * Tests for the {@link BaseDao} class.
 *
 * TODO: Missing tests.
 *
 * @author Civic Computing Ltd.
 */
public class BaseDaoTest
    extends
        TestCase {

    private Resource _r = new Page("foo");

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

        final BaseDao qs = new BaseDao(em);

        // ACT
        qs.list("queryName", Object.class, p1, p2);

        // ASSERT
        verify(q, em);
    }

    /**
     * Test.
     */
    public void testFindByUuid() {

        // ARRANGE
        final Query q = createStrictMock(Query.class);
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, _r.id())).andReturn(_r);
        replay(em);

        final BaseDao qs = new BaseDao(em);

        // ACT
        qs.find(Resource.class, _r.id());

        // ASSERT
        verify(q, em);
    }

}
