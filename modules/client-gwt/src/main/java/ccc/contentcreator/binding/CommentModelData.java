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
package ccc.contentcreator.binding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.contentcreator.core.GlobalsImpl;
import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.Request;
import ccc.contentcreator.core.ResponseHandlerAdapter;
import ccc.contentcreator.events.CommentUpdatedEvent;
import ccc.contentcreator.widgets.ContentCreator;
import ccc.rest.dto.CommentDto;
import ccc.serialization.JsonKeys;
import ccc.types.CommentStatus;
import ccc.types.DBC;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;



/**
 * A CCC comment.
 *
 * @author Civic Computing Ltd.
 */
public final class CommentModelData
    extends
        CccModelData {

    private CommentDto _delegate;


    /**
     * Constructor.
     *
     * @param delegate The DTO this model delegates to.
     */
    public CommentModelData(final CommentDto delegate) {
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


    public final String getAuthor() {
        return _delegate.getAuthor();
    }


    public final String getBody() {
        return _delegate.getBody();
    }


    public final UUID getResourceId() {
        return _delegate.getResourceId();
    }


    public final CommentStatus getStatus() {
        return _delegate.getStatus();
    }


    public final Date getTimestamp() {
        return _delegate.getTimestamp();
    }


    public final String getUrl() {
        return _delegate.getUrl();
    }


    /** {@inheritDoc} */
    @Override
    public final UUID getId() {
        return _delegate.getId();
    }


    public final String getEmail() {
        return _delegate.getEmail();
    }


    public void setDelegate(final CommentDto updated) {
        _delegate = updated;
    }


    /**
     * Update this comment.
     *
     * @param comment The new comment data.
     *
     * @return The HTTP request to perform the update.
     */
    public static Request update(final CommentDto comment) {
        final String path = "api/secure/comments/"+comment.getId();

        final GwtJson json = new GwtJson();
        comment.toJson(json);

        return new Request(
            RequestBuilder.POST,
            path,
            json.toString(),
            new CommentUpdatedCallback(
                new GlobalsImpl().uiConstants().updateComment(),
                comment));
    }



    /**
     * Callback handler for updating a comment.
     *
     * @author Civic Computing Ltd.
     */
    public static class CommentUpdatedCallback extends ResponseHandlerAdapter {

        private final CommentDto _comment;

        /**
         * Constructor.
         *
         * @param name The action name.
         * @param comment The updated comment.
         */
        public CommentUpdatedCallback(final String name,
                                      final CommentDto comment) {
            super(name);
            _comment = DBC.require().notNull(comment);
        }

        /** {@inheritDoc} */
        @Override
        public void onNoContent(final Response response) {
            ContentCreator.EVENT_BUS.fireEvent(
                new CommentUpdatedEvent(_comment));
        }
    }
}
