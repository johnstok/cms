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
package ccc.ws;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.Queries;
import ccc.api.ResourceSummary;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Produces("text/html")
public class RestApi {

    private Queries _queries;

    /**
     * Constructor.
     *
     * @param queries The {@link Queries} implementation to use.
     */
    public RestApi(final Queries queries) {
        _queries = queries;
    }

    /**
     * Query: list the root folders available.
     *
     * @return The root folders, as a collection of resource summaries.
     */
    @GET @Path("/roots") @NoCache
    public Collection<ResourceSummary> roots() {
        return _queries.roots();
    }
}
