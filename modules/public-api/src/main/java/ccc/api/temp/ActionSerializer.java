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

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.api.core.Action;
import ccc.api.types.CommandType;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link Action}s.
 *
 * @author Civic Computing Ltd.
 */
public class ActionSerializer implements Serializer<Action> {


    /** {@inheritDoc} */
    @Override
    public Action read(final Json json) {
        if (null==json) { return null; }

        final UUID resourceId =
            json.getId(JsonKeys.SUBJECT_ID);
        final CommandType command =
            CommandType.valueOf(json.getString(JsonKeys.COMMAND));
        final Date execAfter =
            json.getDate(JsonKeys.EXECUTE_AFTER);
        final Map<String, String> parameters =
            json.getStringMap(JsonKeys.PARAMETERS);

        final Action a = new Action(resourceId, command, execAfter, parameters);

        return a;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final Action instance) {
        if (null==instance) { return null; }

        json.set(JsonKeys.SUBJECT_ID, instance.getResourceId());
        json.set(JsonKeys.COMMAND, instance.getCommand().name());
        json.set(JsonKeys.EXECUTE_AFTER, instance.getExecuteAfter());
        json.set(JsonKeys.PARAMETERS, instance.getParameters());

        return json;
    }
}
