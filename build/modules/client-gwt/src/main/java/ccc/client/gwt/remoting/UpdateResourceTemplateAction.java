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
package ccc.client.gwt.remoting;

import java.util.UUID;

import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.events.ResourceTemplateChanged;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Remote action for resource's template updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateResourceTemplateAction
    extends
        RemotingAction {

    private final UUID _resourceId;
    private final UUID _templateId;


    /**
     * Constructor.
     * @param templateId The template to set.
     * @param resourceId The resource to update.
     */
    public UpdateResourceTemplateAction(final UUID resourceId,
                                         final UUID templateId) {
        super(UI_CONSTANTS.chooseTemplate(), RequestBuilder.POST);
        _resourceId = resourceId;
        _templateId = templateId;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/resources/"+_resourceId+"/template";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        json.set(JsonKeys.REVISION, (String) null);
        json.set(JsonKeys.CACHE_DURATION, (String) null);
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onNoContent(final Response response) {
        ContentCreator.EVENT_BUS.fireEvent(
            new ResourceTemplateChanged(_resourceId, _templateId));
    }
}
