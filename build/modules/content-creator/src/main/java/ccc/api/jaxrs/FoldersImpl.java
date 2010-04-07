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

import javax.ejb.EJBException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Folders;
import ccc.rest.dto.DtoCollection;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceSummary;
import ccc.types.SortOrder;


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

    private Folders _delegate;


    public Folders getFolders() {
        return (null==_delegate) ? defaultFolders() : _delegate;
    }


    public void setFolders(final Folders users) {
        _delegate = users;
    }


    /**
     * Decorate an exiting folders object with a new {@link FoldersImpl}.
     *
     * @param folders The implementation to decorate.
     *
     * @return The decorated implementation.
     */
    public static FoldersImpl decorate(final Folders folders) {
        final FoldersImpl fi = new FoldersImpl();
        fi.setFolders(folders);
        return fi;
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final UUID folderId) {
        try {
            return getFolders().getChildren(folderId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getAccessibleChildren(
                                                          final UUID folderId) {
        try {
            return getFolders().getAccessibleChildren(folderId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                        final UUID folderId) {
        try {
            return getFolders().getChildrenManualOrder(folderId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId) {
        try {
            return getFolders().getFolderChildren(folderId);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Boolean nameExistsInFolder(final UUID folderId, final String name) {
        try {
            return getFolders().nameExistsInFolder(folderId, name);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        try {
            return getFolders().roots();
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final FolderDto folder) {
        try {
            return getFolders().createFolder(folder);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateFolder(final UUID folderId, final FolderDelta delta) {
        try {
            getFolders().updateFolder(folderId, delta);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public DtoCollection<ResourceSummary> getChildrenPaged(
                                                    final UUID folderId,
                                                    final String sort,
                                                    final SortOrder sortOrder,
                                                    final int offset,
                                                    final int limit) {
        try {
            return getFolders().getChildrenPaged(folderId,
                                                 sort,
                                                 sortOrder,
                                                 offset,
                                                 limit);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final UUID parentId,
                                        final String name,
                                        final String title,
                                        final boolean publish) {
        try {
            return getFolders().createFolder(parentId, name, title, publish);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createRoot(final String name) {
        try {
            return getFolders().createRoot(name);
        } catch (final EJBException e) {
            throw convertToNative(e);
        }
    }
}
