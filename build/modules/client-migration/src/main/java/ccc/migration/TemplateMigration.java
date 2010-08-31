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

import ccc.api.core.Resource;
import ccc.api.core.Template;
import ccc.api.core.Templates;
import ccc.api.exceptions.CCException;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


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
                                final Resource templateFolder) {

        final StringBuilder sb = new StringBuilder("<fields>\n");

        final Set<String> fields =
            _legacyQueries.selectTemplateFields(templateName);
        for(final String field : fields) {
            sb.append("    <field name=\""+field+"\" type=\"text_area\"/>\n");
        }
        sb.append("</fields>");

        final Template t = new Template();
        t.setName(ResourceName.escape(templateName));
        t.setParent(templateFolder.getId());
        t.setDescription(templateDescription);
        t.setTitle(templateName);
        t.setBody("Empty template!");
        t.setDefinition(sb.toString());
        t.setMimeType(MimeType.HTML);

        try {
            _templateApi.create(t);
        } catch (final CCException e) {
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
                            final Resource templateFolder) {

        final Set<Template> templates =
            new HashSet<Template>(_templateApi.query(1, 999).getElements());

        Template template = null;
        for (final Template ts : templates) {
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
