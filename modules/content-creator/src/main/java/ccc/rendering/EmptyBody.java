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
package ccc.rendering;

import java.io.OutputStream;
import java.nio.charset.Charset;

import ccc.commons.Context;




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
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) {
        // No Op
    }

}
