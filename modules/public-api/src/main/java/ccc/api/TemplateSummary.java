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
package ccc.api;

import java.io.Serializable;


/**
 * A delta, for updating templates.
 *
 * @author Civic Computing Ltd.
 */
public final class TemplateSummary implements Serializable {
    private ID     _id;
    private String _name;
    private String _title;
    private String _description;
    private String _body;
    private String _definition;

    @SuppressWarnings("unused") private TemplateSummary() { super(); }

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
    public TemplateSummary(final ID   id,
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
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
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
     * Accessor.
     *
     * @return Returns the description.
     */
    public String getDescription() {
        return _description;
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
     * Accessor.
     *
     * @return Returns the definition.
     */
    public String getDefinition() {
        return _definition;
    }
}
