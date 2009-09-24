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

import ccc.domain.User;



/**
 * A response body.
 *
 * @author Civic Computing Ltd.
 */
public interface Body {

    /**
     * Write the body to an {@link OutputStream}.
     *
     * @param os The stream to which the body will be written.
     * @param charset The character set for the output stream.
     * @param processor A text processor for generating markup, etc.
     * @param user The user reading the data.
     *
     * @throws IOException - if writing to the output stream fails.
     */
    void write(OutputStream os,
               Charset charset,
               User user,
               TextProcessor processor) throws IOException;
}
