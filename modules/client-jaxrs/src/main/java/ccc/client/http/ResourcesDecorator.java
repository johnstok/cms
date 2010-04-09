/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.client.http;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import ccc.api.Resources;
import ccc.api.dto.AclDto;
import ccc.api.dto.ResourceDto;
import ccc.api.dto.ResourceSnapshot;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.RevisionDto;
import ccc.api.dto.TemplateSummary;
import ccc.api.exceptions.RestException;
import ccc.api.exceptions.UnauthorizedException;
import ccc.api.types.DBC;
import ccc.api.types.Duration;
import ccc.api.types.HttpStatusCode;
import ccc.api.types.SortOrder;
import ccc.plugins.s11n.Json;


/**
 * Resources decorator to work around RestEasy client bugs.
 *
 * @author Civic Computing Ltd.
 */
class ResourcesDecorator
    implements
        Resources {

    private final Resources _delegate;
    private final String _base;
    private final HttpClient _http;


    /**
     * Constructor.
     *
     * @param delegate
     * @param basePath
     * @param http
     */
    public ResourcesDecorator(final Resources delegate,
                              final String basePath,
                              final HttpClient http) {
        _delegate = DBC.require().notNull(delegate);
        _base     = DBC.require().notNull(basePath);
        _http     = DBC.require().notNull(http);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#applyWorkingCopy(java.util.UUID)
     */
    public void applyWorkingCopy(final UUID resourceId) throws RestException {
        _delegate.applyWorkingCopy(resourceId);
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#cacheDuration(java.util.UUID)
     */
    public Duration cacheDuration(final UUID resourceId) throws RestException {

        return _delegate.cacheDuration(resourceId);
    }


    /**
     * @param resourceId
     * @param acl
     * @throws RestException
     * @see ccc.api.Resources#changeRoles(java.util.UUID, ccc.api.dto.AclDto)
     */
    public void changeRoles(final UUID resourceId, final AclDto acl) throws RestException {

        _delegate.changeRoles(resourceId, acl);
    }


    /**
     * @param pageId
     * @throws RestException
     * @see ccc.api.Resources#clearWorkingCopy(java.util.UUID)
     */
    public void clearWorkingCopy(final UUID pageId) throws RestException {

        _delegate.clearWorkingCopy(pageId);
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#computeTemplate(java.util.UUID)
     */
    public TemplateSummary computeTemplate(final UUID resourceId) throws RestException {

        return _delegate.computeTemplate(resourceId);
    }


    /**
     * @param resourceId
     * @param action
     * @param detail
     * @throws RestException
     * @see ccc.api.Resources#createLogEntry(java.util.UUID, java.lang.String, java.lang.String)
     */
    public void createLogEntry(final UUID resourceId, final String action, final String detail) throws RestException {

        _delegate.createLogEntry(resourceId, action, detail);
    }


    /**
     * @param resourceId
     * @param dto
     * @throws RestException
     * @see ccc.api.Resources#createWorkingCopy(java.util.UUID, ccc.api.dto.ResourceDto)
     */
    public void createWorkingCopy(final UUID resourceId, final ResourceDto dto) throws RestException {

        _delegate.createWorkingCopy(resourceId, dto);
    }


    /**
     * @param id
     * @throws RestException
     * @see ccc.api.Resources#deleteCacheDuration(java.util.UUID)
     */
    public void deleteCacheDuration(final UUID id) throws RestException {

        _delegate.deleteCacheDuration(id);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#deleteResource(java.util.UUID)
     */
    public void deleteResource(final UUID resourceId) throws RestException {

        _delegate.deleteResource(resourceId);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#excludeFromMainMenu(java.util.UUID)
     */
    public void excludeFromMainMenu(final UUID resourceId) throws RestException {

        _delegate.excludeFromMainMenu(resourceId);
    }


    /**
     * @param absolutePath
     * @param charset
     * @return
     * @see ccc.api.Resources#fileContentsFromPath(java.lang.String, java.lang.String)
     */
    public String fileContentsFromPath(final String absolutePath, final String charset) {

        return _delegate.fileContentsFromPath(absolutePath, charset);
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#getAbsolutePath(java.util.UUID)
     */
    public String getAbsolutePath(final UUID resourceId) throws RestException {

        return _delegate.getAbsolutePath(resourceId);
    }


    /**
     * @param resourceId
     * @return
     * @see ccc.api.Resources#getSiblings(java.util.UUID)
     */
    public Collection<ResourceSummary> getSiblings(final UUID resourceId) {

        return _delegate.getSiblings(resourceId);
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#history(java.util.UUID)
     */
    public Collection<RevisionDto> history(final UUID resourceId) throws RestException {

        return _delegate.history(resourceId);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#includeInMainMenu(java.util.UUID)
     */
    public void includeInMainMenu(final UUID resourceId) throws RestException {

        _delegate.includeInMainMenu(resourceId);
    }


    /**
     * @param tag
     * @param before
     * @param after
     * @param sort
     * @param order
     * @param pageNo
     * @param pageSize
     * @return
     * @throws RestException
     * @see ccc.api.Resources#list(java.lang.String, java.lang.Long, java.lang.Long, java.lang.String, ccc.api.types.SortOrder, int, int)
     */
    public Collection<ResourceSummary> list(final String tag,
                                            final Long before,
                                            final Long after,
                                            final String sort,
                                            final SortOrder order,
                                            final int pageNo,
                                            final int pageSize) throws RestException {

        return _delegate.list(tag, before, after, sort, order, pageNo, pageSize);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#lock(java.util.UUID)
     */
    public void lock(final UUID resourceId) throws RestException {

        _delegate.lock(resourceId);
    }


    /**
     * @return
     * @see ccc.api.Resources#locked()
     */
    public Collection<ResourceSummary> locked() {

        return _delegate.locked();
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#metadata(java.util.UUID)
     */
    public Map<String, String> metadata(final UUID resourceId) throws RestException {

        return _delegate.metadata(resourceId);
    }


    /**
     * @param resourceId
     * @param newParentId
     * @throws RestException
     * @see ccc.api.Resources#move(java.util.UUID, java.util.UUID)
     */
    public void move(final UUID resourceId, final UUID newParentId) throws RestException {

        _delegate.move(resourceId, newParentId);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#publish(java.util.UUID)
     */
    public void publish(final UUID resourceId) throws RestException {

        _delegate.publish(resourceId);
    }


    /**
     * @param resourceId
     * @param name
     * @throws RestException
     * @see ccc.api.Resources#rename(java.util.UUID, java.lang.String)
     */
    public void rename(final UUID resourceId, final String name) throws RestException {

        _delegate.rename(resourceId, name);
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @throws UnauthorizedException
     * @see ccc.api.Resources#resource(java.util.UUID)
     */
    public ResourceSummary resource(final UUID resourceId) throws RestException, UnauthorizedException {

        return _delegate.resource(resourceId);
    }


    /**
     * @param legacyId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#resourceForLegacyId(java.lang.String)
     */
    public ResourceSummary resourceForLegacyId(final String legacyId) throws RestException {

        return _delegate.resourceForLegacyId(legacyId);
    }


    /**
     * @param key
     * @return
     * @throws RestException
     * @see ccc.api.Resources#resourceForMetadataKey(java.lang.String)
     */
    public Collection<ResourceSummary> resourceForMetadataKey(final String key) throws RestException {

        return _delegate.resourceForMetadataKey(key);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary resourceForPath(final String path) {
        /*
         * This method works around an encoding issue in REST-EASY 1.1.
         */

        final ClientRequest request =
            new ClientRequest(_base+"/resources/by-path"+path, _http);

        try {
            final ClientResponse<ResourceSummary> response =
                request.get(ResourceSummary.class);
            if (response.getStatus() == HttpStatusCode.OK) {
                return response.getEntity();
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Request failed.");
    }


    /**
     * @param path
     * @return
     * @deprecated
     * @see ccc.api.Resources#resourceForPathSecure(java.lang.String)
     */
    @Deprecated
    public ResourceSnapshot resourceForPathSecure(final String path) {

        return _delegate.resourceForPathSecure(path);
    }


    /**
     * @param path
     * @param version
     * @return
     * @deprecated
     * @see ccc.api.Resources#revisionForPath(java.lang.String, int)
     */
    @Deprecated
    public ResourceSnapshot revisionForPath(final String path, final int version) {

        return _delegate.revisionForPath(path, version);
    }


    /**
     * @param resourceId
     * @return
     * @throws RestException
     * @see ccc.api.Resources#roles(java.util.UUID)
     */
    public AclDto roles(final UUID resourceId) throws RestException {

        return _delegate.roles(resourceId);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#unlock(java.util.UUID)
     */
    public void unlock(final UUID resourceId) throws RestException {

        _delegate.unlock(resourceId);
    }


    /**
     * @param resourceId
     * @throws RestException
     * @see ccc.api.Resources#unpublish(java.util.UUID)
     */
    public void unpublish(final UUID resourceId) throws RestException {

        _delegate.unpublish(resourceId);
    }


    /**
     * @param resourceId
     * @param duration
     * @throws RestException
     * @see ccc.api.Resources#updateCacheDuration(java.util.UUID, ccc.api.dto.ResourceDto)
     */
    public void updateCacheDuration(final UUID resourceId, final ResourceDto duration) throws RestException {

        _delegate.updateCacheDuration(resourceId, duration);
    }


    /**
     * @param resourceId
     * @param json
     * @throws RestException
     * @see ccc.api.Resources#updateMetadata(java.util.UUID, ccc.plugins.s11n.Json)
     */
    public void updateMetadata(final UUID resourceId, final Json json) throws RestException {

        _delegate.updateMetadata(resourceId, json);
    }


    /**
     * @param resourceId
     * @param template
     * @throws RestException
     * @see ccc.api.Resources#updateResourceTemplate(java.util.UUID, ccc.api.dto.ResourceDto)
     */
    public void updateResourceTemplate(final UUID resourceId, final ResourceDto template) throws RestException {

        _delegate.updateResourceTemplate(resourceId, template);
    }


    /**
     * @param path
     * @return
     * @throws RestException
     * @throws UnauthorizedException
     * @deprecated
     * @see ccc.api.Resources#workingCopyForPath(java.lang.String)
     */
    @Deprecated
    public ResourceSnapshot workingCopyForPath(final String path) throws RestException, UnauthorizedException {

        return _delegate.workingCopyForPath(path);
    }


    /**
     * @param parentId
     * @param title
     * @return
     * @see ccc.api.Resources#createSearch(java.util.UUID, java.lang.String)
     */
    public ResourceSummary createSearch(final UUID parentId, final String title) {

        return _delegate.createSearch(parentId, title);
    }
}
