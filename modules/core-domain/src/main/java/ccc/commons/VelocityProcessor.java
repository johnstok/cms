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
package ccc.commons;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import ccc.domain.CCCException;
import ccc.domain.Resource;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessor {
    /**
     * Render a resource with the specified template.
     *
     * @param resource The resource that will be rendered.
     * @param template The template used to render the resource.
     * @return The html rendering as a string.
     */
    public String render(final Resource resource, final String template) {

        final StringWriter html = new StringWriter();

        final Properties velocityProperties = new Properties();
        velocityProperties.setProperty(
            RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
            "org.apache.velocity.runtime.log.Log4JLogChute");
        velocityProperties.setProperty(
            "runtime.log.logsystem.log4j.logger",
            getClass().getName());

        try {
            final VelocityEngine ve = new VelocityEngine(velocityProperties);
            ve.init();
            final VelocityContext context = new VelocityContext();
            context.put("resource", resource);

            ve.evaluate(context, html, "????", template);

        } catch (final ParseErrorException e) {
            throw new CCCException(e);
        } catch (final MethodInvocationException e) {
            throw new CCCException(e);
        } catch (final ResourceNotFoundException e) {
            throw new CCCException(e);
        } catch (final IOException e) {
            throw new CCCException(e);
        } catch (final Exception e) {
            throw new CCCException(e);
        }

        return html.toString();
    }
}
