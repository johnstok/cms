/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api.core;

import static ccc.plugins.s11n.JsonKeys.*;

import java.util.UUID;

import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;
import ccc.api.types.URIBuilder;
import ccc.plugins.s11n.Json;


/**
 * A new template.
 *
 * @author Civic Computing Ltd.
 */
public class Template
    extends
        Resource {

    static final String COLLECTION = "/secure/templates";
    static final String ELEMENT    = COLLECTION + "/{id}";
    static final String EXISTS     = COLLECTION + "/{name}/exists";
    static final String DELTA      = ELEMENT + "/delta";

    private String   _body;
    private String   _definition;
    private MimeType _mimeType;


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
        super.toJson(json);

        json.set(DEFINITION,  getDefinition());
        json.set(BODY,        getBody());
        json.set(MIME_TYPE,   getMimeType());
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        super.fromJson(json);

        setDefinition(json.getString(DEFINITION));
        setBody(json.getString(BODY));
        final Json mime = json.getJson(MIME_TYPE);
        setMimeType((null==mime) ? null : new MimeType(mime));
    }

    /**
     * Create a summary DTO.
     *
     * @param id The template's UUID.
     * @param name The template's name.
     * @param title The template's title.
     * @param description The template's description.
     * @param body The template's body.
     * @param definition The template's definition.
     *
     * @return A template DTO with the appropriate fields set.
     */
    public static Template summary(final UUID id,
                                      final ResourceName name,
                                      final String title,
                                      final String description,
                                      final String body,
                                      final String definition) {
        final Template t = new Template();
        t.setId(id);
        t.setName(name);
        t.setTitle(title);
        t.setDescription(description);
        t._body = body;
        t._definition = definition;
        return t;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public static String list() {
        return Template.COLLECTION;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param id
     * @return
     */
    public static String delta(final UUID id) {
        return
            new URIBuilder(Template.DELTA)
            .replace("id", id.toString())
            .toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param name
     * @return
     */
    public static String exists(final String name) {
        return
            new URIBuilder(Template.EXISTS)
            .replace("name", name)
            .toString();
    }


    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public String self() {
        return
            new URIBuilder(Template.ELEMENT)
            .replace("id", getId().toString())
            .toString();
    }
}
