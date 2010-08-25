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

import ccc.api.types.Duration;
import ccc.plugins.s11n.TextParser;


/**
 * Serializer for Durations.
 *
 * @author Civic Computing Ltd.
 */
class DurationSerializer extends BaseSerializer<Duration> {


    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    DurationSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public Duration read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final Duration d = read(json);

        return d;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final Duration instance) {
        if (null==instance) { return null; }

        final Json json = writeDuration(instance, newJson());

        return json.toString();
    }


    static Duration read(final Json json) {
        if (null==json) { return null; }

        final Duration d = new Duration();
        d.setTime(json.getLong(JsonKeys.SECONDS).longValue());
        return d;
    }


    static Json writeDuration(final Duration instance, final Json json) {
        if (null==instance) { return null; }
        json.set(JsonKeys.SECONDS, Long.valueOf(instance.getTime()));
        return json;
    }
}
