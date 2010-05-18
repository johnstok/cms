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

import static ccc.plugins.s11n.JsonKeys.*;

import java.util.Map;

import ccc.api.core.ActionSummary;
import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.api.types.FailureCode;
import ccc.api.types.ResourceType;
import ccc.api.types.Username;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.Serializer;


/**
 * Serializer for {@link ActionSummary}s.
 *
 * @author Civic Computing Ltd.
 */
public class ActionSummarySerializer implements Serializer<ActionSummary> {


    /** {@inheritDoc} */
    @Override
    public ActionSummary read(final Json json) {
        if (null==json) { return null; }

        final ActionSummary s = new ActionSummary();

        final Map<String, String> links = json.getStringMap("links");
        if (null!=links) { s.addLinks(links); }
        s.setId(json.getId(ID));
        s.setType(CommandType.valueOf(json.getString(TYPE)));
        s.setActorUsername(new Username(json.getString(USERNAME)));
        s.setExecuteAfter(json.getDate(EXECUTE_AFTER));
        s.setSubjectType(ResourceType.valueOf(json.getString(SUBJECT_TYPE)));
        s.setSubjectPath(json.getString(SUBJECT_PATH));
        s.setStatus(ActionStatus.valueOf(json.getString(STATUS)));
        s.setFCode(
            (null==json.getString(CODE))
                ? null
                : FailureCode.valueOf(json.getString(CODE)));

        return s;
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final ActionSummary instance) {
        if (null==instance) { return null; }

        json.set("links", instance.getLinks());
        json.set(ID, instance.getId());
        json.set(TYPE, instance.getType().name());
        json.set(USERNAME, instance.getActorUsername().toString());
        json.set(EXECUTE_AFTER, instance.getExecuteAfter());
        json.set(SUBJECT_TYPE, instance.getSubjectType().name());
        json.set(SUBJECT_PATH, instance.getSubjectPath());
        json.set(STATUS, instance.getStatus().name());
        json.set(
            CODE,
            (null==instance.getFailureCode())
                ? null
                : instance.getFailureCode().name());

        return json;
    }
}
