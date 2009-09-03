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
package ccc.rest.dto;

import static ccc.serialization.JsonKeys.*;

import java.io.Serializable;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.Jsonable;


/**
 * A summary of a template.
 *
 * @author Civic Computing Ltd.
 */
public final class TemplateSummary implements Serializable, Jsonable {
    private UUID     _id;
    private String _name;
    private String _title;
    private String _description;
    private String _body;
    private String _definition;

    @SuppressWarnings("unused") private TemplateSummary() { super(); }

    /**
     * Constructor.
     *
     * @param UUID The template's UUID.
     * @param name The template's name.
     * @param title The template's title.
     * @param description The template's description.
     * @param body The template's body.
     * @param definition The template's definition.
     */
    public TemplateSummary(final UUID   UUID,
                         final String name,
                         final String title,
                         final String description,
                         final String body,
                         final String definition) {
        _id = UUID;
        _name = name;
        _title = title;
        _description = description;
        _body = body;
        _definition = definition;
    }


    /**
     * Constructor.
     *
     * @param json
     */
    public TemplateSummary(final Json json) {
        this(
            json.getId(ID),
            json.getString(NAME),
            json.getString(TITLE),
            json.getString(DESCRIPTION),
            json.getString(BODY),
            json.getString(DEFINITION)
        );
    }

    /**
     * Accessor.
     *
     * @return Returns the UUID.
     */
    public UUID getId() {
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

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(ID, getId());
        json.set(NAME, getName());
        json.set(TITLE, getTitle());
        json.set(DESCRIPTION, getDescription());
        json.set(BODY, getBody());
        json.set(DEFINITION, getDefinition());
    }
}
