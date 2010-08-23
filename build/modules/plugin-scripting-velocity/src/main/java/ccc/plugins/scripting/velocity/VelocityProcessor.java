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
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.app.event.MethodExceptionEventHandler;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import ccc.api.core.ServiceLocator;
import ccc.api.exceptions.EntityNotFoundException;
import ccc.api.types.DBC;
import ccc.plugins.scripting.AbstractTextProcessor;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.ProcessingException;
import ccc.plugins.scripting.Script;


/**
 * Renders a resource as text using the velocity library.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessor
    extends
        AbstractTextProcessor {


    /** {@inheritDoc} */
    public void render(final Script template,
                       final Writer output,
                       final Context ctxt) throws ProcessingException {

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
            "ccc.plugins.scripting.velocity.Template");
        velocityProperties.setProperty(
            "runtime.introspector.uberspect",
            "org.apache.velocity.util.introspection.SecureUberspector");
        velocityProperties.setProperty(
            "eventhandler.methodexception.class",
            "ccc.plugins.scripting.velocity.VelocityProcessor"
                + "$EntityNotFoundHandler");

        try {
            final VelocityContext context = new VelocityContext();

            for (final Map.Entry<String, Object> extra
                                                : ctxt.getAll().entrySet()) {
                context.put(extra.getKey(), extra.getValue());
            }

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
            handleException(
                e,
                "Velocity",
                template.getTitle(),
                e.getLineNumber(),
                e.getColumnNumber());
        } catch (final MethodInvocationException e) {
            handleException(
                e,
                "Velocity",
                template.getTitle(),
                e.getLineNumber(),
                e.getColumnNumber());
        } catch (final ResourceNotFoundException e) {
            handleException(e, "Velocity", template.getTitle(), -1, -1);
        } catch (final IOException e) {
            handleException(e, "Velocity", template.getTitle(), -1, -1);
        } catch (final Exception e) {
            handleException(e, "Velocity", template.getTitle(), -1, -1);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void setWhitelist(final List<String> allowedClasses) {
        throw new UnsupportedOperationException(
            "Whitelists not supported by Velocity plugin.");
    }


    /**
     * Velocity event handler to convert EntityNotFoundExceptions to NULL.
     *
     * @author Civic Computing Ltd.
     */
    public static class EntityNotFoundHandler
        implements
            MethodExceptionEventHandler {

        private static final Logger LOG =
            Logger.getLogger(EntityNotFoundHandler.class);

        /** {@inheritDoc} */
        @SuppressWarnings("unchecked") // Interface is not generic.
        @Override
        public Object methodException(final Class clazz,
                                      final String methodName,
                                      final Exception ex) throws Exception {
            if (ex instanceof EntityNotFoundException) {
                LOG.warn("Converted EntityNotFoundException to NULL.");
                return null;
            }
            throw ex;
        }
    }
}
