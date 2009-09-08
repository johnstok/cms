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

package ccc.rest.impl;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class ResourcesImpl
    extends
        JaxrsCollection
    implements
        Resources {


    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final UUID resourceId) {
        return getCommands().cacheDuration(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final UUID resourceId) {
        return getCommands().computeTemplate(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        return getCommands().getAbsolutePath(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<RevisionDto> history(final UUID resourceId) {
        return getCommands().history(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        return getCommands().locked();
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId) {
        return getCommands().metadata(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId) {
        return getCommands().resource(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        return getCommands().resourceForLegacyId(legacyId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path) {
        return getCommands().resourceForPath(path);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final UUID resourceId) {
        return getCommands().roles(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu)
    throws RestException {
        getCommands().updateCacheDuration(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) throws RestException {
        getCommands().lock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID resourceId)
    throws RestException {
        getCommands().applyWorkingCopy(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu)
    throws RestException {
        getCommands().updateResourceTemplate(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void changeRoles(final UUID resourceId,
                            final Collection<String> roles)
    throws RestException {
        getCommands().changeRoles(resourceId, roles);
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId)
    throws RestException {
        getCommands().move(resourceId, newParentId);
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId) throws RestException {
        getCommands().publish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name)
    throws RestException {
        getCommands().rename(resourceId, name);
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) throws RestException {
        getCommands().unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) throws RestException {
        getCommands().unpublish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final UUID resourceId)
    throws RestException {
        getCommands().excludeFromMainMenu(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId)
    throws RestException {
        getCommands().includeInMainMenu(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId, final Json json)
    throws RestException {
        getCommands().updateMetadata(resourceId, json);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID pageId)
    throws RestException {
        getCommands().clearWorkingCopy(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId,
                                  final ResourceDto pu)
    throws RestException {
        getCommands().createWorkingCopy(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id)
    throws RestException {
        getCommands().updateCacheDuration(id, (Duration) null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() throws RestException {
        throw new RestException(new Failure(FailureCode.PRIVILEGES));
    }
}
