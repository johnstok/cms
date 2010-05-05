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

import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Resources;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.types.ACL;
import ccc.api.types.DBC;
import ccc.api.types.Duration;
import ccc.api.types.PagedCollection;
import ccc.api.types.SortOrder;


/**
 * This class exposes parts of our public API using JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
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
        try {
            return _delegate.cacheDuration(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Template computeTemplate(final UUID resourceId) {
        try {
            return _delegate.computeTemplate(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String getAbsolutePath(final UUID resourceId) {
        try {
            return _delegate.getAbsolutePath(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<Revision> history(final UUID resourceId) {
        try {
            return _delegate.history(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, String> metadata(final UUID resourceId) {
        try {
            return _delegate.metadata(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resource(final UUID resourceId) {
        try {
            return _delegate.resource(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForLegacyId(final String legacyId) {
        try {
            return _delegate.resourceForLegacyId(legacyId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> resourceForMetadataKey(
                                                            final String key) {
        try {
            return _delegate.resourceForMetadataKey(key);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path) {
        try {
            return _delegate.resourceForPath(path);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ACL acl(final UUID resourceId) {
        try {
            return _delegate.acl(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateCacheDuration(final UUID resourceId,
                                    final Resource pu) {
        try {
            _delegate.updateCacheDuration(resourceId, pu);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void lock(final UUID resourceId) {
        try {
            _delegate.lock(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID resourceId) {
        try {
            _delegate.applyWorkingCopy(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final UUID resourceId,
                                       final Resource pu) {
        try {
            _delegate.updateResourceTemplate(resourceId, pu);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void changeAcl(final UUID resourceId, final ACL acl) {
        try {
            _delegate.changeAcl(resourceId, acl);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void move(final UUID resourceId, final UUID newParentId) {
        try {
            _delegate.move(resourceId, newParentId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void publish(final UUID resourceId) {
        try {
            _delegate.publish(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void rename(final UUID resourceId, final String name) {
        try {
            _delegate.rename(resourceId, name);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void unlock(final UUID resourceId) {
        try {
            _delegate.unlock(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void unpublish(final UUID resourceId) {
        try {
            _delegate.unpublish(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void excludeFromMainMenu(final UUID resourceId) {
        try {
            _delegate.excludeFromMainMenu(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void includeInMainMenu(final UUID resourceId) {
        try {
            _delegate.includeInMainMenu(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateMetadata(final UUID resourceId,
                               final Resource resource) {
        try {
            _delegate.updateMetadata(resourceId, resource);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID pageId) {
        try {
            _delegate.clearWorkingCopy(pageId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void createWorkingCopy(final UUID resourceId,
                                  final Resource pu) {
        try {
            _delegate.createWorkingCopy(resourceId, pu);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void deleteCacheDuration(final UUID id) {
        try {
            _delegate.deleteCacheDuration(id);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void deleteResource(final UUID resourceId) {
        try {
            _delegate.deleteResource(resourceId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void createLogEntry(final UUID resourceId,
                               final String action,
                               final String detail) {
        try {
            _delegate.createLogEntry(resourceId, action, detail);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> list(
                                                final UUID parent,
                                                final String tag,
                                                final Long before,
                                                final Long after,
                                                final String mainMenu,
                                                final String type,
                                                final String locked,
                                                final String published,
                                                final String sort,
                                                final SortOrder order,
                                                final int pageNo,
                                                final int pageSize) {
        try {
            return _delegate.list(parent,
                tag,
                before,
                after,
                mainMenu,
                type,
                locked,
                published,
                sort,
                order,
                pageNo,
                pageSize);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String fileContentsFromPath(final String absolutePath,
                                       final String charset) {
        try {
            return _delegate.fileContentsFromPath(absolutePath, charset);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Resource resourceForPathSecure(final String path) {
        try {
            return _delegate.resourceForPathSecure(path);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Resource revisionForPath(final String path,
                                            final int version) {
        try {
            return _delegate.revisionForPath(path, version);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Resource workingCopyForPath(final String path) {
        try {
            return _delegate.workingCopyForPath(path);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createSearch(final UUID parentId,
                                        final String title) {
        try {
            return _delegate.createSearch(parentId, title);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

}
