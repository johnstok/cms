/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.commons.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import ccc.api.core.StreamAction;
import ccc.commons.IO;

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
