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
package ccc.search;

import java.io.InputStream;
import java.nio.charset.Charset;

import ccc.persistence.StreamAction;
import ccc.serialization.IO;

/**
 * A stream action that reads a input stream into a string.
 *
 * @author Civic Computing Ltd.
 */
public class TxtExtractor implements StreamAction {

    private String _content;

    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws Exception {
        // Assume files have come from windows.
        _content = IO.toString(is, Charset.forName("windows-1252"));
    }

    /**
     * Accessor.
     *
     * @return The contents of the stream, as a string.
     */
    public String getContent() {
        return _content;
    }
}
