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
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.RuntimeConstants;

import ccc.domain.CCCException;
import ccc.domain.Folder;
import ccc.domain.Resource;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityProcessor {

    /*
     * TODO: investigate all relevant properties
     * TODO: handling missing expressions...
     * resource.manager.logwhenfound = true
     * input.encoding = UTF-8
     * velocimacro.library = ccc.vm
     * velocimacro.arguments.strict = true
     * velocimacro.permissions.allow.inline.to.replace.global = false
     * velocimacro.permissions.allow.inline = true
     * velocimacro.library.autoreload = false
     */
    private static final String VELOCITY_CONFIG =
        "resource.loader = classpath\n"
        + "classpath.resource.loader.class = "
            + "org.apache.velocity.runtime.resource.loader."
            + "ClasspathResourceLoader\n"
        + "classpath.resource.loader.description = Classpath resource loader\n"
        + "classpath.resource.loader.cache = true\n"
        + "classpath.resource.loader.modificationCheckInterval = -1\n"
        + "velocimacro.library = ccc.vm\n";


    /**
     * Render a resource with the specified template.
     *
     * @param resource The resource that will be rendered.
     * @param template The template used to render the resource.
     * @param root The root folder in which the resource is contained.
     * @return The html rendering as a string.
     */
    public String render(final Resource resource,
                         final Folder root,
                         final String template) {

        final StringWriter renderedOutput = new StringWriter();

        final Properties velocityProperties = new Properties();
        try {
            velocityProperties.load(new StringReader(VELOCITY_CONFIG));
        } catch (final IOException e1) {
            throw new RuntimeException(e1);
        }
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
            context.put("root", root);

            ve.evaluate(context, renderedOutput, "??", template); // What is ??

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

        return renderedOutput.toString();
    }
}
