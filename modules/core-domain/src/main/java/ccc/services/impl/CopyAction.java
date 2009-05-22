/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ccc.commons.IO;
import ccc.services.DataManager.StreamAction;

/**
 * An action to copy from an input stream to an output stream.
 *
 * @author Civic Computing Ltd.
 */
final class CopyAction
    implements
        StreamAction {

    private final OutputStream _dataStream;

    /**
     * Constructor.
     *
     * @param dataStream The output stream to copy to.
     */
    CopyAction(final OutputStream dataStream) {
        _dataStream = dataStream;
    }


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws IOException {
        IO.copy(is, _dataStream);
    }
}
