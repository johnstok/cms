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
package ccc.api.temp;

import ccc.api.core.Failure;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link Failure}s.
 *
 * @author Civic Computing Ltd.
 */
public class FailureSerializer implements Serializer<Failure> {


    /** {@inheritDoc} */
    @Override
    public Failure read(final Json json) {
        if (null==json) { return null; }

        final Failure f =
            new Failure(
                json.getId(JsonKeys.ID),
                json.getString(JsonKeys.CODE),
                json.getStringMap(JsonKeys.PARAMETERS));

        return f;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final Failure instance) {
        if (null==instance) { return null; }

        json.set(JsonKeys.CODE, instance.getCode());
        json.set(JsonKeys.ID, instance.getExceptionId());
        json.set(JsonKeys.PARAMETERS, instance.getParams());

        return json;
    }
}
