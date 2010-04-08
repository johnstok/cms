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
package ccc.contentcreator.remoting;

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a template.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateTemplateAction
    extends
        RemotingAction {

    private final UUID _parentFolder;
    private final String _resourceName;
    private final TemplateDelta _delta;

    /**
     * Constructor.
     *
     * @param parentFolder The folder in which the template will be created.
     * @param resourceName The name of the template.
     * @param delta The template's details.
     */
    public CreateTemplateAction(final UUID parentFolder,
                                 final String resourceName,
                                 final TemplateDelta delta) {
        super(GLOBALS.uiConstants().createTemplate(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _resourceName = resourceName;
        _delta = delta;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/templates";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final Json json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentFolder);
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.TITLE, _resourceName);
        json.set(JsonKeys.DESCRIPTION, "");
        json.set(JsonKeys.NAME, _resourceName);
        return json.toString();
    }

    /**
     * Handle the result of a successful call.
     *
     * @param template The newly created template.
     */
    protected abstract void execute(ResourceSummary template);
}
