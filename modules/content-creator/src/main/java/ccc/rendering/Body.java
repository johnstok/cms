/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.rendering;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import ccc.commons.Context;
import ccc.commons.TextProcessor;



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
     * @param context The template context.
     *
     * @throws IOException - if writing to the output stream fails.
     */
    void write(OutputStream os,
               Charset charset,
               Context context,
               TextProcessor processor) throws IOException;
}
