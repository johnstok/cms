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

import ccc.contentcreator.core.RemotingAction;

import com.google.gwt.http.client.RequestBuilder;



/**
 * Abstract action for property loading. Implement onOK method for accessing
 * map values. See LoginDialog or AboutDialog.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetPropertyAction
    extends
        RemotingAction {


    /**
     * Constructor.
     */
    public GetPropertyAction() {
        super(USER_ACTIONS.readProperties(), RequestBuilder.GET, false);
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/sessions/allproperties";
    }

}
