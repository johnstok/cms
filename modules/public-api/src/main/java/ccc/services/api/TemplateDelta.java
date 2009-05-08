/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.api;

import java.io.Serializable;


/**
 * A delta, for updating templates.
 *
 * @author Civic Computing Ltd.
 */
public final class TemplateDelta implements Serializable {
    private ID     _id;
    private String _name;
    private String _title;
    private String _description;
    private String _body;
    private String _definition;

    @SuppressWarnings("unused") private TemplateDelta() { super(); }

    /**
     * Constructor.
     *
     * @param id
     * @param name
     * @param title
     * @param description
     * @param body
     * @param definition
     */
    public TemplateDelta(final ID     id,
                         final String name,
                         final String title,
                         final String description,
                         final String body,
                         final String definition) {
        _id = id;
        _name = name;
        _title = title;
        _description = description;
        _body = body;
        _definition = definition;
    }


    /**
     * Accessor.
     *
     * @return Returns the id.
     */
    public ID getId() {
        return _id;
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final ID id) {
        _id = id;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }


    /**
     * Mutator.
     *
     * @param name The name to set.
     */
    public void setName(final String name) {
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }


    /**
     * Mutator.
     *
     * @param title The title to set.
     */
    public void setTitle(final String title) {
        _title = title;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return _description;
    }


    /**
     * Mutator.
     *
     * @param description The description to set.
     */
    public void setDescription(final String description) {
        _description = description;
    }


    /**
     * Accessor.
     *
     * @return Returns the body.
     */
    public String getBody() {
        return _body;
    }


    /**
     * Mutator.
     *
     * @param body The body to set.
     */
    public void setBody(final String body) {
        _body = body;
    }


    /**
     * Accessor.
     *
     * @return Returns the definition.
     */
    public String getDefinition() {
        return _definition;
    }


    /**
     * Mutator.
     *
     * @param definition The definition to set.
     */
    public void setDefinition(final String definition) {
        _definition = definition;
    }
}
