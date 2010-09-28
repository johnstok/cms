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

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.core.Folder;
import ccc.api.core.Folders;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;


/**
 * Implementation of the {@link Folders} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
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
    public PagedCollection<ResourceSummary> getAccessibleChildren(
                                                          final UUID folderId) {
        try {
            return _delegate.getAccessibleChildren(folderId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary nameExistsInFolder(final UUID folderId,
                                              final String name) {
        try {
            return _delegate.nameExistsInFolder(folderId, name);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> roots() {
        try {
            return _delegate.roots();
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Folder create(final Folder folder) {
        try {
            return _delegate.create(folder);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Folder update(final UUID folderId, final Folder delta) {
        try {
            return _delegate.update(folderId, delta);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Folder createFolder(final UUID parentId,
                               final String name,
                               final String title,
                               final boolean publish) {
        try {
            return _delegate.createFolder(parentId, name, title, publish);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Folder createRoot(final String name) {
        try {
            return _delegate.createRoot(name);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

}
