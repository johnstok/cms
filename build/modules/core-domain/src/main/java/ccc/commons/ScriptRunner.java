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
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;


/**
 * Helper class for executing scripts.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptRunner {

    /** OUT : String. */
    public static final String OUT = "ccc.scriptrunner.out";
    /** ERR : String. */
    public static final String ERR = "ccc.scriptrunner.err";
    /** IN : String. */
    public static final String IN = "ccc.scriptrunner.in";

    private final ArrayList<String> _allowedClasses;


    /**
     * Constructor.
     *
     * @param allowedClasses Class white-list for the script environment.
     */
    public ScriptRunner(final List<String> allowedClasses) {
        _allowedClasses = new ArrayList<String>(allowedClasses);
    }


    /**
     * Constructor.
     */
    public ScriptRunner() {
        _allowedClasses = null;
    }


    /**
     * Evaluate a string as Javascript.
     *
     * @param script The script to evaluate.
     * @param context The script context.
     * @param out The writer the script will write to.
     */
    public void eval(final String script,
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

            cx.evaluateReader(scope, new StringReader(script), "ccc", 1, null);

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
}
