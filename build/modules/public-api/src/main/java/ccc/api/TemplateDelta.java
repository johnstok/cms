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

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.MimeType;


/**
 * A delta, for updating templates.
 *
 * @author Civic Computing Ltd.
 */
public final class TemplateDelta implements Serializable, Jsonable {
    private String _body;
    private String _definition;
    private MimeType _mimeType;

    @SuppressWarnings("unused") private TemplateDelta() { super(); }

    /**
     * Constructor.
     *
     * @param body The template's body.
     * @param definition The template's definition.
     * @param mimeType The template's mime type.
     */
    public TemplateDelta(final String body,
                         final String definition,
                         final MimeType mimeType) {
        _body = body;
        _definition = definition;
        _mimeType = mimeType;
    }


    /**
     * Constructor.
     *
     * @param gwtJson
     */
    public TemplateDelta(final Json json) {
        this(
            json.getString(JsonKeys.BODY),
            json.getString(JsonKeys.DEFINITION),
            new MimeType(json.getJson(JsonKeys.MIME_TYPE))
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


    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public MimeType getMimeType() {
        return _mimeType;
    }


    /**
     * Mutator.
     *
     * @param mimeType The mimeType to set.
     */
    public void setMimeType(final MimeType mimeType) {
        _mimeType = mimeType;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.DEFINITION,  getDefinition());
        json.set(JsonKeys.BODY,        getBody());
        json.set(JsonKeys.MIME_TYPE,   getMimeType());
    }
}
