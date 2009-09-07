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
import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;


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
    public FileDelta fileDelta(final UUID fileId) {
        return getQueries().fileDelta(fileId);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<FileDto> getAllContentImages() {
        return getQueries().getAllContentImages();
    }
}
