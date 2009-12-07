/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
