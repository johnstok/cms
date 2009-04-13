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

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class CachingOutputStream
    extends
        FilterOutputStream {

    private final ByteArrayOutputStream _cache = new ByteArrayOutputStream();

    /**
     * Constructor.
     *
     * @param out The output stream we decorate.
     */
    public CachingOutputStream(final OutputStream out) {
        super(out);
    }


    /** {@inheritDoc} */
    @Override
    public void write(final byte[] b,
                      final int off,
                      final int len) throws IOException {
        super.write(b, off, len);
        _cache.write(b, off, len);
    }


    /** {@inheritDoc} */
    @Override
    public void write(final byte[] b) throws IOException {
        super.write(b);
        _cache.write(b);
    }


    /** {@inheritDoc} */
    @Override
    public void write(final int b) throws IOException {
        super.write(b);
        _cache.write(b);
    }

    public byte[] cache() {
        return _cache.toByteArray();
    }
}
