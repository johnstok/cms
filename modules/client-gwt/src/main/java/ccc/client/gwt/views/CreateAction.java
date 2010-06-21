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

package ccc.client.gwt.views;

import java.util.Date;
import java.util.Map;

import ccc.api.types.CommandType;
import ccc.client.core.Editable;
import ccc.client.core.Validatable;
import ccc.client.core.View;


/**
 * API for create action dialogs.
 *
 * @author Civic Computing Ltd.
 */
public interface CreateAction extends View<Editable>, Validatable {

    /**
     * Display an alert with the specified message.
     *
     * @param message The message to display.
     */
    void alert(String message);

    /**
     * Accessor.
     *
     * @return The command type.
     */
    CommandType getCommandType();

    /**
     * Accessor.
     *
     * @return The date.
     */
    Date getDate();

    /**
     * Accessor.
     *
     * @return The action parameters.
     */
    Map<String, String> getActionParameters();

}
