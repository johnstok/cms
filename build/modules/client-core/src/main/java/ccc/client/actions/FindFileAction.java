/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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
 * Revision      $Rev: 3212 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-10-14 14:54:42 +0100 (Thu, 14 Oct 2010) $
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import java.util.UUID;

import ccc.api.core.File;
import ccc.api.types.Link;
import ccc.client.core.InternalServices;
import ccc.client.core.RemotingAction;
import ccc.client.core.Response;

/**
 * Open taxonomy.
 *
 * @author Civic Computing Ltd.
 */
public abstract class FindFileAction
    extends
        RemotingAction<File> {


    private final UUID _id;


    /**
     * Constructor.
     *
     * @param id The file ID to look up.
     */
    public FindFileAction(final UUID id) {
        super(USER_ACTIONS.unknownAction());
        _id = id;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        // FIXME hard coded path
        return new Link("/secure/files/{id}")
            .build("id", _id.toString(), InternalServices.encoder);
    }


    /** {@inheritDoc} */
    @Override
    public abstract void onSuccess(final File item);


    /** {@inheritDoc} */
    @Override
    protected File parse(final Response response) {
        return readFile(response);
    }
}
