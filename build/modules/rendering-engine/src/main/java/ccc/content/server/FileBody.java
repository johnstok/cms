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
package ccc.content.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import ccc.commons.DBC;
import ccc.domain.File;
import ccc.services.DataManager;


/**
 * Implementation of the {@link Body} interface that wraps a {@link File}.
 *
 * @author Civic Computing Ltd.
 */
public class FileBody
    implements
        Body {

    private final File _file;
    private final DataManager _dataManager;

    /**
     * Constructor.
     *
     * @param f The file this body represents.
     * @param dataManager The data manager used to retrieve the file's contents
     *      from the data store.
     */
    public FileBody(final File f, final DataManager dataManager) {
        DBC.require().notNull(f);
        DBC.require().notNull(dataManager);

        _file = f;
        _dataManager = dataManager;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset) throws IOException {
        _dataManager.retrieve(_file.data(), os);
    }
}
