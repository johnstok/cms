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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import ccc.commons.Context;



/**
 * A response body defined by a byte array.
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
     * @param bytes The body data.
     */
    public ByteArrayBody(final byte[] bytes) {
        _bytes = Arrays.copyOf(bytes, bytes.length);
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) throws IOException {
        os.write(_bytes);
    }
}
