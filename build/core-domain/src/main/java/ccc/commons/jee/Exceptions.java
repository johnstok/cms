/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.commons.jee;

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

}
