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

import ccc.api.dto.TextFileDelta;
import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.contentcreator.events.TextFileUpdated;
import ccc.contentcreator.widgets.ContentCreator;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Action for updating the text file's content.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileAction
    extends
        RemotingAction {

    private final TextFileDelta _dto;

    /**
     * Constructor.
     *
     * @param dto The dto of the file.
     */
    public EditTextFileAction(final TextFileDelta dto) {
        super(UI_CONSTANTS.updateTextFile(), RequestBuilder.POST);
        _dto = dto;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/files/"+_dto.getId();
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
    protected void onNoContent(final Response response) {
        ContentCreator.EVENT_BUS.fireEvent(new TextFileUpdated());
    }
}
