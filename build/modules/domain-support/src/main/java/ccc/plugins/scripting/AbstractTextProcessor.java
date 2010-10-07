/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.plugins.scripting;

import java.io.StringWriter;



/**
 * Abstract base class for implementing text processors.
 *
 * @author Civic Computing Ltd.
 */
public abstract class AbstractTextProcessor
    implements
        TextProcessor {


    /** {@inheritDoc} */
    public final String render(final Script template,
                               final Context context)
                                                    throws ProcessingException {
        final StringWriter renderedOutput = new StringWriter();
        render(template, renderedOutput, context);
        return renderedOutput.toString();
    }


    /**
     * Handle a processor exception.
     *
     * @param e      The engine-specific exception.
     * @param engine The name of the text processing engine.
     * @param title  The title of the script.
     * @param lineNo The line that caused the exception.
     * @param colNo  The column that caused the exception.
     *
     * @throws ProcessingException A processing exception wrapping the
     *  engine-specific exception.
     */
    protected final void handleException(final Exception e,
                                         final String engine,
                                         final String title,
                                         final int lineNo,
                                         final int colNo)
                                                    throws ProcessingException {
        throw new ProcessingException(title, lineNo, e);
    }
}
