/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev: 1072 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-04-13 10:29:52 +0100 (Mon, 13 Apr 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.content.response;

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
