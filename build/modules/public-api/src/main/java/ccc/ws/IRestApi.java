/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.ws;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.ResourceSummary;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface IRestApi {

    /**
     * Query: list the root folders available.
     *
     * @return The root folders, as a collection of resource summaries.
     */
    @GET @Path("/roots") @NoCache
    public abstract Collection<ResourceSummary> roots();

}