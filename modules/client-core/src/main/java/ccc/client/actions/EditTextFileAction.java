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
package ccc.client.actions;

import ccc.api.core.File;
import ccc.api.types.CommandType;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;
import ccc.client.events.Event;


/**
 * Action for updating the text file's content.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileAction
    extends
        RemotingAction<File> {

    private final File _dto;


    /**
     * Constructor.
     *
     * @param dto The dto of the file.
     */
    public EditTextFileAction(final File dto) {
        super(UI_CONSTANTS.updateTextFile(), HttpMethod.POST);
        _dto = dto;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _dto.self().build(InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() { return writeFile(_dto); }


    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final File f) {
        final Event<CommandType> event =
            new Event<CommandType>(CommandType.FILE_UPDATE);
        event.addProperty("file", f);
        InternalServices.remotingBus.fireEvent(event);
    }


    /** {@inheritDoc} */
    @Override
    protected File parse(final Response response) {
        return readFile(response);
    }
}
