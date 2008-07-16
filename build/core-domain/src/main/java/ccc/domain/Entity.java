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

import java.util.UUID;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Entity {

   protected UUID id = UUID.randomUUID();
   private int version = -1;

   /**
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return this.getClass().getName()+": "+id.toString()+" [version="+version+"]";
   }

   /**
    * Accessor for the id field.
    *
    * @return
    */
   public UUID id() {
      return id;
   }

}