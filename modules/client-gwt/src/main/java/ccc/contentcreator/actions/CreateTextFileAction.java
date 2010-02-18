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

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TextFileDto;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreateTextFileAction  extends
        RemotingAction {


    private TextFileDto _dto;
    /**
     * Constructor.
     *
     * @param dto Text file DTO.
     */
    public CreateTextFileAction(final TextFileDto dto) {
        super(GLOBALS.uiConstants().createTextFile(), RequestBuilder.POST);

        _dto = dto;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {

        return "/files";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _dto.getParentId());
        json.set(JsonKeys.NAME, _dto.getName());
        json.set(JsonKeys.MIME_TYPE, _dto.getMimeType());
        json.set(JsonKeys.MAJOR_CHANGE, _dto.isMajorRevision());
        json.set(JsonKeys.COMMENT, _dto.getRevisionComment());
        json.set(JsonKeys.DATA, _dto.getContent());
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
