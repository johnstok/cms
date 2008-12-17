/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.commons;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;


/**
 * JNDI utility class.
 *
 * @author Civic Computing Ltd
 */
public final class JNDI implements Registry {

    private final Logger _log = Logger.getLogger(JNDI.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Registry put(final String location, final Object object) {

        try {
            jndiContext().rebind(location, object);
            return this;
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
        final Context jndiContext = new InitialContext();
        return jndiContext;
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked") // JNDI doesn't support generics
    @Override
    public <T> T get(final String location) {
        try {
            final Object o = jndiContext().lookup(location);
            return (T) o;
        } catch (final NameNotFoundException nfe) {
            _log.debug("JNDI lookup failed.", nfe);
            return null;
        } catch (final NamingException ne) {
            throw new RuntimeException(
                "Error looking up JNDI location: " + location,
                ne);
        }
    }
}
