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
package ccc.plugins.s11n.json;

import ccc.api.core.Comment;
import ccc.api.types.CommentStatus;


/**
 * Serializer for {@link Comment}s.
 *
 * @author Civic Computing Ltd.
 */
class CommentSerializer extends BaseSerializer<Comment> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    CommentSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public Comment read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final Comment c = new Comment();

        ResourceMappings.readRes(json, c);

        c.setTimestamp(json.getDate(JsonKeys.DATE_CREATED));
        c.setResourceId(json.getId(JsonKeys.TARGET_ID));
        c.setBody(json.getString(JsonKeys.BODY));
        c.setAuthor(json.getString(JsonKeys.AUTHOR));
        c.setUrl(json.getString(JsonKeys.URL));
        c.setId(json.getId(JsonKeys.ID));
        c.setStatus(CommentStatus.valueOf(json.getString(JsonKeys.STATUS)));
        c.setEmail(json.getString(JsonKeys.EMAIL));

        return c;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final Comment instance) {
        if (null==instance) { return null; }
        final Json json = newJson();

        ResourceMappings.writeRes(json, instance);

        json.set("links", instance.getLinks());
        json.set(JsonKeys.DATE_CREATED, instance.getTimestamp());
        json.set(JsonKeys.TARGET_ID, instance.getResourceId());
        json.set(JsonKeys.BODY, instance.getBody());
        json.set(JsonKeys.AUTHOR, instance.getAuthor());
        json.set(JsonKeys.URL, instance.getUrl());
        json.set(JsonKeys.ID, instance.getId());
        json.set(JsonKeys.STATUS, instance.getStatus().name());
        json.set(JsonKeys.EMAIL, instance.getEmail());

        return json.toString();
    }
}
