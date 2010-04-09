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

import java.util.UUID;

import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Request;


/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public final class CreateAliasAction
    extends
        RemotingAction {

    private final UUID _parentId;
    private final String _aliasName;
    private final UUID _targetId;


    /**
     * Constructor.
     *
     * @param targetId The id of the target resource.
     * @param aliasName The name of the alias.
     * @param parentId The id of the alias' parent folder.
     */
    public CreateAliasAction(final UUID parentId,
                              final String aliasName,
                              final UUID targetId) {
        _parentId = parentId;
        _aliasName = aliasName;
        _targetId = targetId;
    }


    /** {@inheritDoc} */
    @Override
    protected Request getRequest() {
        return ResourceSummaryModelData.createAlias(
            _parentId, _aliasName, _targetId);
    }
}
