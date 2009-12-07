/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.commons;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Helper methods when dealing with exceptions.
 *
 * @author Civic Computing Ltd
 */
public final class Exceptions {

    private Exceptions() { /* NO-OP */ }

    /**
     * Convert the stack trace for an exception to a string.
     *
     * @param t The exception for which we want the stack trace.
     * @return The stack trace as a string.
     */
    public static String stackTraceFor(final Throwable t) {

        final StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }

    /**
     * Returns the root cause of this exception.
     * If the incoming Throwable instance is not null, there is always
     * at least one root, the argument itself.
     *
     * @param t The exception to be investigated.
     * @return Root cause of the exception.
     */
    public static Throwable rootCause(final Throwable t) {
        Throwable result = t;
        if (result != null) {
            for (Throwable cause = result.getCause();
                 cause != null;
                 cause = result.getCause()) {
                result = cause;
            }
        }
        return result;
    }

    /**
     * Handle an exception that we can do nothing sensible with.
     *
     * @param t The exception to ignore.
     */
    public static void swallow(@SuppressWarnings("unused") final Throwable t) {
        /* NO-OP */
    }
}
