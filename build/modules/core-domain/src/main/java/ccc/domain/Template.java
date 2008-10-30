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

import ccc.commons.DBC;


/**
 * A template is used to define how a resource will be displayed.
 *
 * @author Civic Computing Ltd
 */
public class Template extends Resource {

    private String _description;
    private String _body;
    private String _definition;

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    protected Template() { super(); }

    /**
     * Constructor.
     *
     * @param title The title of the template.
     * @param description The description for the template.
     * @param body A valid velocity template for rendering a page.
     * @param definiton An xml definition of the fields that the body requires.
     */
    public Template(final String title,
                    final String description,
                    final String body,
                    final String definiton) {

        super(title);
        DBC.require().notNull(description);
        DBC.require().notNull(body);
        DBC.require().notNull(definiton);

        _description = description;
        _body = body;
        _definition = definiton;
    }

    /**
     * Constructor.
     *
     * @param name The name of the template.
     * @param title The title of the template.
     * @param description The description for the template.
     * @param body A valid velocity template for rendering a page.
     * @param definiton An xml definition of the fields that the body requires.
     */
    public Template(final ResourceName name,
                    final String title,
                    final String description,
                    final String body,
                    final String definiton) {

        super(name, title);
        DBC.require().notNull(description);
        DBC.require().notNull(body);
        DBC.require().notNull(definiton);

        _description = description;
        _body = body;
        _definition = definiton;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ResourceType type() {
        return ResourceType.TEMPLATE;
    }

    /**
     * Accessor for the template's description.
     *
     * @return The description as a string.
     */
    public String description() {
        return _description;
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
     * Mutator for description.
     *
     * @param description The new description.
     */
    public void description(final String description) {
        DBC.require().notEmpty(description);
        _description = description;
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
}
