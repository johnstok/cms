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

import ccc.domain.User;
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
    private final DataRepository _dataRepository;

    /**
     * Constructor.
     *
     * @param f The file this body represents.
     * @param dataRepository The data manager used to retrieve the file's
     *      contents from the data store.
     */
    public FileBody(final FileSnapshot f, final DataRepository dataRepository) {
        DBC.require().notNull(f);
        DBC.require().notNull(dataRepository);

        _file = f;
        _dataRepository = dataRepository;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final User user,
                      final TextProcessor processor) {
        _dataRepository.retrieve(_file.getData(), os);
    }
}
