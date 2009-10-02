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
package ccc.rendering;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;



/**
 * A text processor implementation that treats the template as javascript.
 *
 * @author Civic Computing Ltd.
 */
public class JavascriptProcessor
    implements
        TextProcessor {
    private static final Logger LOG =
        Logger.getLogger(JavascriptProcessor.class);


    /** {@inheritDoc} */
    @Override
    public String render(final String template, final Context context) {
        final StringWriter renderedOutput = new StringWriter();
        render(template, renderedOutput, context);
        return renderedOutput.toString();
    }


    /** {@inheritDoc} */
    @Override
    public void render(final String template,
                       final Writer output,
                       final Context context) {
        try {
            final ScriptEngineManager factory = new ScriptEngineManager();
            final ScriptEngine engine = factory.getEngineByName("JavaScript");
            engine.getContext().setWriter(output);

            for (final Map.Entry<String, Object> extra
                : context.getExtras().entrySet()) {
                engine.put(extra.getKey(), extra.getValue());
            }
            engine.put("resource", context.getResource());
            engine.put("params", context.getParams());
            engine.put("reader", context.getReader());
            engine.put("out", output);

            engine.eval(template);

            output.flush();

        } catch (final Exception e) {
            handleException(output, e);
        }
    }


    private void handleException(final Writer output, final Exception e) {
        final String msg = ""+e.getMessage(); // getMessage() is NULL for an NPE
        LOG.warn("Error in template: "+msg);

        try {
            output.write(msg);
        } catch (final IOException e1) {
            LOG.warn("Error writing to servlet response.", e1);
        }
    }
}
