/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.migration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;

import ccc.rest.Templates;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.rest.dto.TemplateSummary;
import ccc.rest.exceptions.RestException;
import ccc.types.MimeType;
import ccc.types.ResourceName;


/**
 * Migrate templates from CCC6.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateMigration {
    private static Logger log = Logger.getLogger(Migrations.class);

    private final LegacyDBQueries _legacyQueries;
    private final Templates       _templateApi;


    /**
     * Constructor.
     *
     * @param legacyQueries The query API for CCC6.
     * @param templates Templates API implementation.
     */
    public TemplateMigration(final LegacyDBQueries legacyQueries,
                             final Templates templates) {
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
            _templateApi.createTemplate(
                new TemplateDto(
                    templateFolder.getId(),
                    t,
                    templateName,
                    templateDescription,
                    templateName));

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
    public UUID getTemplate(final ResourceName templateName,
                            final String templateDescription,
                            final ResourceSummary templateFolder) {

        final Set<TemplateSummary> templates =
            new HashSet<TemplateSummary>(_templateApi.templates());

        TemplateSummary template = null;
        for (final TemplateSummary ts : templates) {
            if (ts.getName().equals(templateName)) {
                template = ts;
            }
        }

        if (null==template) { // Not yet migrated or does not exists
            createTemplate(
                templateName.toString(), templateDescription, templateFolder);
        }
        return (null==template) ? null : template.getId();
    }
}
