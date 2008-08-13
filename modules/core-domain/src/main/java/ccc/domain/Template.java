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


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Template extends Resource {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = -4937503495883222143L;
    private String _description;
    private String _body;

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    private Template() { super(); }

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

        super(ResourceName.escape(title), title);
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
     * {@inheritDoc}
     */
    @Override
    public final String toJSON() {
        return "{}";
    }

    /**
     * Accessor for the template's description.
     *
     * @return The description as a string.
     */
    public final String description() {
        return _description;
    }

    /**
     * Accessor for the template's body.
     *
     * @return The body as a string.
     */
    public final String body() {
        return _body;
    }

}
