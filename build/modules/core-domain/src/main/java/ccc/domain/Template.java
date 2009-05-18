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

import ccc.api.MimeType;
import ccc.api.ResourceType;
import ccc.commons.DBC;


/**
 * A template is used to define how a resource will be displayed.
 *
 * @author Civic Computing Ltd
 */
public class Template extends Resource {

    private String _body;
    private String _definition;
    private MimeType _mimeType;

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
        DBC.require().notNull(body);
        DBC.require().notNull(definiton);
        DBC.require().notNull(mimeType);

        description(description);
        _body = body;
        _definition = definiton;
        _mimeType = mimeType;
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
        return _body;
    }

    /**
     * Accessor for the template's definition.
     *
     * @return The definition as a String.
     */
    public String definition() {
        return _definition;
    }

    /**
     * Mutator for definition.
     *
     * @param definition The new definition.
     */
    public void definition(final String definition) {
        DBC.require().notEmpty(definition);
        _definition = definition;
    }

    /**
     * Mutator for body.
     *
     * @param body The new body.
     */
    public void body(final String body) {
        DBC.require().notEmpty(body);
        _body = body;
    }

    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public MimeType mimeType() {
        return _mimeType;
    }

    /**
     * Mutator.
     *
     * @param mimeType The mimeType to set.
     */
    public void mimeType(final MimeType mimeType) {
        _mimeType = mimeType;
    }

    /** {@inheritDoc} */
    @Override
    public Snapshot createSnapshot() {
        final Snapshot s = super.createSnapshot();
        s.set("description", description());
        s.set("definition", _definition);
        s.set("body", _body);
        return s;
    }
}
