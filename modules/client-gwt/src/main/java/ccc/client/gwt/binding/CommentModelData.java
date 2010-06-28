/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.client.gwt.binding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.api.core.Comment;
import ccc.api.types.CommentStatus;
import ccc.plugins.s11n.JsonKeys;



/**
 * A CCC comment.
 *
 * @author Civic Computing Ltd.
 */
public final class CommentModelData
    extends
        CccModelData {

    private Comment _delegate;


    /**
     * Constructor.
     *
     * @param delegate The DTO this model delegates to.
     */
    public CommentModelData(final Comment delegate) {
        _delegate = delegate;
    }


    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(final String property) {
        if (property.equals(JsonKeys.ID)) {
            return (X) _delegate.getId();
        } else if (property.equals(JsonKeys.URL)) {
            return (X) _delegate.getUrl();
        } else if (property.equals(JsonKeys.DATE_CREATED)) {
            return (X) _delegate.getTimestamp();
        } else if (property.equals(JsonKeys.AUTHOR)) {
            return (X) _delegate.getAuthor();
        } else if (property.equals(JsonKeys.STATUS)) {
            return (X) _delegate.getStatus();
        } else {
            throw new IllegalArgumentException("Key not supported: "+property);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public Collection<String> getPropertyNames() {
        return
            Arrays.asList(
                JsonKeys.ID,
                JsonKeys.URL,
                JsonKeys.DATE_CREATED,
                JsonKeys.AUTHOR,
                JsonKeys.STATUS);
    }


    /** {@inheritDoc} */
    @Override
    public <X> X remove(final String property) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /** {@inheritDoc} */
    @Override
    public <X> X set(final String property, final X value) {
        throw new UnsupportedOperationException("Method not implemented.");
    }


    /**
     * Accessor.
     *
     * @return The comment's author.
     */
    public String getAuthor() {
        return _delegate.getAuthor();
    }


    /**
     * Accessor.
     *
     * @return The comment's body text.
     */
    public String getBody() {
        return _delegate.getBody();
    }


    /**
     * Accessor.
     *
     * @return The resource the comment refers to.
     */
    public UUID getResourceId() {
        return _delegate.getResourceId();
    }


    /**
     * Accessor.
     *
     * @return The comment's status.
     */
    public CommentStatus getStatus() {
        return _delegate.getStatus();
    }


    /**
     * Accessor.
     *
     * @return The date the comment was created.
     */
    public Date getTimestamp() {
        return _delegate.getTimestamp();
    }


    /**
     * Accessor.
     *
     * @return The comment's url.
     */
    public String getUrl() {
        return _delegate.getUrl();
    }


    /** {@inheritDoc} */
    @Override
    public UUID getId() {
        return _delegate.getId();
    }


    /**
     * Accessor.
     *
     * @return The email address.
     */
    public String getEmail() {
        return _delegate.getEmail();
    }


    /**
     * Mutator.
     *
     * @param comment The comment delegated to.
     */
    public void setDelegate(final Comment comment) {
        _delegate = comment;
    }


    /**
     * Return the comment object backing this model data.
     *
     * @return The delegate comment.
     */
    public Comment getDelegate() { return _delegate; }
}
