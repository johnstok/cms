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
import ccc.client.core.HttpMethod;
import ccc.client.core.RemotingAction;
import ccc.client.gwt.core.GWTTemplateEncoder;



/**
 * Update an alias.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateAliasAction
    extends
        RemotingAction {

    private final Alias _details;


    /**
     * Constructor.
     *
     * @param details The new alias details.
     */
    public UpdateAliasAction(final Alias details) {
        super(UI_CONSTANTS.updateAlias(), HttpMethod.PUT);
        _details = details;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return _details.self().build(new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        return writeAlias(_details);
    }
}
