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


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public interface ResourceManager {

   /**
    * TODO: Add a description of this method.
    *
    */
   public void create();
   
   /**
    * TODO: Add a description of this method.
    *
    * @param absoulteURI
    * @return
    */
   public Resource lookup(String absoulteURI);
}
