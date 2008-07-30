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
package ccc.remoting.gwt;

import junit.framework.TestCase;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;
import ccc.services.adaptors.ResourceManagerAdaptor;


/**
 * TODO Add Description for this type.
 * TODO Test getResource().
 *
 * @author Civic Computing Ltd
 */
public class ResourceServiceImplTest extends TestCase {

    /**
     * Test.
     */
    public void testGetContentRoot() {

        // ARRANGE
        final ResourceServiceImpl resourceService = new ResourceServiceImpl() {

            /** serialVersionUID : long */
            private static final long serialVersionUID = 7146453466370673791L;

            /**
             * @see ccc.content.server.ContentServlet#resourceManager()
             */
            @Override
            protected ResourceManager resourceManager() {

                return new ResourceManagerAdaptor() {

                    /** @see ResourceManagerAdaptor#lookup(java.lang.String) */
                    @Override
                    public Resource lookup(final ResourcePath path) {
                        return
                            new Folder(PredefinedResourceNames.CONTENT);
                    }
                };
            }
        };

        // ACT
        final String jsonRoot = resourceService.getContentRoot();

        // VERIFY
        assertEquals(
            "{\"name\": \"content\",\"entries\": []}",
            jsonRoot);
    }

}
