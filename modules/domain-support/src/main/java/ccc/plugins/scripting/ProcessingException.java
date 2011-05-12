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


/**
 * Exception thrown when script processing fails.
 *
 * @author Civic Computing Ltd.
 */
public class ProcessingException
    extends
        Exception {


    /**
     * Constructor.
     *
     * @param engine The name of the text processing engine.
     * @param title  The title of the script.
     * @param lineNo The line of the script that failed.
     * @param colNo  The column the line that that failed.
     * @param cause  The cause of the failure.
     */
    public ProcessingException(final String engine,
                               final String title,
                               final int lineNo,
                               final int colNo,
                               final Throwable cause) {
        super(
            "Error processing "+engine
            +" script '"+title
            +"' [line number "+lineNo
            +", column number "+colNo
            +"].",
            cause);
    }
}
