/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;
import ccc.types.DBC;
import ccc.types.MimeType;
import ccc.types.ResourceName;
import ccc.types.ResourceType;


/**
 * A template is used to define how a resource will be displayed.
 *
 * @author Civic Computing Ltd
 */
public class Template
    extends
        HistoricalResource<TemplateDelta, TemplateRevision> {

    /** Constructor: for persistence only. */
    protected Template() { super(); }

    /**
     * Constructor.
     *
     * @param title The title of the template.
     * @param description The description for the template.
     * @param body A valid velocity template for rendering a page.
     * @param definiton An xml definition of the fields that the body requires.
     * @param mimeType The mime type this template will produce.
     * @param metadata The metadata describing the initial revision.
     */
    public Template(final String title,
                    final String description,
                    final String body,
                    final String definiton,
                    final MimeType mimeType,
                    final RevisionMetadata metadata) {

        this(
            ResourceName.escape(title),
            title,
            description,
            body,
            definiton,
            mimeType,
            metadata);
    }

    /**
     * Constructor.
     *
     * @param name The name of the template.
     * @param title The title of the template.
     * @param description The description for the template.
     * @param body A valid velocity template for rendering a page.
     * @param definiton An xml definition of the fields that the body requires.
     * @param mimeType The mime type this template will produce.
     * @param metadata The metadata of the template.
     */
    public Template(final ResourceName name,
                    final String title,
                    final String description,
                    final String body,
                    final String definiton,
                    final MimeType mimeType,
                    final RevisionMetadata metadata) {

        super(name, title);
        DBC.require().notNull(description);

        setDescription(description);
        update(
            new TemplateDelta(body, definiton, mimeType),
            metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceType getType() {
        return ResourceType.TEMPLATE;
    }

    /**
     * Accessor for the template's body.
     *
     * @return The body as a string.
     */
    public String getBody() {
        return currentRevision().getBody();
    }

    /**
     * Accessor for the template's definition.
     *
     * @return The definition as a String.
     */
    public String getDefinition() {
        return currentRevision().getDefinition();
    }

    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public MimeType getMimeType() {
        return currentRevision().getMimeType();
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta createSnapshot() {
        return new TemplateDelta(
            getBody(),
            getDefinition(),
            getMimeType());
    }


    /**
     * Update the contents of this template.
     *
     * @param delta The new content for the template.
     * @param metadata The metadata describing this revision.
     */
    public void update(final TemplateDelta delta,
                       final RevisionMetadata metadata) {
        addRevision(
            new TemplateRevision(
                metadata.getTimestamp(),
                metadata.getActor(),
                metadata.isMajorChange(),
                metadata.getComment(),
                delta.getBody(),
                delta.getDefinition(),
                delta.getMimeType()));
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary forCurrentRevision() {
        return summarize();
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary forSpecificRevision(final int revNo) {
        // TODO: Return correct revision.
        return summarize();
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary forWorkingCopy() {
        // TODO: Return working copy.
        return summarize();
    }


    /**
     * Create a summary for a template.
     *
     * @return The corresponding summary.
     */
    public TemplateSummary summarize() {
        final TemplateSummary dto =
            new TemplateSummary(
                getId(),
                getName(),
                getTitle(),
                getDescription(),
                getBody(),
                getDefinition());
        setDtoProps(dto);
        dto.setRevision(currentRevisionNo());
        return dto;
    }


    /**
     * Create a delta for a template.
     *
     * @return A corresponding delta.
     */
    public TemplateDelta deltaTemplate() {
        final TemplateDelta delta =
            new TemplateDelta(
                getBody(),
                getDefinition(),
                getMimeType());
        return delta;
    }


    /**
     * Create summaries for a collection of templates.
     *
     * @param templates The templates.
     * @return The corresponding summaries.
     */
    public static Collection<TemplateSummary> mapTemplates(
                                               final List<Template> templates) {
        final Collection<TemplateSummary> mapped =
            new ArrayList<TemplateSummary>();
        for (final Template t : templates) {
            mapped.add(t.summarize()); }
        return mapped;
    }


    /**
     * Create deltas for a collection of templates.
     *
     * @param templates The templates.
     * @return The corresponding deltas.
     */
    protected Collection<TemplateDelta> deltaTemplates(
                                               final List<Template> templates) {
        final Collection<TemplateDelta> mapped = new ArrayList<TemplateDelta>();
        for (final Template t : templates) {
            mapped.add(t.deltaTemplate());
        }
        return mapped;
    }
}
