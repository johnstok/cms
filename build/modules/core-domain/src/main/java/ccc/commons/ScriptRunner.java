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

import java.io.Writer;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 * Helper class for executing scripts.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptRunner {

    /**
     * Evaluate a string as Javascript.
     *
     * @param script The script to evaluate.
     * @param context The script context.
     * @param out The writer the script will write to.
     *
     * @throws ScriptException If execution of the script fails.
     */
    public void eval(final String script,
                      final Map<String, Object> context,
                      final Writer out) throws ScriptException {

        final ScriptEngineManager factory = new ScriptEngineManager();
        final ScriptEngine engine = factory.getEngineByName("JavaScript");
        engine.getContext().setWriter(out);

        for (final Map.Entry<String, Object> entry : context.entrySet()) {
            engine.put(entry.getKey(), entry.getValue());
        }

        engine.eval(script);
    }
}
