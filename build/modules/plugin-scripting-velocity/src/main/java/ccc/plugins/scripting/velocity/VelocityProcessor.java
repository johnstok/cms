/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.scripting.velocity;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.SortTool;

import ccc.api.ServiceLocator;
import ccc.api.types.DBC;
import ccc.plugins.markup.XHTML;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.Script;
import ccc.plugins.scripting.TextProcessor;


/**
 * Renders a resource as text using the velocity library.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessor implements TextProcessor {

    private static final String HOSTNAME = getHostname();

    /**
     * Velocity tools for helping with Java5 enum's.
     *
     * @author Civic Computing Ltd.
     */
    public static final class EnumTools {

        /**
         * Retrieve an enum constant.
         *
         * @param className The enum's class.
         * @param value The enum's name.
         *
         * @return The corresponding enum.
         */
        @SuppressWarnings("unchecked")
        public Object of(final String className, final String value) {
            try {
                return
                    Enum.valueOf(
                        (Class<Enum>) Class.forName(className), value);
            } catch (final ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static final Logger LOG = Logger.getLogger(VelocityProcessor.class);


    /*
     * resource.manager.logwhenfound = true
     * velocimacro.arguments.strict = true
     * velocimacro.permissions.allow.inline.to.replace.global = false
     * velocimacro.permissions.allow.inline = true
     * velocimacro.library.autoreload = false
     */


    private final Random _random = new Random();


    /** {@inheritDoc} */
    public String render(final Script template,
                         final Context context) {
        final StringWriter renderedOutput = new StringWriter();
        render(template, renderedOutput, context);
        return renderedOutput.toString();
    }


    /** {@inheritDoc} */
    public void render(final Script template,
                       final Writer output,
                       final Context ctxt) {

        DBC.require().notNull(template);
        DBC.require().notNull(output);
        DBC.require().notNull(ctxt);

        final Properties velocityProperties = new Properties();
        velocityProperties.setProperty(
            "input.encoding",
            "UTF-8");
        velocityProperties.setProperty(
            "resource.loader",
            "ccc");
        velocityProperties.setProperty(
            "ccc.resource.loader.class",
            CCCResourceLoader.class.getName());
        velocityProperties.setProperty(
            "ccc.resource.loader.description",
            "CCC resource loader");
        velocityProperties.setProperty(
            "ccc.resource.loader.cache",
            "true");
        velocityProperties.setProperty(
            "ccc.resource.loader.modificationCheckInterval",
            "-1");
        velocityProperties.setProperty(
            RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityProperties.setProperty(
            "runtime.log.logsystem.log4j.logger",
            "ccc.rendering.velocity.Template");
        velocityProperties.setProperty(
            "runtime.introspector.uberspect",
            "org.apache.velocity.util.introspection.SecureUberspector");

        try {
            final VelocityContext context = new VelocityContext();

            for (final Map.Entry<String, Object> extra
                                                : ctxt.getAll().entrySet()) {
                context.put(extra.getKey(), extra.getValue());
            }
            context.put("random", _random);
            context.put("math", Math.class);
            context.put("collections", Collections.class);
            context.put("calendar", Calendar.class);
            context.put("html", XHTML.class);
            context.put("uuid", UUID.class);
            context.put("enums", new EnumTools());
            context.put("hostname", HOSTNAME);
            context.put("dateTool", new DateTool());
            context.put("sortTool", new SortTool());

            final VelocityEngine ve = new VelocityEngine(velocityProperties);
            final ServiceLocator sl = (ServiceLocator) context.get("services");
            ve.setApplicationAttribute("ccc-reader", sl.getResources());
            ve.init();

            ve.evaluate(
                context,
                output,
                template.getTitle(),
                template.getBody());

            output.flush();

        } catch (final ParseErrorException e) {
            handleException(e, template.getTitle());
        } catch (final MethodInvocationException e) {
            handleException(e, template.getTitle());
        } catch (final ResourceNotFoundException e) {
            handleException(e, template.getTitle());
        } catch (final IOException e) {
            handleException(e, template.getTitle());
        } catch (final Exception e) {
            handleException(e, template.getTitle());
        }
    }

    private void handleException(final Exception e, final String title) {
//        LOG.warn("Error in template '"+title+"': "+e.getMessage());
        throw new RuntimeException("Error in template '"+title+"'.", e);
    }

    private static String getHostname() {
        try {
            final InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (final UnknownHostException e) {
            LOG.warn("Failed to determine host address.", e);
            return "<unknown>";
        }
    }


    /** {@inheritDoc} */
    @Override
    public void setWhitelist(final List<String> allowedClasses) {
        throw new UnsupportedOperationException(
            "Whitelists not supported by Velocity plugin.");
    }
}
