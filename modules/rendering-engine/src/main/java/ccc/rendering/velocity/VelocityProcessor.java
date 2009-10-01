/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering.velocity;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import ccc.commons.XHTML;
import ccc.domain.CCCException;
import ccc.rendering.Context;
import ccc.rendering.TextProcessor;


/**
 * Renders a resource as text using the velocity library.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessor implements TextProcessor {
    private static final Logger LOG = Logger.getLogger(VelocityProcessor.class);

    /*
     * resource.manager.logwhenfound = true
     * input.encoding = UTF-8
     * velocimacro.arguments.strict = true
     * velocimacro.permissions.allow.inline.to.replace.global = false
     * velocimacro.permissions.allow.inline = true
     * velocimacro.library.autoreload = false
     */
    private static final String VELOCITY_CONFIG =
        "input.encoding = UTF-8\n"
        + "resource.loader = ccc\n"
        + "ccc.resource.loader.class = "
            + CCCResourceLoader.class.getName() + "\n"
        + "ccc.resource.loader.description = CCC resource loader\n"
        + "ccc.resource.loader.cache = true\n"
        + "ccc.resource.loader.modificationCheckInterval = -1\n";

    private final Random _random = new Random();


    /** {@inheritDoc} */
    public String render(final String template,
                         final Context context) {
        final StringWriter renderedOutput = new StringWriter();
        render(template, renderedOutput, context);
        return renderedOutput.toString();
    }


    /** {@inheritDoc} */
    public void render(final String template,
                       final Writer output,
                       final Context ctxt) {

        final Properties velocityProperties = new Properties();
        try {
            velocityProperties.load(new StringReader(VELOCITY_CONFIG));
        } catch (final IOException e1) {
            throw new CCCException(e1);
        }
        velocityProperties.setProperty(
            RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityProperties.setProperty(
            "runtime.log.logsystem.log4j.logger",
            getClass().getName());
        velocityProperties.setProperty("runtime.introspector.uberspect",
            "org.apache.velocity.util.introspection.SecureUberspector");
        try {
            final VelocityContext context = new VelocityContext();

            for (final Map.Entry<String, Object> extra
                                                : ctxt.getExtras().entrySet()) {
                context.put(extra.getKey(), extra.getValue());
            }
            context.put("reader", ctxt.getReader());
            context.put("resource", ctxt.getResource());
            context.put("parameters", ctxt.getParams());
            context.put("random", _random);
            context.put("math", Math.class);
            context.put("calendar", Calendar.class);
            context.put("html", XHTML.class);

            final VelocityEngine ve = new VelocityEngine(velocityProperties);
            ve.setApplicationAttribute("ccc-reader", ctxt.getReader());
            ve.init();

            ve.evaluate(context, output, "VelocityProcessor", template);

        } catch (final ParseErrorException e) {
            handleException(output, e);
        } catch (final MethodInvocationException e) {
            handleException(output, e);
        } catch (final ResourceNotFoundException e) {
            handleException(output, e);
        } catch (final IOException e) {
            handleException(output, e);
        } catch (final Exception e) {
            handleException(output, e);
        } finally {
            try {
                output.flush();
            } catch (final IOException e) {
                LOG.warn("Error flushing servlet response.", e);
            }
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
