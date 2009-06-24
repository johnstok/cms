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

import ccc.api.DBC;
import ccc.api.MimeType;
import ccc.api.ResourceType;
import ccc.api.TemplateDelta;


/**
 * A template is used to define how a resource will be displayed.
 *
 * @author Civic Computing Ltd
 */
public class Template
    extends
        HistoricalResource<TemplateRevision> {

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
     * TODO: Add a description for this method.
     * TODO: Pull version incrementing up to HistoricalResource.
     *
     * @param delta
     */
    public void update(final TemplateDelta delta,
                       final RevisionMetadata metadata) {
        incrementVersion();
        addRevision(
            new TemplateRevision(
                currentVersion(),
                metadata.getTimestamp(),
                metadata.getActor(),
                metadata.isMajorChange(),
                metadata.getComment(),
                delta.getBody(),
                delta.getDefinition(),
                delta.getMimeType()));
    }
}
