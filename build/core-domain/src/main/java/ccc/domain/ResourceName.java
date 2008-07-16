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

import java.util.regex.Pattern;


/**
 * Represents a valid resource name in CCC. A valid name must contain one or
 * more characters. Characters must be members of the group [a-zA-Z_0-9].
 * 
 * See the following links for further details on URL encoding:
 * http://en.wikipedia.org/wiki/Percent-encoding
 * http://i-technica.com/whitestuff/urlencodechart.html
 *
 * @author Civic Computing Ltd
 */
public class ResourceName {

   private String representation;
   private final String validCharacters = "\\w+";
   private final Pattern validRegex = Pattern.compile(validCharacters);

   /**
    * Constructor.
    *
    * @param lowercaseAlphabet
    */
   public ResourceName(String representation) {

      if (null == representation)
         throw new RuntimeException("A resource name may not be NULL.");
      if (representation.length() < 1)
         throw new RuntimeException("A resource name must be longer than zero characters.");
      if (!validRegex.matcher(representation).matches())
         throw new RuntimeException(representation+" does not match the java.util.regex pattern '"+validCharacters+"'.");
      
      this.representation = representation;
   }

   /**
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {
      return representation;
   }

   /**
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode() {

      final int prime = 31;
      int result = 1;
      result = prime * result + ((representation == null) ? 0 : representation.hashCode());
      return result;
   }

   /**
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(Object obj) {

      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      final ResourceName other = (ResourceName) obj;
      if (representation == null) {
         if (other.representation != null) return false;
      } else if (!representation.equals(other.representation)) return false;
      return true;
   }

   /**
    * Escape a string to provide a valid ResourceName.
    *
    * @param invalidCharacters
    * @return
    */
   public static ResourceName escape(String invalidCharacters) {
      String validCharacters = invalidCharacters.replaceAll("\\W", "_");
      return new ResourceName(validCharacters);
   }
}
