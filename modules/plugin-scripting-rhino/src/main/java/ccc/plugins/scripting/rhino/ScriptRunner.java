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
package ccc.plugins.scripting.rhino;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import ccc.plugins.scripting.AbstractTextProcessor;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.ProcessingException;
import ccc.plugins.scripting.Script;


/**
 * Helper class for executing scripts.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptRunner
    extends
        AbstractTextProcessor {

    /** OUT : String. */
    static final String OUT = "ccc.scriptrunner.out";
    /** ERR : String. */
    static final String ERR = "ccc.scriptrunner.err";
    /** IN : String. */
    static final String IN = "ccc.scriptrunner.in";

    private ArrayList<String> _allowedClasses = null;


    /** {@inheritDoc} */
    @Override
    public void render(final Script script,
                       final Writer out,
                       final Context context) throws ProcessingException {

        final org.mozilla.javascript.Context cx =
            org.mozilla.javascript.Context.enter();

        try {
            final ScriptableObject scope = cx.initStandardObjects();
            addGlobalFunction(scope, "print");
            addContextToScope(scope, context);
            cx.putThreadLocal(OUT, new PrintWriter(out));

            if (null!=_allowedClasses) {
                cx.setClassShutter(new CccClassShutter(_allowedClasses));
            }

            cx.evaluateReader(
                scope,
                new StringReader(script.getBody()),
                script.getTitle(),
                1,
                null);

        } catch (final IOException e) {
            throw new RuntimeException("Error invoking script.", e);
        } catch (final RhinoException e) {
            handleException(
                e,
                "Rhino",
                script.getTitle(),
                e.lineNumber(),
                e.columnNumber());
        } finally {
            org.mozilla.javascript.Context.exit();
        }
    }


    private void addGlobalFunction(final ScriptableObject scope,
                                   final String... functionNames) {
            scope.defineFunctionProperties(
                functionNames,
                RhinoGlobalFunctions.class,
                ScriptableObject.DONTENUM);
    }


    private void addContextToScope(final Scriptable scope,
                                   final Context context) {
        for (final Map.Entry<String, Object> entry
                : context.getAll().entrySet()) {
            addContextValue(scope, entry.getKey(), entry.getValue());
        }
    }


    private void addContextValue(final Scriptable scope,
                                 final String key,
                                 final Object value) {
        final Object jsValue =
            org.mozilla.javascript.Context.javaToJS(value, scope);
        ScriptableObject.putProperty(scope, key, jsValue);
    }


    /** {@inheritDoc} */
    @Override
    public void setWhitelist(final List<String> allowedClasses) {
        _allowedClasses = new ArrayList<String>(allowedClasses);
    }
}
