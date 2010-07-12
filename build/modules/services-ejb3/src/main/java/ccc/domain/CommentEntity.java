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
package ccc.domain;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ccc.api.core.Comment;
import ccc.api.types.CommentStatus;
import ccc.api.types.DBC;
import ccc.api.types.EmailAddress;
import ccc.api.types.Link;
import ccc.api.types.NormalisingEncoder;


/**
 * A user comment.
 * FIXME: email can be NULL.
 *
 * @author Civic Computing Ltd.
 */
public class CommentEntity
    extends
        Entity {

    private String _body;
    private String _author;      // ZLS allowed.
    private URL _url;            // NULL allowed.
    private EmailAddress _email;

    private ResourceEntity _resource;

    private final Date _timestamp = new Date();
    private CommentStatus _status = CommentStatus.PENDING;


    /** Constructor: for persistence only. */
    protected CommentEntity() { super(); }


    /**
     * Constructor.
     *
     * @param resource The resource this comment refers to.
     * @param body The comment's body content.
     * @param author The comment's author.
     */
    public CommentEntity(final ResourceEntity resource,
                   final String body,
                   final String author) {
        DBC.require().notNull(resource);
        _resource = resource;

        setBody(body);
        setAuthor(author);
    }


    /**
     * Accessor.
     *
     * @return The comment's body, as a string.
     */
    public String getBody() { return _body; }


    /**
     * Mutator.
     *
     * @param body The body to set.
     */
    public final void setBody(final String body) {
        DBC.require().notEmpty(body);
        _body = body;
    }


    /**
     * Accessor.
     *
     * @return The comment's resource.
     */
    public ResourceEntity getResource() { return _resource; }


    /**
     * Accessor.
     *
     * @return The comment's author.
     */
    public String getAuthor() { return (null==_author) ? "" : _author; }


    /**
     * Mutator.
     *
     * @param author The author to set.
     */
    public final void setAuthor(final String author) {
        _author = author;
    }


    /**
     * Mutator.
     *
     * @param url The comment's URL, the URL may be NULL.
     */
    public void setUrl(final URL url) {  _url = url; }


    /**
     * Accessor.
     *
     * @return The comment's URL, the URL may be NULL.
     */
    public URL getUrl() { return _url; }


    /**
     * Accessor.
     *
     * @return The comment's timestamp, as a date.
     */
    public Date getTimestamp() { return new Date(_timestamp.getTime()); }


    /**
     * Accessor.
     *
     * @return Returns the status.
     */
    public final CommentStatus getStatus() {
        return _status;
    }


    /**
     * Mutator.
     *
     * @param status The status to set.
     */
    public final void setStatus(final CommentStatus status) {
        DBC.require().notNull(status);
        _status = status;
    }


    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    public final EmailAddress getEmail() {
        return _email;
    }


    /**
     * Mutator.
     *
     * @param email The email to set.
     */
    public final void setEmail(final EmailAddress email) {
        DBC.require().notNull(email);
        _email = email;
    }


    /**
     * Create a DTO for this comment.
     *
     * @return A DTO representing this comment.
     */
    public Comment createDto() {
        final Comment dto = new Comment(
            getAuthor(),
            getBody(),
            getResource().getId(),
            getTimestamp(),
            getUrl()==null ? null : getUrl().toExternalForm());
        dto.setId(getId());
        dto.setStatus(getStatus());
        dto.setEmail(getEmail().getText());
        dto.addLink(
            "self",
            new Link(ccc.api.core.ResourceIdentifiers.Comment.ELEMENT)
                .build("id", getId().toString(), new NormalisingEncoder()));

        return dto;
    }


    /**
     * Map a list of comments to a list of comment DTOs.
     *
     * @param comments The comments to map.
     * @return The corresponding DTOs.
     */
    public static List<Comment> map(final List<CommentEntity> comments) {
        final List<Comment> mapped = new ArrayList<Comment>();
        for (final CommentEntity c : comments) {
            mapped.add(c.createDto());
        }
        return mapped;
    }
}
