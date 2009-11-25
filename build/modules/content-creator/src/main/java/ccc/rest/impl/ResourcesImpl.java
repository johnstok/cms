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
import ccc.rest.UnauthorizedException;
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
    public Duration cacheDuration(final UUID resourceId) throws RestException {
        return getResources().cacheDuration(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final UUID resourceId)
    throws RestException {
        return getResources().computeTemplate(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) throws RestException {
        return getResources().getAbsolutePath(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<RevisionDto> history(final UUID resourceId)
    throws RestException {
        return getResources().history(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        return getResources().locked();
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId)
    throws RestException {
        return getResources().metadata(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId)
    throws RestException {
        return getResources().resource(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId)
    throws RestException {
        return getResources().resourceForLegacyId(legacyId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> resourceForMetadataKey(final String key)
    throws RestException {
        return getResources().resourceForMetadataKey(key);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path)
    throws RestException {
        return getResources().resourceForPath(path);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> roles(final UUID resourceId)
    throws RestException {
        return getResources().roles(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu)
    throws RestException {
        getResources().updateCacheDuration(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) throws RestException {
        getResources().lock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID resourceId)
    throws RestException {
        getResources().applyWorkingCopy(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu)
    throws RestException {
        getResources().updateResourceTemplate(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void changeRoles(final UUID resourceId,
                            final Collection<String> roles)
    throws RestException {
        getResources().changeRoles(resourceId, roles);
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId)
    throws RestException {
        getResources().move(resourceId, newParentId);
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId) throws RestException {
        getResources().publish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name)
    throws RestException {
        getResources().rename(resourceId, name);
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) throws RestException {
        getResources().unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) throws RestException {
        getResources().unpublish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final UUID resourceId)
    throws RestException {
        getResources().excludeFromMainMenu(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId)
    throws RestException {
        getResources().includeInMainMenu(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId, final Json json)
    throws RestException {
        getResources().updateMetadata(resourceId, json);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID pageId)
    throws RestException {
        getResources().clearWorkingCopy(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId,
                                  final ResourceDto pu)
    throws RestException {
        getResources().createWorkingCopy(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id)
    throws RestException {
        getResources().updateCacheDuration(id, (Duration) null);
    }


    /** {@inheritDoc} */
    @Override
    public void fail() throws RestException {
        throw new RestException(new Failure(FailureCode.PRIVILEGES));
    }


    /** {@inheritDoc} */
    @Override
    public void deleteResource(final UUID resourceId) throws RestException {
        getResources().deleteResource(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail)
    throws RestException {
        getResources().createLogEntry(resourceId, action, detail);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<Collection<ResourceSummary>> getMenuResources(
        final UUID resourceId) throws RestException, UnauthorizedException {
        return getResources().getMenuResources(resourceId);
    }
}
