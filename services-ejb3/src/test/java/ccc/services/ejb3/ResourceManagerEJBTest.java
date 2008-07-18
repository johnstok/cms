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


import javax.persistence.EntityManager;
import javax.persistence.Query;

import junit.framework.TestCase;
import ccc.domain.Content;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.domain.ResourceType;


/**
 * Tests for the {@link ResourceManagerEJB} class.
 *
 * @author Civic Computing Ltd
 */
public class ResourceManagerEJBTest extends TestCase {

    /**
     * Test.
     */
    public final void testLookup() {

        // ARRANGE
        EntityManager em = new EntityManagerAdaptor() {

            /**
             * @see EntityManagerAdaptor#createQuery(java.lang.String)
             */
            @Override
            public Query createQuery(String arg0) {

                return new QueryAdaptor() {

                    /**
                     * @see ccc.services.ejb3.QueryAdaptor#getSingleResult()
                     */
                    @Override
                    public Object getSingleResult() {
                        return
                            new Content("My name")
                                .addParagraph(
                                    "default",
                                    new Paragraph("<H1>Default</H!>"));
                    }
                };
            }

        };
        ResourceManagerEJB resourceMgr = new ResourceManagerEJB(em);

        // ACT
        Resource resource = resourceMgr.lookup(new ResourcePath("/foo/bar"));

        // ASSERT
        assertEquals(ResourceType.CONTENT, resource.type());
        Content content = resource.asContent();
        assertEquals(1, content.paragraphs().size());
    }

}
