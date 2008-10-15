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
     * @param body The body for the template.
     */
    public Template(final String title,
                    final String description,
                    final String body) {

        super(title);
        _description = description;
        _body = body;
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
