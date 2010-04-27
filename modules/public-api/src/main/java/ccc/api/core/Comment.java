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

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import ccc.api.types.CommentStatus;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable2;


/**
 * DTO describing a comment.
 *
 * @author Civic Computing Ltd.
 */
public class Comment
    implements
        Jsonable2,
        Serializable {

    private UUID          _id;
    private UUID          _resourceId;
    private String        _body;
    private String        _author;
    private String        _url;
    private String        _email;
    private Date          _timestamp;
    private CommentStatus _status = CommentStatus.PENDING;


    /**
     * Constructor.
     *
     * @param author    The comment's author.
     * @param body      The comment's body.
     * @param resource  The comment's resource.
     * @param timestamp The comment's timestamp.
     * @param url       The comment's url.
     */
    public Comment(final String author,
                      final String body,
                      final UUID resource,
                      final Date timestamp,
                      final String url) {
        _author = author;
        _body = body;
        _resourceId = resource;
        _timestamp = timestamp;
        _url = url;
    }


    /**
     * Constructor.
     */
    public Comment() { super(); }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this comment.
     */
    public Comment(final Json json) {
        fromJson(json);
    }


    /**
     * Accessor.
     *
     * @return Returns the resourceId.
     */
    public final UUID getResourceId() { return _resourceId; }


    /**
     * Accessor.
     *
     * @return Returns the body.
     */
    public final String getBody() { return _body; }


    /**
     * Accessor.
     *
     * @return Returns the author.
     */
    public final String getAuthor() { return _author; }


    /**
     * Accessor.
     *
     * @return Returns the url.
     */
    public final String getUrl() { return _url; }


    /**
     * Accessor.
     *
     * @return Returns the timestamp.
     */
    public final Date getTimestamp() { return _timestamp; }


    /**
     * Accessor.
     *
     * @return Returns the ID.
     */
    public final UUID getId() { return _id; }


    /**
     * Accessor.
     *
     * @return Returns the status.
     */
    public final CommentStatus getStatus() { return _status; }



    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    public final String getEmail() { return _email; }



    /**
     * Mutator.
     *
     * @param email The email to set.
     */
    public final void setEmail(final String email) { _email = email; }


    /**
     * Mutator.
     *
     * @param status The status to set.
     */
    public final void setStatus(final CommentStatus status) {
        _status = status;
    }


    /**
     * Mutator.
     *
     * @param body The body to set.
     */
    public final void setBody(final String body) { _body = body; }


    /**
     * Mutator.
     *
     * @param id The ID to set.
     */
    public final void setId(final UUID id) { _id = id; }


    /**
     * Mutator.
     *
     * @param resourceId The resourceId to set.
     */
    public final void setResourceId(final UUID resourceId) {
        _resourceId = resourceId;
    }


    /**
     * Mutator.
     *
     * @param author The author to set.
     */
    public final void setAuthor(final String author) { _author = author; }


    /**
     * Mutator.
     *
     * @param url The url to set.
     */
    public final void setUrl(final String url) { _url = url; }


    /**
     * Mutator.
     *
     * @param timestamp The timestamp to set.
     */
    public final void setTimestamp(final Date timestamp) {
        _timestamp = timestamp;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.DATE_CREATED, _timestamp);
        json.set(JsonKeys.TARGET_ID, _resourceId);
        json.set(JsonKeys.BODY, _body);
        json.set(JsonKeys.AUTHOR, _author);
        json.set(JsonKeys.URL, _url);
        json.set(JsonKeys.ID, _id);
        json.set(JsonKeys.STATUS, _status.name());
        json.set(JsonKeys.EMAIL, _email);
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        _timestamp = json.getDate(JsonKeys.DATE_CREATED);
        _resourceId = json.getId(JsonKeys.TARGET_ID);
        _body = json.getString(JsonKeys.BODY);
        _author = json.getString(JsonKeys.AUTHOR);
        _url = json.getString(JsonKeys.URL);
        _id = json.getId(JsonKeys.ID);
        _status = CommentStatus.valueOf(json.getString(JsonKeys.STATUS));
        _email = json.getString(JsonKeys.EMAIL);
    }
}
