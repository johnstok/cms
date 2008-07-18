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
import ccc.domain.Folder;
import ccc.domain.Paragraph;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourceName;
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
        final Folder contentRoot = new Folder(PredefinedResourceNames.CONTENT);
        final Folder foo = new Folder(new ResourceName("foo"));
        final Content bar = new Content(new ResourceName("bar"))
                                    .addParagraph(
                                        "default",
                                        new Paragraph("<H1>Default</H!>"));
        contentRoot.add(foo);
        foo.add(bar);

        EntityManager em = new EntityManagerAdaptor() {
            /**@see EntityManagerAdaptor#createQuery(java.lang.String)*/
            @Override public Query createNamedQuery(String arg0) {
                return new QueryAdaptor() {
                    /** @see ccc.services.ejb3.QueryAdaptor#getSingleResult() */
                    @Override
                    public Object getSingleResult() { return contentRoot; }
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
