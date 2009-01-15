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

import ccc.commons.DBC;
import ccc.domain.File;
import ccc.domain.Resource;
import ccc.services.DataManager;


/**
 * TODO: Add Description for this type.
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
     * @param f
     * @param dataManager
     */
    public FileBody(final File f, final DataManager dataManager) {
        DBC.require().notNull(f);
        DBC.require().notNull(dataManager);

        _file = f;
        _dataManager = dataManager;
    }

    /** {@inheritDoc} */
    @Override
    public Resource getResource() {
        return _file;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os) throws IOException {
        _dataManager.retrieve(_file.data(), os);
    }

}
