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
package ccc.services.ejb3;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.Setting.Name;


/**
 * Tests for the {@link QueryManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class QueryManagerEJBTest extends TestCase {

    private Resource _r = new Page("foo");

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

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        qs.find(Resource.class, _r.id());

        // ASSERT
        verify(q, em);
    }

    /**
     * Test.
     */
    public void testFindByString() {

        // ARRANGE
        final Query q = createStrictMock(Query.class);
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.find(Resource.class, _r.id())).andReturn(_r);
        replay(em);

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        qs.find(Resource.class, _r.id().toString());

        // ASSERT
        verify(q, em);
    }

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

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        qs.list("queryName", Object.class, p1, p2);

        // ASSERT
        verify(q, em);
    }

    /**
     * Test.
     */
    public void testFindAssetsRoot() {

        // ARRANGE
        final UUID folderId = UUID.randomUUID();
        final Query q = createStrictMock(Query.class);
        expect(q.setParameter("name", new ResourceName("assets"))).andReturn(q);
        expect(q.getSingleResult()).andReturn(new Folder(new ResourceName("bar")));
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery("resourcesByName")).andReturn(q);
        replay(em);

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        qs.findAssetsRoot();

        // ASSERT
        verify(q, em);
    }

    /**
     * Test.
     */
    public void testFindContentRoot() {

        // ARRANGE
        final UUID folderId = UUID.randomUUID();
        final Query q = createStrictMock(Query.class);
        expect(q.setParameter("name", new ResourceName("content"))).andReturn(q);
        expect(q.getSingleResult()).andReturn(new Folder(new ResourceName("bar")));
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery("resourcesByName")).andReturn(q);
        replay(em);

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        qs.findContentRoot();

        // ASSERT
        verify(q, em);
    }

    /**
     * Test.
     */
    public void testFindSetting() {

        // ARRANGE
        final Query q = createStrictMock(Query.class);
        expect(q.setParameter("name", Name.CONTENT_ROOT_FOLDER_ID))
            .andReturn(q);
        expect(q.getSingleResult()).andReturn(
            new Setting(Name.CONTENT_ROOT_FOLDER_ID, "foo"));
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createNamedQuery("settingsByName"))
            .andReturn(q);
        replay(em);

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        final Setting s = qs.findSetting(Name.CONTENT_ROOT_FOLDER_ID);

        // ASSERT
        verify(em, q);
        assertEquals(Name.CONTENT_ROOT_FOLDER_ID, s.name());
    }

    /**
     * Test.
     */
    public void testConstructor() {
        // ACT
        new QueryManagerEJB(Testing.dummy(EntityManager.class));
    }

    /**
     * Test.
     */
    public void testConstructorRejectsNullEm() {

        // ACT
        try {
            new QueryManagerEJB(null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
