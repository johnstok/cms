/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions.remote;

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.serialization.JsonKeys;
import ccc.types.Duration;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for cache duration updating.
 *
 * @author Civic Computing Ltd.
 */
public abstract class UpdateCacheDurationAction
    extends
        RemotingAction {

    private final UUID _resourceId;
    private final Duration _duration;


    /**
     * Constructor.
     * @param duration The new cache duration.
     * @param resourceId The resource to update.
     */
    public UpdateCacheDurationAction(final UUID resourceId,
                                      final Duration duration) {
        super(GLOBALS.uiConstants().editCacheDuration(), RequestBuilder.POST);
        _resourceId = resourceId;
        _duration = duration;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/resources/"+_resourceId+"/duration";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.CACHE_DURATION, _duration);
        json.set(JsonKeys.REVISION, (String) null);
        json.set(JsonKeys.TEMPLATE_ID, (String) null);
        return json.toString();
    }
}
