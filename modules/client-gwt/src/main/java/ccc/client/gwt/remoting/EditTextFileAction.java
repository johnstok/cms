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
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.GwtJson;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.events.TextFileUpdated;
import ccc.client.gwt.widgets.ContentCreator;

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

    private final File _dto;

    /**
     * Constructor.
     *
     * @param dto The dto of the file.
     */
    public EditTextFileAction(final File dto) {
        super(UI_CONSTANTS.updateTextFile(), RequestBuilder.POST);
        _dto = dto;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _dto.self().build(new GWTTemplateEncoder());
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
