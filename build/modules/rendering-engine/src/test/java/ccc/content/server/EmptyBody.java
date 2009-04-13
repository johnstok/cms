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


/**
 * An empty body.
 *
 * @author Civic Computing Ltd.
 */
public class EmptyBody
    implements
        Body {

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset) throws IOException {
        // No Op
    }

}
