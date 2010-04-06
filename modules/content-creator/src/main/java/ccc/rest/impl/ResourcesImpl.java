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

package ccc.rest.impl;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Resources;
import ccc.rest.dto.AclDto;
import ccc.rest.dto.ResourceDto;
import ccc.rest.dto.ResourceSnapshot;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.RevisionDto;
import ccc.rest.dto.TemplateSummary;
import ccc.serialization.Json;
import ccc.types.Duration;
import ccc.types.SortOrder;


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


    /** {@inheritDoc} */
    @Override
    public Duration cacheDuration(final UUID resourceId) {
        try {
            return getResources().cacheDuration(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public TemplateSummary computeTemplate(final UUID resourceId) {
        try {
            return getResources().computeTemplate(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        try {
            return getResources().getAbsolutePath(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<RevisionDto> history(final UUID resourceId) {
        try {
            return getResources().history(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> locked() {
        try {
            return getResources().locked();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId) {
        try {
            return getResources().metadata(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId) {
        try {
            return getResources().resource(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        try {
            return getResources().resourceForLegacyId(legacyId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> resourceForMetadataKey(
                                                            final String key) {
        try {
            return getResources().resourceForMetadataKey(key);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path) {
        try {
            return getResources().resourceForPath(path);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public AclDto roles(final UUID resourceId) {
        try {
            return getResources().roles(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final ResourceDto pu) {
        try {
            getResources().updateCacheDuration(resourceId, pu);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) {
        try {
            getResources().lock(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID resourceId) {
        try {
            getResources().applyWorkingCopy(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final ResourceDto pu) {
        try {
            getResources().updateResourceTemplate(resourceId, pu);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void changeRoles(final UUID resourceId, final AclDto roles) {
        try {
            getResources().changeRoles(resourceId, roles);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId) {
        try {
            getResources().move(resourceId, newParentId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId) {
        try {
            getResources().publish(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name) {
        try {
            getResources().rename(resourceId, name);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) {
        try {
            getResources().unlock(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) {
        try {
            getResources().unpublish(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final UUID resourceId) {
        try {
            getResources().excludeFromMainMenu(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId) {
        try {
            getResources().includeInMainMenu(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId, final Json json) {
        try {
            getResources().updateMetadata(resourceId, json);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID pageId) {
        try {
            getResources().clearWorkingCopy(pageId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId,
                                  final ResourceDto pu) {
        try {
            getResources().createWorkingCopy(resourceId, pu);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id) {
        try {
            getResources().deleteCacheDuration(id);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void deleteResource(final UUID resourceId) {
        try {
            getResources().deleteResource(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail) {
        try {
            getResources().createLogEntry(resourceId, action, detail);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getSiblings(final UUID resourceId) {
        try {
            return getResources().getSiblings(resourceId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
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
        try {
            return getResources().list(
                tag, before, after, sort, order, pageNo, pageSize);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        try {
            return getResources().fileContentsFromPath(absolutePath, charset);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot resourceForPathSecure(final String path) {
        try {
            return getResources().resourceForPathSecure(path);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot revisionForPath(final String path,
                                            final int version) {
        try {
            return getResources().revisionForPath(path, version);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSnapshot workingCopyForPath(final String path) {
        try {
            return getResources().workingCopyForPath(path);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final UUID parentId,
                                        final String title) {
        try {
            return getResources().createSearch(parentId, title);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }
}
