/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */
package ccc.services;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;


/**
 * Business methods that operate on resources.
 *
 * @author Civic Computing Ltd
 */
public interface ResourceManager {

   /**
    * Lookup a resource, given its absolute path.
    *
    * @param path The absolute path to the resource.
    * @return The resource.
    */
   Resource lookup(ResourcePath path);
}
