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

import ccc.rest.Files;
import ccc.rest.RestException;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.dto.TextFileDto;


/**
 * Implementation of the {@link Files} API.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/files")
@Consumes("application/json")
@Produces("application/json")
public class FilesImpl
    extends
        JaxrsCollection
    implements
        Files {


    /** {@inheritDoc} */
    @Override
    public TextFileDelta get(final UUID fileId) throws RestException {
        return getFiles().get(fileId);
    }

    /** {@inheritDoc} */
    @Override
    public void update(final UUID id, final TextFileDelta file)
    throws RestException {
        getFiles().update(id, file);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTextFile(final TextFileDto textFile)
    throws RestException {
        return getFiles().createTextFile(textFile);
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FileDto> getPagedImages(final UUID folderId,
                                              final int pageNo,
                                              final int pageSize)
                                              throws RestException {
        return getFiles().getPagedImages(folderId, pageNo, pageSize);
    }

    /** {@inheritDoc} */
    @Override
    public String getImagesCount(final UUID folderId) throws RestException  {
        return getFiles().getImagesCount(folderId);
    }
}
