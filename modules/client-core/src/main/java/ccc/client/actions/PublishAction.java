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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import ccc.client.callbacks.ResourcePublishedCallback;
import ccc.client.commands.PublishResourceCommand;
import ccc.client.core.Action;
import ccc.client.core.SingleSelectionModel;

/**
 * Binds a command to a UI event.
 *
 * @author Civic Computing Ltd.
 */
public final class PublishAction
    implements
        Action {

    private final SingleSelectionModel _ssm;

    /**
     * Constructor.
     *
     * @param ssm The selection model with selecting a resource.
     */
    public PublishAction(final SingleSelectionModel ssm) {
        _ssm = ssm;
    }

    /** {@inheritDoc} */
    @Override public void execute() {
        new PublishResourceCommand().invoke(
            _ssm.tableSelection(),
            new ResourcePublishedCallback(UI_CONSTANTS.publish()));
    }
}
