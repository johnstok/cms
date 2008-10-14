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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.commons.Testing;
import ccc.domain.Folder;
import ccc.domain.Maybe;
import ccc.domain.ResourceName;
import ccc.domain.Setting;
import ccc.domain.UUID;
import ccc.domain.Setting.Name;
import ccc.services.ejb3.QueryManagerEJB.NamedQueries;


/**
 * Tests for the {@link QueryManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class QueryManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public void testFindAssetsRoot() {

        // ARRANGE
        final UUID folderId = UUID.randomUUID();
        final Query q = createStrictMock(Query.class);
        expect(q.setParameter("name", Name.ASSETS_ROOT_FOLDER_ID))
            .andReturn(q);
        expect(q.getSingleResult()).andReturn(
            new Setting(Name.ASSETS_ROOT_FOLDER_ID, folderId.toString()));
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createQuery(NamedQueries.SETTING_BY_NAME.queryString()))
            .andReturn(q);
        expect(em.find(Folder.class, folderId))
            .andReturn(new Folder(new ResourceName("bar")));
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
        expect(q.setParameter("name", Name.CONTENT_ROOT_FOLDER_ID))
            .andReturn(q);
        expect(q.getSingleResult()).andReturn(
            new Setting(Name.CONTENT_ROOT_FOLDER_ID, folderId.toString()));
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createQuery(NamedQueries.SETTING_BY_NAME.queryString()))
            .andReturn(q);
        expect(em.find(Folder.class, folderId))
            .andReturn(new Folder(new ResourceName("bar")));
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
        expect(em.createQuery(NamedQueries.SETTING_BY_NAME.queryString()))
            .andReturn(q);
        replay(em);

        final QueryManagerEJB qs = new QueryManagerEJB(em);

        // ACT
        final Maybe<Setting> s = qs.findSetting(Name.CONTENT_ROOT_FOLDER_ID);

        // ASSERT
        verify(em, q);
        assertEquals(Name.CONTENT_ROOT_FOLDER_ID, s.get().name());
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
