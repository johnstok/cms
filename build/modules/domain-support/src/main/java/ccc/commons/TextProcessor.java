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
 * Revision      $Rev: 2564 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-03-22 14:09:24 +0000 (Mon, 22 Mar 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.commons;

import java.io.Writer;



/**
 * API for text processing.
 *
 * @author Civic Computing Ltd.
 */
public interface TextProcessor {

    /**
     * Render a resource with the specified template.
     * <br/><br/>
     * The rendered output will be stored in memory as a String. Therefore,
     * caution should be taken that this method is not used to generate large
     * output, otherwise an {@link OutOfMemoryError} could be thrown.
     *
     * @param template The template used to render the resource.
     * @param context Additional values that are passed to the template.
     *
     * @return The html rendering as a string.
     */
    String render(final Script template,
                  final Context context);

    /**
     * Render a resource with the specified template.
     * <br/><br/>
     * The rendered output will be written to the specified writer.
     *
     * @param template The template used to render the resource.
     * @param output A valid {@link Writer}. The writer will be flushed when
     *  output is complete. The writer will not be closed.
     * @param context Additional values that are passed to the template.
     */
    void render(final Script template,
                final Writer output,
                final Context context);

}
