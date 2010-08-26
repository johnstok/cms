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

import ccc.api.core.Failure;


/**
 * Serializer for {@link Failure}s.
 *
 * @author Civic Computing Ltd.
 */
class FailureSerializer extends BaseSerializer<Failure> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    FailureSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public Failure read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final Failure f = read(json);

        return f;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final Failure instance) {
        return write(instance, newJson()).toString();
    }


    static Failure read(final Json json) {
        if (null==json) { return null; }
        return
            new Failure(
                json.getId(JsonKeys.ID),
                json.getString(JsonKeys.CODE),
                json.getStringMap(JsonKeys.PARAMETERS));
    }


    static Json write(final Failure instance, final Json json) {
        if (null==instance) { return null; }

        json.set(JsonKeys.CODE, instance.getCode());
        json.set(JsonKeys.ID, instance.getExceptionId());
        json.set(JsonKeys.PARAMETERS, instance.getParams());

        return json;
    }
}
