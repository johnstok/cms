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
package ccc.rest.dto;

import java.io.Serializable;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


/**
 * A new page.
 *
 * @author Civic Computing Ltd.
 */
public class PageDto implements Jsonable, Serializable {

    private final UUID      _parentId;
    private final PageDelta _delta;
    private final String    _name;
    private final UUID      _templateId;
    private final String    _title;
    private final String    _comment;
    private final boolean   _majorChange;


    /**
     * Constructor.
     *
     * @param parentId The page's parent folder id.
     * @param delta The page delta.
     * @param name The page's name.
     * @param templateId The page's template id.
     * @param title The page's title.
     * @param comment The comment for a page update.
     * @param majorChange Is the update a major change.
     */
    public PageDto(final UUID parentId,
                   final PageDelta delta,
                   final String name,
                   final UUID templateId,
                   final String title,
                   final String comment,
                   final boolean majorChange) {
        _parentId = parentId;
        _delta = delta;
        _name = name;
        _templateId = templateId;
        _title = title;
        _comment = comment;
        _majorChange = majorChange;
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
    public final PageDelta getDelta() {
        return _delta;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public final String getName() {
        return _name;
    }


    /**
     * Accessor.
     *
     * @return Returns the templateId.
     */
    public final UUID getTemplateId() {
        return _templateId;
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
     * @return Returns the comment.
     */
    public final String getComment() {
        return _comment;
    }

    /**
     * Accessor.
     *
     * @return Returns the majorChange.
     */
    public final boolean getMajorChange() {
        return _majorChange;
    }

    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.NAME, _name);
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
        json.set(JsonKeys.TITLE, _title);
        json.set(JsonKeys.COMMENT, _comment);
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.valueOf(_majorChange));
    }
}
