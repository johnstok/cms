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

package ccc.persistence.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import ccc.persistence.StreamAction;
import ccc.serialization.IO;

/**
 * An action to copy from an input stream to an output stream.
 *
 * @author Civic Computing Ltd.
 */
public final class CopyAction
    implements
        StreamAction {
    private static final Logger LOG = Logger.getLogger(CopyAction.class);

    private final OutputStream _dataStream;

    /**
     * Constructor.
     *
     * @param dataStream The output stream to copy to.
     */
    public CopyAction(final OutputStream dataStream) {
        _dataStream = dataStream;
    }


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws IOException {
        LOG.debug("Starting write.");
        IO.copy(is, _dataStream);
        LOG.debug("Write completed.");
    }
}
