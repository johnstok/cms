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

import ccc.rest.snapshots.ResourceSnapshot;
import ccc.serialization.Json;
import ccc.serialization.Jsonable;
import ccc.types.ResourceName;


/**
 * A summary of a template.
 *
 * @author Civic Computing Ltd.
 */
public final class TemplateSummary
    extends
        ResourceSnapshot
    implements
        Serializable,
        Jsonable {

    private String _body;
    private String _definition;

    @SuppressWarnings("unused") private TemplateSummary() { super(); }

    /**
     * Constructor.
     *
     * @param id The template's UUID.
     * @param name The template's name.
     * @param title The template's title.
     * @param description The template's description.
     * @param body The template's body.
     * @param definition The template's definition.
     */
    public TemplateSummary(final UUID id,
                           final ResourceName name,
                           final String title,
                           final String description,
                           final String body,
                           final String definition) {
        setId(id);
        setName(name);
        setTitle(title);
        setDescription(description);
        _body = body;
        _definition = definition;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation for this class.
     */
    public TemplateSummary(final Json json) {
        this(
            json.getId(ID),
            new ResourceName(json.getString(NAME)),
            json.getString(TITLE),
            json.getString(DESCRIPTION),
            json.getString(BODY),
            json.getString(DEFINITION)
        );
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
        json.set(NAME, getName().toString());
        json.set(TITLE, getTitle());
        json.set(DESCRIPTION, getDescription());
        json.set(BODY, getBody());
        json.set(DEFINITION, getDefinition());
    }
}
