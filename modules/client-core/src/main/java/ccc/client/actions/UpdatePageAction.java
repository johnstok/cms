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

import ccc.api.core.Page;
import ccc.client.core.HttpMethod;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;


/**
 * Remote action for page updating.
 *
 * @author Civic Computing Ltd.
 */
public class UpdatePageAction
    extends
        RemotingAction<Page> {

    private final Page _details;


    /**
     * Constructor.
     *
     * @param details Details of the update.
     */
    public UpdatePageAction(final Page details) {
        super(UI_CONSTANTS.updateContent(), HttpMethod.PUT);
        _details = details;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _details.self().build(InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writePage(_details);
    }


    /** {@inheritDoc} */
    @Override
    protected Page parse(final Response response) {
        return readPage(response);
    }
}
