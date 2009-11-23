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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import ccc.commons.Exceptions;
import ccc.rest.ServiceLocator;
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
    private final UUID _data;

    /**
     * Constructor.
     *
     * @param data The data to render.
     */
    public FileBody(final UUID data) {
        DBC.require().notNull(data);
        _data = data;
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
            int maxDimension = DEFAULT_MAX_DIMENSION;
            try {
                maxDimension = Integer.parseInt(r.getParameter("thumb"));
            } catch (final NumberFormatException e) {
                Exceptions.swallow(e);
            }
            files.retrieveThumb(_data, os, maxDimension);
        } else {
            files.retrieve(_data, os);
        }
    }
}
