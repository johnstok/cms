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
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link Revision}s.
 *
 * @author Civic Computing Ltd.
 */
public class RevisionSerializer
    implements
        Serializer<Revision> {


    /** {@inheritDoc} */
    @Override
    public Revision read(final Json json) {
        if (null==json) { return null; }

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
    public Json write(final Json json, final Revision instance) {
        if (null==instance) { return null; }

        json.set(JsonKeys.COMMAND, instance.getCommand().name());
        json.set(JsonKeys.USERNAME, instance.getActorUsername().toString());
        json.set(JsonKeys.HAPPENED_ON, instance.getHappenedOn());
        json.set(JsonKeys.MAJOR_CHANGE, Boolean.valueOf(instance.isMajor()));
        json.set(JsonKeys.INDEX, Long.valueOf(instance.getIndex()));
        json.set(JsonKeys.COMMENT, instance.getComment());

        return json;
    }

}
