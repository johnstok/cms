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

import ccc.commons.Context;
import ccc.commons.Exceptions;
import ccc.persistence.streams.CopyAction;
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

    /** DEFAULT_MAX_DIMENSION : int. */
    private static final int DEFAULT_MAX_DIMENSION = 200;
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

        if (r != null && r.getParameter("thumb") != null) {
            // TODO: Extract this code to a ThiumbnailStreamAction class.
            int maxDimension = DEFAULT_MAX_DIMENSION;
            try {
                maxDimension = Integer.parseInt(r.getParameter("thumb"));
            } catch (final NumberFormatException e) {
                Exceptions.swallow(e);
            }
            files.retrieveThumb(_file.getData(), os, maxDimension);

        } else {
            try {
                if (_file.isWorkingCopy()) {
                    files.retrieveWorkingCopy(
                        _file.getId(), new CopyAction(os));
                } else if (_file.isCurrentRevision()) {
                    files.retrieve(
                        _file.getId(), new CopyAction(os));
                } else {
                    files.retrieveRevision(
                        _file.getId(), _file.getRevision(), new CopyAction(os));
                }
            } catch (final RestException e) {
                throw new RuntimeException(e);
            } catch (final UnauthorizedException e) {
                throw new AuthenticationRequiredException(
                    _file.getAbsolutePath());
            }
        }
    }
}
