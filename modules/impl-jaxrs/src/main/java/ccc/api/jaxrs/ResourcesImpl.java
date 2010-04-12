/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.api.jaxrs;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.Resources;
import ccc.api.dto.AclDto;
import ccc.api.dto.ResourceDto;
import ccc.api.dto.ResourceSnapshot;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.RevisionDto;
import ccc.api.dto.TemplateSummary;
import ccc.api.types.DBC;
import ccc.api.types.Duration;
import ccc.api.types.SortOrder;
import ccc.plugins.s11n.Json;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/resources")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class ResourcesImpl
    extends
        JaxrsCollection
    implements
        Resources {

    private final Resources _delegate;

    /**
     * Constructor.
     *
     * @param resources The resources implementation delegated to.
     */
    public ResourcesImpl(final Resources resources) {
        _delegate = DBC.require().notNull(resources);
    }


    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final UUID resourceId) {
        return _delegate.cacheDuration(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final UUID resourceId) {
        return _delegate.computeTemplate(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        return _delegate.getAbsolutePath(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<RevisionDto> history(final UUID resourceId) {
        return _delegate.history(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        return _delegate.locked();
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId) {
        return _delegate.metadata(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId) {
        return _delegate.resource(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        return _delegate.resourceForLegacyId(legacyId);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> resourceForMetadataKey(
                                                            final String key) {
        return _delegate.resourceForMetadataKey(key);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path) {
        return _delegate.resourceForPath(path);
    }


    /** {@inheritDoc} */
    @Override
    public AclDto roles(final UUID resourceId) {
        return _delegate.roles(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu) {
        _delegate.updateCacheDuration(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) {
        _delegate.lock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID resourceId) {
        _delegate.applyWorkingCopy(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu) {
        _delegate.updateResourceTemplate(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void changeRoles(final UUID resourceId, final AclDto roles) {
        _delegate.changeRoles(resourceId, roles);
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId) {
        _delegate.move(resourceId, newParentId);
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId) {
        _delegate.publish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name) {
        _delegate.rename(resourceId, name);
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) {
        _delegate.unlock(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) {
        _delegate.unpublish(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final UUID resourceId) {
        _delegate.excludeFromMainMenu(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId) {
        _delegate.includeInMainMenu(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId, final Json json) {
        _delegate.updateMetadata(resourceId, json);
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID pageId) {
        _delegate.clearWorkingCopy(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId,
                                  final ResourceDto pu) {
        _delegate.createWorkingCopy(resourceId, pu);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id) {
        _delegate.deleteCacheDuration(id);
    }


    /** {@inheritDoc} */
    @Override
    public void deleteResource(final UUID resourceId) {
        _delegate.deleteResource(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail) {
        _delegate.createLogEntry(resourceId, action, detail);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getSiblings(final UUID resourceId) {
        return _delegate.getSiblings(resourceId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> list(
                                      final String tag,
                                      final Long before,
                                      final Long after,
                                      final String sort,
                                      final SortOrder order,
                                      final int pageNo,
                                      final int pageSize) {
        return _delegate.list(
            tag, before, after, sort, order, pageNo, pageSize);
    }


    /** {@inheritDoc} */
    @Override
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        return _delegate.fileContentsFromPath(absolutePath, charset);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot resourceForPathSecure(final String path) {
        return _delegate.resourceForPathSecure(path);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot revisionForPath(final String path,
                                            final int version) {
        return _delegate.revisionForPath(path, version);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot workingCopyForPath(final String path) {
        return _delegate.workingCopyForPath(path);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final UUID parentId,
                                        final String title) {
        return _delegate.createSearch(parentId, title);
    }
}
