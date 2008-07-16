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



/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource extends Entity {
   
   /**
    * Query the type of this resource.
    *
    * @return The ResourceType that describes this resource.
    */
   public abstract ResourceType type();

   /**
    * Type-safe helper method to convert an instance of {@link Resource} to an
    * instance of {@link Content}.
    *
    * @return
    */
   public Content asContent() {
      return Content.class.cast(this);
   }

   /**
    * Type-safe helper method to convert an instance of {@link Resource} to an
    * instance of {@link Folder}.
    *
    * @return
    */
   public Folder asFolder() {
      return Folder.class.cast(this);
   }
}
