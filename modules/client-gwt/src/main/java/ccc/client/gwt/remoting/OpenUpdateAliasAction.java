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

import ccc.api.core.Alias;
import ccc.api.core.ResourceSummary;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.RemotingAction;
import ccc.client.gwt.core.Response;
import ccc.client.gwt.views.gxt.UpdateAliasDialog;



/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OpenUpdateAliasAction
    extends
        RemotingAction {

    private final ResourceSummaryModelData _alias;
    private final ResourceSummary _targetRoot;

    /**
     * Constructor.
     * @param alias The alias to update.
     * @param targetRoot The target root of the alias.
     */
    public OpenUpdateAliasAction(final ResourceSummaryModelData alias,
                             final ResourceSummary targetRoot) {
        super(UI_CONSTANTS.updateAlias());
        _alias = alias;
        _targetRoot = targetRoot;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return Alias.targetName(_alias.getId()).build(new GWTTemplateEncoder());
    }

    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final String targetName = response.getText();
        new UpdateAliasDialog(
            _alias.getId(), targetName, _alias.getName(), _targetRoot)
        .show();
    }

}
