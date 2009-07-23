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
    String render(final String template,
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
    void render(final String template,
                final Writer output,
                final Context context);

}
