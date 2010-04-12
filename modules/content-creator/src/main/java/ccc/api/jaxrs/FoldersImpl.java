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
package ccc.api.jaxrs;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.Folders;
import ccc.api.dto.DtoCollection;
import ccc.api.dto.FolderDelta;
import ccc.api.dto.FolderDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.types.DBC;
import ccc.api.types.SortOrder;


/**
 * Implementation of the {@link Folders} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/folders")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class FoldersImpl
    extends
        JaxrsCollection
    implements
        Folders {

    private final Folders _delegate;


    /**
     * Constructor.
     *
     * @param delegate The folders implementation delegated to.
     */
    public FoldersImpl(final Folders delegate) {
        _delegate = DBC.require().notNull(delegate);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final UUID folderId) {
        return _delegate.getChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getAccessibleChildren(
                                                          final UUID folderId) {
        return _delegate.getAccessibleChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                        final UUID folderId) {
        return _delegate.getChildrenManualOrder(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId) {
        return _delegate.getFolderChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Boolean nameExistsInFolder(final UUID folderId, final String name) {
        return _delegate.nameExistsInFolder(folderId, name);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        return _delegate.roots();
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final FolderDto folder) {
        return _delegate.createFolder(folder);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFolder(final UUID folderId, final FolderDelta delta) {
        _delegate.updateFolder(folderId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public DtoCollection<ResourceSummary> getChildrenPaged(
                                                    final UUID folderId,
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int offset,
                                                    final int limit) {
        return _delegate.getChildrenPaged(folderId,
                                             sort,
                                             sortOrder,
                                             offset,
                                             limit);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish) {
        return _delegate.createFolder(parentId, name, title, publish);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        return _delegate.createRoot(name);
    }
}
