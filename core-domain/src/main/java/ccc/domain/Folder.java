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
public final class Folder extends Resource {

   private final List<Resource> entries = new ArrayList<Resource>();

   /**
    * @see ccc.domain.Resource#type()
    */
   @Override
   public ResourceType type() {
      return ResourceType.FOLDER;
   }

   /**
    * Accessor for size.
    *
    * @return The number of resources contained by this folder.
    */
   public int size() {
      return entries.size();
   }

   /**
    * Add a resource to this folder.
    *
    * @param resource The resource to add.
    */
   public void add(final Resource resource) {
      entries.add(resource);
   }

   /**
    * Accessor for entries.
    *
    * @return A list of all the resources in this folder.
    */
   public List<Resource> entries() {
      return entries;
   }

}
