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
package ccc.domain;

import java.util.ArrayList;
import java.util.List;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Folder extends Resource {
   
   private List<Resource> entries = new ArrayList<Resource>();

   /**
    * @see ccc.domain.Resource#type()
    */
   @Override
   public ResourceType type() {
      return ResourceType.FOLDER;
   }

   /**
    * TODO: Add a description of this method.
    *
    * @return
    */
   public int size() {
      return entries.size();
   }

   /**
    * TODO: Add a description of this method.
    *
    * @param content
    */
   public void add(Resource resource) {
      entries.add(resource);
   }

   /**
    * TODO: Add a description of this method.
    *
    * @return
    */
   public List<Resource> entries() {

      return entries;
   }

}
