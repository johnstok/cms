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

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import ccc.api.core.Action;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;


/**
 * Serializer for {@link Action}s.
 *
 * @author Civic Computing Ltd.
 */
class ActionSerializer extends BaseSerializer<Action> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    ActionSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public Action read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final UUID resourceId =
            json.getId(JsonKeys.SUBJECT_ID);
        final CommandType command =
            CommandType.valueOf(json.getString(JsonKeys.COMMAND));
        final Date execAfter =
            json.getDate(JsonKeys.EXECUTE_AFTER);
        final Map<String, String> parameters =
            json.getStringMap(JsonKeys.PARAMETERS);

        final Action a = new Action(resourceId, command, execAfter, parameters);

        a.setActor(json.getId(JsonKeys.ACTOR_ID));
        final String status = json.getString(JsonKeys.STATUS);
        a.setStatus((null==status) ? null : ActionStatus.valueOf(status));
        a.setFailure(FailureSerializer.read(json.getJson(JsonKeys.FAILURE)));

        return a;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final Action instance) {
        if (null==instance) { return null; }
        final Json json = newJson();

        json.set(JsonKeys.SUBJECT_ID, instance.getResourceId());
        json.set(JsonKeys.COMMAND, instance.getCommand().name());
        json.set(JsonKeys.EXECUTE_AFTER, instance.getExecuteAfter());
        json.set(JsonKeys.PARAMETERS, instance.getParameters());
        json.set(JsonKeys.ACTOR_ID, instance.getActor());
        final ActionStatus status = instance.getStatus();
        json.set(JsonKeys.STATUS, (null==status) ? null : status.name());
        json.set(
            JsonKeys.FAILURE,
            FailureSerializer.write(instance.getFailure(), newJson()));

        return json.toString();
    }
}
