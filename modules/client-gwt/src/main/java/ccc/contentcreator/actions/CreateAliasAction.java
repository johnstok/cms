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

import java.util.UUID;

import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.RemotingAction;
import ccc.serialization.JsonKeys;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Create an alias.
 *
 * @author Civic Computing Ltd.
 */
public class CreateAliasAction
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
        super(UI_CONSTANTS.createAlias(), RequestBuilder.POST);
        _parentId = parentId;
        _aliasName = aliasName;
        _targetId = targetId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/aliases";
    }

    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.NAME, _aliasName);
        json.set(JsonKeys.TARGET_ID, _targetId);
        return json.toString();
    }
}
