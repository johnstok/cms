/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.dto;

import java.io.Serializable;

import ccc.api.types.MimeType;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;


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
     * @param json The JSON representation for this class.
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
