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
package ccc.contentcreator.actions;

import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a folder.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateFolderAction
    extends
        RemotingAction {

    private final String _name;
    private final UUID _parentFolder;

    /**
     * Constructor.
     *
     * @param name The folder's name.
     * @param parentFolder The folder's parent folder.
     */
    public CreateFolderAction(final UUID parentFolder, final String name) {
        super(GLOBALS.uiConstants().createFolder(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _name = name;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentFolder);
        json.set(JsonKeys.NAME, _name);
        return json.toString();
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }

    /**
     * Handle the result of a successful call.
     *
     * @param folder The folder returned.
     */
    protected abstract void execute(ResourceSummary folder);
}
