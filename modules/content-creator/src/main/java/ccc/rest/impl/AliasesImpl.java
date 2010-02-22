/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Aliases;
import ccc.rest.RestException;
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
@NoCache
public class AliasesImpl
    extends
        JaxrsCollection
    implements
        Aliases {


    /** {@inheritDoc} */
    @Override
    public String aliasTargetName(final UUID aliasId) throws RestException {
        return getAliases().aliasTargetName(aliasId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateAlias(final UUID aliasId, final AliasDelta delta)
    throws RestException {
        getAliases().updateAlias(aliasId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createAlias(final AliasDto alias)
    throws RestException {
        return getAliases().createAlias(alias);
    }
}
