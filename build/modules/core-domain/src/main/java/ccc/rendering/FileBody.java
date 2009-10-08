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
        dataRepository.retrieve(_file.getData(), os);
    }
}
