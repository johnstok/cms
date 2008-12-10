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
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface StatefulReader {

    /**
     * TODO: Add a description of this method.
     *
     * @param contentPath ResourcePath
     * @return Resource
     */
    Resource lookup(ResourcePath contentPath);

}
