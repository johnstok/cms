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
import ccc.domain.Setting;
import ccc.domain.Setting.Name;


/**
 * Tests for the {@link QueryManagerEJB} class.
 *
 * @author Civic Computing Ltd.
 */
public class QueriesTest extends TestCase {

    /**
     * Test.
     */
    public void testLookupSetting() {

        // ARRANGE
        final Query q = createStrictMock(Query.class);
        expect(q.setParameter("name", Name.CONTENT_ROOT_FOLDER_ID))
            .andReturn(q);
        expect(q.getSingleResult()).andReturn(
            new Setting(Name.CONTENT_ROOT_FOLDER_ID, "foo"));
        replay(q);

        final EntityManager em = createStrictMock(EntityManager.class);
        expect(em.createQuery("from Setting s where s.name=:name"))
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
        final QueryManagerEJB q = new QueryManagerEJB(Testing.dummy(EntityManager.class));
    }

    /**
     * Test.
     */
    public void testConstructorRejectsNullEm() {

        // ACT
        try {
            final QueryManagerEJB q = new QueryManagerEJB(null);
            fail("Null should be rejected.");

        // ASSERT
        } catch (final IllegalArgumentException e) {
            assertEquals("Specified value may not be NULL.", e.getMessage());
        }
    }
}
