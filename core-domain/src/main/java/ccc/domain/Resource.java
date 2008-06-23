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
package ccc.domain;

import java.util.UUID;


/**
 * A simple resource.
 *
 * @author Civic Computing Ltd
 */
public class Resource {
   private UUID id = UUID.randomUUID();
   private int version = -1;
   
   
   /**
    * @see java.lang.Object#toString()
    */
   public String toString() {
      return this.getClass().getName()+": "+id.toString()+" [version="+version+"]";
   }
   
   /**
    * @see java.lang.Object#hashCode()
    */
   public int hashCode() {

      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }
   
   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object obj) {

      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final Resource other = (Resource) obj;
      if (id == null) {
         if (other.id != null) return false;
      } else if (!id.equals(other.id)) return false;
      return true;
   }
}
