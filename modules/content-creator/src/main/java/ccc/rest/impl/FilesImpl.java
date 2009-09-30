/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
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
    public Collection<FileDto> getAllContentImages() {
        return getFiles().getAllContentImages();
    }

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
    public ResourceSummary createTextFile(final TextFileDto textFile) throws RestException {
        return getFiles().createTextFile(textFile);
    }
}
