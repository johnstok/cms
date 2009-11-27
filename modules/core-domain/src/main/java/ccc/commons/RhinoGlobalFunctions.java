/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.commons;

import java.io.IOException;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;


/**
 * Functions available in the Rhino Javascript context.
 *
 * @author Civic Computing Ltd.
 */
public final class RhinoGlobalFunctions {

    private static final Logger LOG =
        Logger.getLogger(RhinoGlobalFunctions.class);

    private RhinoGlobalFunctions() { /* NO OP */ }

    /**
     * Print to a context's OUT parameter.
     *
     * @param cx The Rhino context.
     * @param thisObj The 'this' object for the function.
     * @param args The arguments to the function.
     * @param funObj The Rhino representation of the function.
     */
    public static void print(
                         final org.mozilla.javascript.Context cx,
                         @SuppressWarnings("unused") final Scriptable thisObj,
                         final Object[] args,
                         @SuppressWarnings("unused") final Function funObj) {
        final Writer out = ((Writer) cx.getThreadLocal(ScriptRunner.OUT));
        try {
            for (final Object arg : args) {
                if (arg instanceof NativeJavaObject) {
                    final NativeJavaObject jObj = (NativeJavaObject) arg;
                    out.write(jObj.unwrap().toString());
                } else {
                    out.write(arg.toString());
                }
            }
            out.flush();
        } catch (final IOException e) {
            LOG.warn("Error printing from script.", e);
        }
    }
}
