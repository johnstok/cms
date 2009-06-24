/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import ccc.api.CommandFailedException;
import ccc.api.Commands;
import ccc.api.ID;
import ccc.api.MimeType;
import ccc.api.ResourceSummary;
import ccc.api.TemplateDelta;


/**
 * Migrate templates from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateMigration {
    private static Logger log = Logger.getLogger(Migrations.class);

    private final Commands _commands;
    private final Map<String, ResourceSummary> _templates =
        new HashMap<String, ResourceSummary>();

    /**
     * Constructor.
     *
     * @param commands The command API for the new system.
     */
    public TemplateMigration(final Commands commands) {
        _commands = commands;
    }


    private void createTemplate(final String templateName,
                                final ResourceSummary templateFolder) {
        final TemplateDelta t =
            new TemplateDelta(
                "Empty template!",
                "<fields/>",
                MimeType.HTML);

        try {
            final ResourceSummary ts =
                _commands.createTemplate(
                    templateFolder.getId(),
                    t,
                    templateName,
                    "No description.",
                    templateName);

            _templates.put(templateName, ts);
        } catch (final CommandFailedException e) {
            log.error("Failed to create template: "+templateName, e);
        }
    }


    /**
     * Lookup the template for a template name.
     *
     * @param templateName The name.
     * @param templateFolder The folder in which templates should be created.
     * @return The corresponding template, or null;
     */
    public ID getTemplate(final String templateName,
                          final ResourceSummary templateFolder) {
        if (!_templates.containsKey(templateName)) { // Not yet migrated
            createTemplate(templateName, templateFolder);
        }
        final ResourceSummary template = _templates.get(templateName);
        return (null==template) ? null : template.getId();
    }
}
