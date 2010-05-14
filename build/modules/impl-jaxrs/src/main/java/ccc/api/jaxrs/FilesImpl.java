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

import ccc.api.core.File;
import ccc.api.core.Files;
import ccc.api.core.PagedCollection;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;
import ccc.api.types.StreamAction;


/**
 * Implementation of the {@link Files} API.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class FilesImpl
    extends
        JaxrsCollection
    implements
        Files {

    private final Files _files;


    /**
     * Constructor.
     *
     * @param files The files implementation delegated to.
     */
    public FilesImpl(final Files files) {
        _files = DBC.require().notNull(files);
    }


    /** {@inheritDoc} */
    @Override
    public File retrieve(final UUID fileId) {
        try {
            return _files.retrieve(fileId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void update(final UUID id, final File file) {
        try {
            _files.update(id, file);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTextFile(final File textFile) {
        try {
            return _files.createTextFile(textFile);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public PagedCollection<File> getPagedImages(final UUID folderId,
                                                 final int pageNo,
                                                 final int pageSize) {
        try {
            return _files.getPagedImages(folderId, pageNo, pageSize);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary create(final File file) {
        try {
            return _files.create(file);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void retrieve(final UUID file, final StreamAction action) {
        try {
            _files.retrieve(file, action);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void retrieveRevision(final UUID file,
                                 final int revision,
                                 final StreamAction action) {
        try {
            _files.retrieveRevision(file, revision, action);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void retrieveWorkingCopy(final UUID file,
                                    final StreamAction action) {
        try {
            _files.retrieveWorkingCopy(file, action);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateFile(final UUID fileId, final File file) {
        try {
            return _files.updateFile(fileId, file);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }
}
