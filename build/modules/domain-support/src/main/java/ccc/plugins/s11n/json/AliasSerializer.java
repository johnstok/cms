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

import ccc.api.core.Alias;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * Serializer for {@link Alias}s.
 *
 * @author Civic Computing Ltd.
 */
public class AliasSerializer
    extends
        ResourceSerializer<Alias> {


    /** {@inheritDoc} */
    @Override
    public Alias read(final Json json) {
        if (null==json) { return null; }

        final Alias a = super.read(json);

        a.setTargetId(json.getId(JsonKeys.TARGET_ID));

        return a;
    }


    /** {@inheritDoc} */
    @Override protected Alias createObject() { return new Alias(); }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final Alias instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.set(JsonKeys.TARGET_ID, instance.getTargetId());

        return json;
    }
}
