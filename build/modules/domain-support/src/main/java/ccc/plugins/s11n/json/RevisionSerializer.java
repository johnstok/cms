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

import ccc.api.core.Revision;
import ccc.api.types.CommandType;
import ccc.api.types.Username;
import ccc.plugins.s11n.TextParser;


/**
 * Serializer for {@link Revision}s.
 *
 * @author Civic Computing Ltd.
 */
class RevisionSerializer extends BaseSerializer<Revision> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    RevisionSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public Revision read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final Revision r =
            new Revision(
                CommandType.valueOf(json.getString(JsonKeys.COMMAND)),
                new Username(json.getString(JsonKeys.USERNAME)),
                json.getDate(JsonKeys.HAPPENED_ON),
                json.getLong(JsonKeys.INDEX).longValue(),
                json.getString(JsonKeys.COMMENT),
                json.getBool(JsonKeys.MAJOR_CHANGE).booleanValue());

        return r;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final Revision instance) {
        if (null==instance) { return null; }
        final Json json = newJson();

        json.set(JsonKeys.COMMAND, instance.getCommand().name());
        json.set(JsonKeys.USERNAME, instance.getActorUsername().toString());
        json.set(JsonKeys.HAPPENED_ON, instance.getHappenedOn());
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.valueOf(instance.isMajor()));
        json.set(JsonKeys.INDEX, Long.valueOf(instance.getIndex()));
        json.set(JsonKeys.COMMENT, instance.getComment());

        return json.toString();
    }

}
