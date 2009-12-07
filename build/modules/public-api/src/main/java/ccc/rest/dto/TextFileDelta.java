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

import static ccc.serialization.JsonKeys.*;

import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.MimeType;


/**
 * A representation of a file.
 *
 * @author Civic Computing Ltd.
 */
public class TextFileDelta
    implements
        Jsonable {

    private final UUID                _id;
    private final String              _content;
    private final MimeType            _mimeType;
    private final boolean             _isMajorRevision;
    private final String              _revisionComment;


    /**
     * Constructor.
     *
     * @param id The file's ID.
     * @param content The file's content.
     * @param mimeType The file's mime-type.
     * @param isMajorRevision Is this a major revision.
     * @param revisionComment Comment describing the revision.
     */
    public TextFileDelta(final UUID id,
                    final String content,
                    final MimeType mimeType,
                    final boolean isMajorRevision,
                    final String revisionComment) {
        _id = id;
        _content = content;
        _mimeType = mimeType;
        _isMajorRevision = isMajorRevision;
        _revisionComment = revisionComment;
    }

    /**
     * Constructor.
     *
     * @param json The JSON representation for this class.
     */
    public TextFileDelta(final Json json) {
        this(
            json.getId(ID),
            json.getString(DATA),
            new MimeType(json.getJson(JsonKeys.MIME_TYPE)),
            json.getBool(MAJOR_CHANGE),
            json.getString(COMMENT)
        );
    }


    /**
     * Accessor.
     *
     * @return Returns the ID.
     */
    public UUID getId() {
        return _id;
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
     * @return Returns the mime-type.
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
    @Override public void toJson(final Json json) {
        json.set(MIME_TYPE, getMimeType());
        json.set(ID, getId());
        json.set(MAJOR_CHANGE, Boolean.valueOf(isMajorRevision()));
        json.set(COMMENT, getRevisionComment());
        json.set(DATA, getContent());
    }
}
