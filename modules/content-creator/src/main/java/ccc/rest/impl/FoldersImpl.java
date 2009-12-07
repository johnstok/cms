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
package ccc.rest.impl;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.Folders;
import ccc.rest.RestException;
import ccc.rest.dto.FolderDelta;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.ResourceSummary;


/**
 * Implementation of the {@link Folders} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class FoldersImpl
    extends
        JaxrsCollection
    implements
        Folders {


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildren(final UUID folderId)
    throws RestException {
        return getFolders().getChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getAccessibleChildren(final UUID folderId)
    throws RestException {
        return getFolders().getAccessibleChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getChildrenManualOrder(
                                                        final UUID folderId)
    throws RestException {
        return getFolders().getChildrenManualOrder(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> getFolderChildren(final UUID folderId)
    throws RestException {
        return getFolders().getFolderChildren(folderId);
    }


    /** {@inheritDoc} */
    @Override
    public Boolean nameExistsInFolder(final UUID folderId, final String name)
    throws RestException {
        return getFolders().nameExistsInFolder(folderId, name);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<ResourceSummary> roots() {
        return getFolders().roots();
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final FolderDto folder)
    throws RestException {
        return getFolders().createFolder(folder);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFolder(final UUID folderId, final FolderDelta delta)
    throws RestException {
        getFolders().updateFolder(folderId, delta);
    }
}
