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

import ccc.commons.Exceptions;
import ccc.persistence.DataRepository;
import ccc.snapshots.FileSnapshot;
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
    private final FileSnapshot _file;

    /**
     * Constructor.
     *
     * @param f The file this body represents.
     */
    public FileBody(final FileSnapshot f) {
        DBC.require().notNull(f);
        _file = f;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) {
        final DataRepository dataRepository =
            context.get("data", DataRepository.class);
        final HttpServletRequest r =
            (HttpServletRequest) context.getExtras().get("request");
        if (r != null && r.getParameter("thumb") != null) {
            int maxDimension = DEFAULT_MAX_DIMENSION;
            try {
                maxDimension = Integer.parseInt(r.getParameter("thumb"));
            } catch (final NumberFormatException e) {
                Exceptions.swallow(e);
            }
            dataRepository.retrieveThumb(_file.getData(), os, maxDimension);
        } else {
            dataRepository.retrieve(_file.getData(), os);
        }
    }
}
