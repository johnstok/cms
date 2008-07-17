/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.services.ejb3;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import ccc.domain.Content;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceType;
import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 * 
 * @author Civic Computing Ltd
 */
public class ResourceManagerEJBTest extends TestCase {

    /**
     * TODO Add Description for this type.
     * 
     * @author Civic Computing Ltd
     */
    private class QueryAdaptor implements Query {

        @Override
        public int executeUpdate() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public List getResultList() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Object getSingleResult() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setFirstResult(int arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setFlushMode(FlushModeType arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setHint(String arg0, Object arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setMaxResults(int arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setParameter(String arg0, Object arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setParameter(int arg0, Object arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setParameter(String arg0, Date arg1, TemporalType arg2) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setParameter(String arg0, Calendar arg1, TemporalType arg2) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setParameter(int arg0, Date arg1, TemporalType arg2) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query setParameter(int arg0, Calendar arg1, TemporalType arg2) {

            throw new UnsupportedOperationException("Method not implemented.");
        }
    }

    /**
     * TODO Add Description for this type.
     * 
     * @author Civic Computing Ltd
     */
    private class EntityManagerAdaptor implements EntityManager {

        @Override
        public void clear() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void close() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public boolean contains(Object arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query createNamedQuery(String arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query createNativeQuery(String arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query createNativeQuery(String arg0, Class arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query createNativeQuery(String arg0, String arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Query createQuery(String arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public <T> T find(Class<T> arg0, Object arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void flush() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public Object getDelegate() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public FlushModeType getFlushMode() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public <T> T getReference(Class<T> arg0, Object arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public EntityTransaction getTransaction() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public boolean isOpen() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void joinTransaction() {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void lock(Object arg0, LockModeType arg1) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public <T> T merge(T arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void persist(Object arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void refresh(Object arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void remove(Object arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }

        @Override
        public void setFlushMode(FlushModeType arg0) {

            throw new UnsupportedOperationException("Method not implemented.");
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {

        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link ccc.services.ejb3.ResourceManagerEJB#lookup(java.lang.String)}.
     */
    public final void testLookup() {

        // ARRANGE
        EntityManager em = new EntityManagerAdaptor() {

            /**
             * @see ccc.services.ejb3.ResourceManagerEJBTest.EntityManagerAdaptor#createQuery(java.lang.String)
             */
            @Override
            public Query createQuery(String arg0) {

                return new QueryAdaptor() {

                    /**
                     * @see ccc.services.ejb3.ResourceManagerEJBTest.QueryAdaptor#getSingleResult()
                     */
                    @Override
                    public Object getSingleResult() {
                        return new Content("My name").addParagraph("default", new Paragraph("<H1>Default</H!>"));
                    }
                };
            }

        };
        ResourceManagerEJB resourceMgr = new ResourceManagerEJB(em);

        // ACT
        Resource resource = resourceMgr.lookup("foo/bar");

        // ASSERT
        assertEquals(ResourceType.CONTENT, resource.type());
        Content content = resource.asContent();
        assertEquals(1, content.paragraphs().size());
    }

}
