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
package ccc.services;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;


/**
 * Reader API to lookup resources and navigate their associations, out with a
 * transaction.
 *
 * @author Civic Computing Ltd.
 */
public interface StatefulReader {

    /**
     * Look up a resource.
     *
     * @param contentPath ResourcePath The path to the resource.
     * @param rootName The name of the root folder in which the resource exists.
     * @return Resource The resource at the specified path, or NULL if it
     *  doesn't exist.
     */
    Resource lookup(String rootName, ResourcePath contentPath);

}
