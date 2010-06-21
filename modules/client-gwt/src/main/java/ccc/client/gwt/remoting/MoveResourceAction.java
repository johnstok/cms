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

import ccc.api.core.ResourceSummary;
import ccc.client.core.HttpMethod;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.core.RemotingAction;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class MoveResourceAction
    extends
        RemotingAction {

    private final ResourceSummary _resource;
    private final UUID _parent;


    /**
     * Constructor.
     *
     * @param newParent The new parent folder the resource.
     * @param resource The resource to move.
     */
    public MoveResourceAction(final ResourceSummary resource,
                              final UUID newParent) {
        super(UI_CONSTANTS.move(), HttpMethod.POST);
        _resource = resource;
        _parent = newParent;
    }


    /** {@inheritDoc} */
    @Override protected String getPath() {
        return _resource.move().build(new GWTTemplateEncoder());
    }


    /** {@inheritDoc} */
    @Override protected String getBody() {
        return _parent.toString();
    }
}
