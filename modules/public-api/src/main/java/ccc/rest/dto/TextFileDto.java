/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.rest.dto;


import static ccc.plugins.s11n.JsonKeys.*;

import java.io.Serializable;
import java.util.UUID;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;
import ccc.types.MimeType;


/**
 * A new text file.
 *
 * @author Civic Computing Ltd.
 */
public final class TextFileDto
implements Serializable, Jsonable {

    private final UUID                _parentId;
    private final String              _name;
    private final String              _content;
    private final MimeType            _mimeType;
    private final boolean             _isMajorRevision;
    private final String              _revisionComment;

    /**
     * Constructor.
     *
     * @param parentId The parent ID.
     * @param name The file's name.
     * @param mimeType The file's mime type.
     * @param isMajorRevision Is this a major revision.
     * @param revisionComment Comment describing the revision.
     * @param content The file's content.
     */
    public TextFileDto(final UUID parentId,
                         final String name,
                         final MimeType mimeType,
                         final boolean isMajorRevision,
                         final String revisionComment,
                         final String content) {
        _parentId = parentId;
        _name = name;
        _mimeType = mimeType;
        _isMajorRevision = isMajorRevision;
        _revisionComment = revisionComment;
        _content = content;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this delta.
     */
    public TextFileDto(final Json json) {
        this(
            json.getId(PARENT_ID),
            json.getString(NAME),
            new MimeType(json.getJson(JsonKeys.MIME_TYPE)),
            json.getBool(MAJOR_CHANGE),
            json.getString(COMMENT),
            json.getString(DATA));
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
     * Accessor.
     *
     * @return Returns true if this is a major revision, false otherwise.
     */
    public boolean isMajorRevision() {
        return _isMajorRevision;
    }


    /**
     * Accessor.
     *
     * @return Returns the revision comment.
     */
    public String getRevisionComment() {
        return _revisionComment;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(NAME, getName());
        json.set(PARENT_ID, getParentId());
        json.set(MIME_TYPE, getMimeType());
        json.set(MAJOR_CHANGE, Boolean.valueOf(isMajorRevision()));
        json.set(COMMENT, getRevisionComment());
        json.set(DATA, getContent());

    }

    /**
     * Accessor.
     *
     * @return Returns the content.
     */
    public String getContent() {
        return _content;
    }

    /**
     * Accessor.
     *
     * @return Returns the parentId.
     */
    public UUID getParentId() {
        return _parentId;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }

}
