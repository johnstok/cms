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
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.Script;
import ccc.plugins.scripting.TextProcessor;


/**
 * Helper class for executing scripts.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptRunner implements TextProcessor {

    /** OUT : String. */
    public static final String OUT = "ccc.scriptrunner.out";
    /** ERR : String. */
    public static final String ERR = "ccc.scriptrunner.err";
    /** IN : String. */
    public static final String IN = "ccc.scriptrunner.in";

    private ArrayList<String> _allowedClasses = null;


    /**
     * Evaluate a string as Javascript.
     *
     * @param script The script to evaluate.
     * @param context The script context.
     * @param out The writer the script will write to.
     */
    public void eval(final Script script,
                     final Context context,
                     final Writer out) {

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
    public String render(final Script template, final Context context) {
        final StringWriter renderedOutput = new StringWriter();
        render(template, renderedOutput, context);
        return renderedOutput.toString();
    }


    /** {@inheritDoc} */
    @Override
    public void render(final Script template,
                       final Writer output,
                       final Context context) {
        eval(template, context, output);
    }


    /** {@inheritDoc} */
    @Override
    public void setWhitelist(final List<String> allowedClasses) {
        _allowedClasses = new ArrayList<String>(allowedClasses);
    }
}
