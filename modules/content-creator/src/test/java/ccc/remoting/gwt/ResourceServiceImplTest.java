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
import ccc.commons.MapRegistry;
import ccc.domain.Folder;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.adaptors.ContentManagerAdaptor;


/**
 * TODO Add Description for this type.
 * TODO Test getResource().
 *
 * @author Civic Computing Ltd
 */
public final class ResourceServiceImplTest extends TestCase {

    /**
     * Test.
     */
    public void testGetContentRoot() {

        // ARRANGE
        final ResourceServiceImpl resourceService =
            new ResourceServiceImpl(
                new MapRegistry(
                "ContentManagerEJB/local",
                new ContentManagerAdaptor() {
                    /** @see ContentManagerAdaptor#lookup(java.lang.String) */
                    @Override
                    public Resource lookup(final ResourcePath path) {
                        return
                            new Folder(PredefinedResourceNames.CONTENT);
                    }
            }));

        // ACT
        final String jsonRoot = resourceService.getContentRoot();

        // VERIFY
        assertEquals(
            "{\"name\": \"content\",\"entries\": []}",
            jsonRoot);
    }
}
