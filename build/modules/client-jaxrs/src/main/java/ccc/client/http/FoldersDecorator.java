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

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.UUID;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.Folders;
import ccc.api.dto.DtoCollection;
import ccc.api.dto.FolderDelta;
import ccc.api.dto.FolderDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.exceptions.RestException;
import ccc.api.jaxrs.providers.RestExceptionMapper;
import ccc.api.types.SortOrder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class FoldersDecorator
    implements
        Folders {

    private final Folders _delegate;

    /**
     * Constructor.
     *
     * @param delegate The folders implementation to delegate to.
     */
    public FoldersDecorator(final Folders delegate) {
        _delegate = delegate;
    }

    /**
     * @param folder
     * @return
     * @throws RestException
     * @see ccc.api.Folders#createFolder(ccc.api.dto.FolderDto)
     */
    public ResourceSummary createFolder(final FolderDto folder) throws RestException {

        return _delegate.createFolder(folder);
    }

    /**
     * @param parentId
     * @param name
     * @param title
     * @param publish
     * @return
     * @deprecated
     * @see ccc.api.Folders#createFolder(java.util.UUID, java.lang.String, java.lang.String, boolean)
     */
    @Deprecated
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish) {

        return _delegate.createFolder(parentId, name, title, publish);
    }

    /**
     * @param name
     * @return
     * @see ccc.api.Folders#createRoot(java.lang.String)
     */
    public ResourceSummary createRoot(final String name) {
        try {
            return _delegate.createRoot(name);
        } catch (final ClientResponseFailure e) {
            throw convertException(e);
        }
    }

    /**
     * @param folderId
     * @return
     * @throws RestException
     * @see ccc.api.Folders#getAccessibleChildren(java.util.UUID)
     */
    public Collection<ResourceSummary> getAccessibleChildren(final UUID folderId) throws RestException {

        return _delegate.getAccessibleChildren(folderId);
    }

    /**
     * @param folderId
     * @return
     * @throws RestException
     * @see ccc.api.Folders#getChildren(java.util.UUID)
     */
    public Collection<ResourceSummary> getChildren(final UUID folderId) throws RestException {

        return _delegate.getChildren(folderId);
    }

    /**
     * @param folderId
     * @return
     * @throws RestException
     * @see ccc.api.Folders#getChildrenManualOrder(java.util.UUID)
     */
    public Collection<ResourceSummary> getChildrenManualOrder(final UUID folderId) throws RestException {

        return _delegate.getChildrenManualOrder(folderId);
    }

    /**
     * @param folderId
     * @param sort
     * @param sortOrder
     * @param pageNo
     * @param pageSize
     * @return
     * @throws RestException
     * @see ccc.api.Folders#getChildrenPaged(java.util.UUID, java.lang.String, ccc.api.types.SortOrder, int, int)
     */
    public DtoCollection<ResourceSummary> getChildrenPaged(final UUID folderId,
                                                           final String sort,
                                                           final SortOrder sortOrder,
                                                           final int pageNo,
                                                           final int pageSize) throws RestException {

        return _delegate.getChildrenPaged(folderId, sort, sortOrder, pageNo, pageSize);
    }

    /**
     * @param folderId
     * @return
     * @throws RestException
     * @see ccc.api.Folders#getFolderChildren(java.util.UUID)
     */
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId) throws RestException {

        return _delegate.getFolderChildren(folderId);
    }

    /**
     * @param folderId
     * @param name
     * @return
     * @throws RestException
     * @see ccc.api.Folders#nameExistsInFolder(java.util.UUID, java.lang.String)
     */
    public Boolean nameExistsInFolder(final UUID folderId, final String name) throws RestException {

        return _delegate.nameExistsInFolder(folderId, name);
    }

    /**
     * @return
     * @see ccc.api.Folders#roots()
     */
    public Collection<ResourceSummary> roots() {

        return _delegate.roots();
    }

    /**
     * @param folderId
     * @param delta
     * @throws RestException
     * @see ccc.api.Folders#updateFolder(java.util.UUID, ccc.api.dto.FolderDelta)
     */
    public void updateFolder(final UUID folderId, final FolderDelta delta) throws RestException {

        _delegate.updateFolder(folderId, delta);
    }




    /**
     * Convert a RestEasy exception to a CC API exception.
     *
     * @param <T> The type of exception that should be returned.
     * @param ex The RestEasy exception.
     *
     * @return The converted exception.
     */
    // FIXME: Move to a better location.
    public static <T extends RestException> T convertException(
                                             final ClientResponseFailure ex) {
        try {
            final ClientResponse<byte[]> r = ex.getResponse();
            final String body = new String(r.getEntity(), "UTF-8");
            return
                new RestExceptionMapper().<T>fromResponse(r.getStatus(), body);
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding", e);
        }
    }
}
