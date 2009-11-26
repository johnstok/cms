/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

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

        description(description);
        update(
            new TemplateDelta(body, definiton, mimeType),
            metadata);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceType type() {
        return ResourceType.TEMPLATE;
    }

    /**
     * Accessor for the template's body.
     *
     * @return The body as a string.
     */
    public String body() {
        return currentRevision().getBody();
    }

    /**
     * Accessor for the template's definition.
     *
     * @return The definition as a String.
     */
    public String definition() {
        return currentRevision().getDefinition();
    }

    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public MimeType mimeType() {
        return currentRevision().getMimeType();
    }

    /** {@inheritDoc} */
    @Override
    public TemplateDelta createSnapshot() {
        return new TemplateDelta(
            body(),
            definition(),
            mimeType());
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
        return mapTemplate();
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary forSpecificRevision(final int revNo) {
        // TODO: Return correct revision.
        return mapTemplate();
    }

    /** {@inheritDoc} */
    @Override
    public TemplateSummary forWorkingCopy() {
        // TODO: Return working copy.
        return mapTemplate();
    }

    /**
     * Create a summary for a template.
     *
     * @return The corresponding summary.
     */
    public TemplateSummary mapTemplate() {
        final TemplateSummary dto =
            new TemplateSummary(
                id(),
                name(),
                getTitle(),
                description(),
                body(),
                definition());
        setDtoProps(dto);
        dto.setRevision(currentRevisionNo());
        return dto;
    }
}
