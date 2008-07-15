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
package ccc.commons.jee;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 * JNDI utility class.
 *
 * @author Civic Computing Ltd
 */
public class JNDI {

   /**
    * Put an object into JNDI.
    *
    * @param location
    * @param object
    */
   public static void put(final String location, final Object object) {
      try {
         jndiContext().rebind(location, object);
      } catch (final NamingException ne) {
         throw new RuntimeException("Error binding JNDI location: "+location, ne);
      }
   }

   /**
    * Create a JNDI context.
    *
    * @return {@link Context}
    * @throws NamingException
    */
   private static Context jndiContext() throws NamingException {

      final Hashtable<String, String> env = new Hashtable<String, String>();
      env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
      env.put(Context.PROVIDER_URL, "localhost:1099");

      final Context jndiContext = new InitialContext(env);
      return jndiContext;
   }

   /**
    * Get an object from JNDI.
    *
    * @param <T>
    * @param location
    * @return The object at the specified location or NULL if the location is
    * not bound.
    */
   @SuppressWarnings("unchecked")
    public static <T> T get(final String location) {
        try {
            final Object o = jndiContext().lookup(location);
            return (T)o;
        } catch (final NameNotFoundException nfe) {
         return null;
      } catch (final NamingException ne) {
            throw new RuntimeException("Error looking up JNDI location: "+location, ne);
        }
    }
}

