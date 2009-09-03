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
package ccc.rest.providers;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.StringConverter;


/**
 * A provider for UUIDs.
 *
 * @author Civic Computing Ltd.
 */
@Provider
@Consumes("application/json")
@Produces("application/json")
public class UUIDProvider
    extends
        AbstractProvider
    implements
        StringConverter<UUID>{

    /** {@inheritDoc} */
    @Override
    public UUID fromString(final String arg0) {
        return UUID.fromString(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(final UUID arg0) {
        return arg0.toString();
    }
}
