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
package ccc.content.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ByteArrayBody
    implements
        Body {

    private final byte[] _bytes;

    /**
     * Constructor.
     *
     * @param bytes
     */
    public ByteArrayBody (final byte[] bytes) {
        _bytes = bytes;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset) throws IOException {
        os.write(_bytes);
    }
}
