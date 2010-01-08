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

import ccc.rest.dto.CommentDto;
import ccc.types.CommentStatus;
import ccc.types.DBC;


/**
 * A user comment.
 *
 * @author Civic Computing Ltd.
 */
public class Comment
    extends
        Entity {

    private String _body;
    private Resource _resource;
    private String _author;
    private URL _url;
    private final Date _timestamp = new Date();
    private CommentStatus _status = CommentStatus.PENDING;

    /** Constructor: for persistence only. */
    protected Comment() { super(); }

    /**
     * Constructor.
     *
     * @param resource The resource this comment refers to.
     * @param body The comment's body content.
     * @param author The comment's author.
     */
    public Comment(final Resource resource,
                   final String body,
                   final String author) {
        DBC.require().notNull(resource);
        DBC.require().notEmpty(author);

        setBody(body);
        _resource = resource;
        _author = author;
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
        DBC.require().notNull(body);
        _body = body;
    }


    /**
     * Accessor.
     *
     * @return The comment's resource.
     */
    public Resource getResource() { return _resource; }


    /**
     * Accessor.
     *
     * @return The comment's author.
     */
    public String getAuthor() { return _author; }


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
    public Date getTimestamp() { return _timestamp; }


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
     * Create a DTO for this comment.
     *
     * @return A DTO representing this comment.
     */
    public CommentDto createDto() {
        final CommentDto dto = new CommentDto(
            getAuthor(),
            getBody(),
            getResource().id(),
            getTimestamp(),
            getUrl().toExternalForm());
        dto.setId(id());
        dto.setStatus(getStatus());

        return dto;
    }

    /**
     * Map a list of comments to a list of comment DTOs.
     *
     * @param comments The comments to map.
     * @return The corresponding DTOs.
     */
    public static List<CommentDto> map(final List<Comment> comments) {
        final List<CommentDto> mapped = new ArrayList<CommentDto>();
        for (final Comment c : comments) {
            mapped.add(c.createDto());
        }
        return mapped;
    }
}