/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering;

import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ccc.commons.Context;
import ccc.persistence.streams.CopyAction;
import ccc.persistence.streams.ThumbAction;
import ccc.rest.RestException;
import ccc.rest.ServiceLocator;
import ccc.rest.UnauthorizedException;
import ccc.rest.dto.FileDto;
import ccc.rest.extensions.FilesExt;
import ccc.types.DBC;


/**
 * Implementation of the {@link Body} interface that wraps a {@link File}.
 *
 * @author Civic Computing Ltd.
 */
public class FileBody
    implements
        Body {
    private static final Logger LOG = Logger.getLogger(FileBody.class);

    private final FileDto _file;

    /**
     * Constructor.
     *
     * @param file The file to render.
     */
    public FileBody(final FileDto file) {
        DBC.require().notNull(file);
        _file = file;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) {
        final ServiceLocator sl = context.get("services", ServiceLocator.class);
        final FilesExt files = (FilesExt) sl.getFiles();
        final HttpServletRequest r =
            context.get("request", HttpServletRequest.class);

        try {
            if (r != null && r.getParameter("thumb") != null) {
                files.retrieve(_file.getId(),
                    new ThumbAction(os, r.getParameter("thumb")));

            } else {
                if (_file.isWorkingCopy()) {
                    LOG.debug("Writing file contents for working copy.");
                    files.retrieveWorkingCopy(
                        _file.getId(), new CopyAction(os));
                } else {
                    LOG.debug(
                        "Writing file contents for revision "
                        + _file.getRevision()
                        + ".");
                    files.retrieveRevision(
                        _file.getId(), _file.getRevision(), new CopyAction(os));
                }
            }
        } catch (final RestException e) {
            throw new RuntimeException(e);
        } catch (final UnauthorizedException e) {
            throw new AuthenticationRequiredException(
                _file.getAbsolutePath());
        }
    }
}
