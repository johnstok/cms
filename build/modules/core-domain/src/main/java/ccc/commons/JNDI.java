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

package ccc.commons;

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
public final class JNDI implements Registry {

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String location, final Object object) {

        try {
            jndiContext().rebind(location, object);
        } catch (final NamingException ne) {
            throw new RuntimeException(
                "Error binding JNDI location: " + location,
                ne);
        }
    }

    /**
     * Create a JNDI context.
     *
     * @return {@link Context}
     * @throws NamingException Thrown if creation of the JNDI context fails.
     */
    private Context jndiContext() throws NamingException {

        final Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(
            Context.INITIAL_CONTEXT_FACTORY,
            "org.jnp.interfaces.NamingContextFactory");
        env.put(
            Context.PROVIDER_URL,
            "localhost:1099");

        final Context jndiContext = new InitialContext(env);
        return jndiContext;
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(final String location) {
        try {
            final Object o = jndiContext().lookup(location);
            return (T) o;
        } catch (final NameNotFoundException nfe) {
            return null;
        } catch (final NamingException ne) {
            throw new RuntimeException(
                "Error looking up JNDI location: " + location,
                ne);
        }
    }
}
