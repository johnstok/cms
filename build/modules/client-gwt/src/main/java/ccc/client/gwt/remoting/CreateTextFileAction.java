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

import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.HttpMethod;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.events.ResourceCreated;
import ccc.client.gwt.widgets.ContentCreator;


/**
 * Action creating a text file on the server.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateTextFileAction
    extends
        RemotingAction {


    private File _dto;
    /**
     * Constructor.
     *
     * @param dto Text file DTO.
     */
    public CreateTextFileAction(final File dto) {
        super(UI_CONSTANTS.createTextFile(), HttpMethod.POST);
        _dto = dto;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return File.list();
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        _dto.toJson(json);
        return json.toString();
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        ContentCreator.EVENT_BUS.fireEvent(new ResourceCreated(rs));
    }
}
