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

import ccc.api.core.Template;
import ccc.api.types.DBC;
import ccc.api.types.Link;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.commons.NormalisingEncoder;


/**
 * A template is used to define how a resource will be displayed.
 *
 * @author Civic Computing Ltd
 */
public class TemplateEntity
    extends
        HistoricalResource<Template, TemplateRevision> {

    /** Constructor: for persistence only. */
    protected TemplateEntity() { super(); }

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
    public TemplateEntity(final String title,
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
    public TemplateEntity(final ResourceName name,
                    final String title,
                    final String description,
                    final String body,
                    final String definiton,
                    final MimeType mimeType,
                    final RevisionMetadata metadata) {

        super(name, title);
        DBC.require().notNull(description);

        setDescription(description);

        final Template t = new Template();
        t.setBody(body);
        t.setDefinition(definiton);
        t.setMimeType(new MimeType(mimeType.getPrimaryType(),
            mimeType.getSubType()));

        update(t, metadata);
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


    /**
     * Update the contents of this template.
     *
     * @param delta The new content for the template.
     * @param metadata The metadata describing this revision.
     */
    public void update(final Template delta,
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
    public Template forCurrentRevision() {
        return summarize();
    }

    /** {@inheritDoc} */
    @Override
    public Template forSpecificRevision(final int revNo) {
        // TODO: Return correct revision.
        return summarize();
    }

    /** {@inheritDoc} */
    @Override
    public Template forWorkingCopy() {
        // TODO: Return working copy.
        return summarize();
    }


    /**
     * Create a summary for a template.
     *
     * @return The corresponding summary.
     */
    public Template summarize() {
        final Template dto =
            Template.summary(
                getId(),
                getName(),
                getTitle(),
                getDescription(),
                getBody(),
                getDefinition());
        setDtoProps(dto);
        dto.setMimeType(getMimeType());
        dto.setRevision(currentRevisionNo());

        dto.addLink(
            Template.SELF,
            new Link(ccc.api.core.ResourceIdentifiers.Template.ELEMENT)
            .build("id", getId().toString(), new NormalisingEncoder()));

        return dto;
    }


    /**
     * Create summaries for a collection of templates.
     *
     * @param templates The templates.
     * @return The corresponding summaries.
     */
    public static List<Template> mapTemplates(
                                        final List<TemplateEntity> templates) {
        final List<Template> mapped =
            new ArrayList<Template>();
        for (final TemplateEntity t : templates) {
            mapped.add(t.summarize()); }
        return mapped;
    }


    /**
     * Create deltas for a collection of templates.
     *
     * @param templates The templates.
     * @return The corresponding deltas.
     */
    protected Collection<Template> deltaTemplates(
                                         final List<TemplateEntity> templates) {
        final Collection<Template> mapped = new ArrayList<Template>();
        for (final TemplateEntity t : templates) {
            mapped.add(t.forCurrentRevision());
        }
        return mapped;
    }
}
