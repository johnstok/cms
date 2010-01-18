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
 * Revision      $Rev: 2246 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-12-07 17:46:54 +0000 (Mon, 07 Dec 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.actions;

import java.util.UUID;

import com.google.gwt.http.client.Response;


/**
 * Get a list of all images from the CCC server.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetImagesCountAction
    extends
        RemotingAction {

    private final UUID _parentId;

    /**
     * Constructor.
     *
     * @param parentId The id of the folder.
     * @param actionName Local-specific name for the action.
     */
    public GetImagesCountAction(final String actionName,
                                  final UUID parentId) {
        super(actionName);
        _parentId = parentId;

    }

    /** {@inheritDoc} */
    @Override protected String getPath() {
        return "/files/imagescount/"+_parentId+"/";
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final String count = response.getText();
        execute(Integer.decode(count).intValue());
    }

    /**
     * Handle the data returned from the server.
     *
     * @param count The number of available images.
     */
    protected abstract void execute(int count);
}
