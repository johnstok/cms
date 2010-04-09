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
package ccc.api.dto;

import java.io.Serializable;
import java.util.UUID;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;


/**
 * A new template.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDto implements Jsonable, Serializable {

    private final UUID            _parentId;
    private final TemplateDelta _delta;
    private final String        _title;
    private final String        _description;
    private final String        _name;


    /**
     * Constructor.
     *
     * @param parentId    The template's parent.
     * @param delta       The template's details.
     * @param title       The template's title.
     * @param description The template's description.
     * @param name        The template's name.
     */
    public TemplateDto(final UUID parentId,
                       final TemplateDelta delta,
                       final String title,
                       final String description,
                       final String name) {
        _parentId = parentId;
        _delta = delta;
        _title = title;
        _description = description;
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the parentId.
     */
    public final UUID getParentId() {
        return _parentId;
    }


    /**
     * Accessor.
     *
     * @return Returns the delta.
     */
    public final TemplateDelta getDelta() {
        return _delta;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public final String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public final String getDescription() {
        return _description;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public final String getName() {
        return _name;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.TITLE, _title);
        json.set(JsonKeys.DESCRIPTION, _description);
        json.set(JsonKeys.NAME, _name);
    }
}
