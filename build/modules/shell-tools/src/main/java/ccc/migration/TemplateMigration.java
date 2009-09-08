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
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.MimeType;


/**
 * Migrate templates from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateMigration {
    private static Logger log = Logger.getLogger(Migrations.class);

    private final LegacyDBQueries _legacyQueries;
    private final ResourcesExt    _resourcesExt;
    private final Templates       _templateApi;

    private final Map<String, ResourceSummary> _templates =
        new HashMap<String, ResourceSummary>();


    /**
     * Constructor.
     *
     * @param legacyQueries The query API for CCC6.
     * @param resourcesExt The command API for the new system.
     */
    public TemplateMigration(final LegacyDBQueries legacyQueries,
                             final ResourcesExt resourcesExt,
                             final Templates templates) {
        _resourcesExt = resourcesExt;
        _legacyQueries = legacyQueries;
        _templateApi = templates;
    }


    private void createTemplate(final String templateName,
                                final String templateDescription,
                                final ResourceSummary templateFolder) {

        final StringBuilder sb = new StringBuilder("<fields>\n");

        final Set<String> fields =
            _legacyQueries.selectTemplateFields(templateName);
        for(final String field : fields) {
            sb.append("    <field name=\""+field+"\" type=\"text_area\"/>\n");
        }
        sb.append("</fields>");

        final TemplateDelta t =
            new TemplateDelta(
                "Empty template!",
                sb.toString(),
                MimeType.HTML);

        try {
            final ResourceSummary ts =
                _templateApi.createTemplate(
                    new TemplateDto(
                        templateFolder.getId(),
                        t,
                        templateName,
                        templateDescription,
                        templateName));

            _templates.put(templateName, ts);
        } catch (final RestException e) {
            log.error("Failed to create template: "+templateName, e);
        }
    }


    /**
     * Lookup the template for a template name.
     *
     * @param templateName The name.
     * @param templateDescription The description.
     * @param templateFolder The folder in which templates should be created.
     * @return The corresponding template, or null;
     */
    public UUID getTemplate(final String templateName,
                          final String templateDescription,
                          final ResourceSummary templateFolder) {
        if (!_templates.containsKey(templateName)) { // Not yet migrated
            createTemplate(templateName, templateDescription, templateFolder);
        }
        final ResourceSummary template = _templates.get(templateName);
        return (null==template) ? null : template.getId();
    }
}
