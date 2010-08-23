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

import java.util.UUID;

import ccc.api.types.DBC;
import ccc.api.types.MimeType;
import ccc.api.types.ResourceName;


/**
 * A new template.
 *
 * @author Civic Computing Ltd.
 */
public class Template
    extends
        Resource {

    /** MAXIMUM_PARAGRAPHS : int. */
    public static final int MAXIMUM_PARAGRAPHS = 32;

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
        DBC.ensure().maxOccurrences(
            definition.toLowerCase(),
            "<field ",
            MAXIMUM_PARAGRAPHS);
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
        DBC.ensure().maxOccurrences(
            definition.toLowerCase(),
            "<field ",
            MAXIMUM_PARAGRAPHS);
        final Template t = new Template();
        t.setId(id);
        t.setName(name);
        t.setTitle(title);
        t.setDescription(description);
        t._body = body;
        t._definition = definition;
        return t;
    }


    /** EXISTS : String. */
    public static final String EXISTS = "template-exists";

    /** REVISION : String. */
    public static final String REVISION = "revision";

}
