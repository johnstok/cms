/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.impl;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.Aliases;
import ccc.rest.CommandFailedException;
import ccc.rest.dto.AliasDelta;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.ResourceSummary;


/**
 * Implementation of the {@link Aliases} API.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/aliases")
@Consumes("application/json")
@Produces("application/json")
public class AliasesImpl
    extends
        JaxrsCollection
    implements
        Aliases {


    /** {@inheritDoc} */
    @Override
    public String aliasTargetName(final UUID aliasId) {
        return getAliases().aliasTargetName(aliasId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID aliasId, final AliasDelta delta)
    throws CommandFailedException {
        getAliases().updateAlias(aliasId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final AliasDto alias)
    throws CommandFailedException {
        return getAliases().createAlias(alias);
    }
}
