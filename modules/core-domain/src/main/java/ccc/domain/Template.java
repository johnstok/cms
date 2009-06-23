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

import java.util.ArrayList;
import java.util.List;

import ccc.api.DBC;
import ccc.api.MimeType;
import ccc.api.ResourceType;
import ccc.api.TemplateDelta;


/**
 * A template is used to define how a resource will be displayed.
 *
 * @author Civic Computing Ltd
 */
public class Template extends Resource {

    private List<TemplateRevision> _history = new ArrayList<TemplateRevision>();
    private int                    _pageVersion = -1;


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
                    final MimeType mimeType) {

        this(
            ResourceName.escape(title),
            title,
            description,
            body,
            definiton,
            mimeType);
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
                    final MimeType mimeType) {

        super(name, title);
        DBC.require().notNull(description);

        description(description);
        _pageVersion++;
        _history.add(
            new TemplateRevision(
                _pageVersion,
                true,
                "Created.",
                body,
                definiton,
                mimeType));
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
            title(),
            description(),
            body(),
            definition(),
            mimeType());
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public TemplateRevision currentRevision() {
        for (final TemplateRevision r : _history) {
            if (_pageVersion==r.getIndex()) {
                return r;
            }
        }
        throw new RuntimeException("No current revision!");
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param i
     * @return
     */
    public TemplateRevision revision(final int i) {
        for (final TemplateRevision r : _history) {
            if (i==r.getIndex()) {
                return r;
            }
        }
        throw new RuntimeException("No current revision!");
    }

    /**
     * TODO: Add a description for this method.
     *
     * @param delta
     */
    public void update(final TemplateDelta delta) {
        _pageVersion++;
        _history.add(
            new TemplateRevision(
                _pageVersion,
                true,
                "Updated.",
                delta.getBody(),
                delta.getDefinition(),
                delta.getMimeType()));
    }
}
