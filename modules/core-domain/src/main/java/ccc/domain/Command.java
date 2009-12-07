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
package ccc.domain;

import java.util.Date;



/**
 * A command that can be executed using a CCC action.
 *
 * @param <T> The return type of the command.
 *
 * @author Civic Computing Ltd.
 */
public interface Command<T> {

    /**
     * Execute the command.
     *
     * @param action The action that supplies the command parameters.
     * @param happenedOn When the command took place.
     * @return A command specific result of type T.
     * @throws CccCheckedException If the command fails.
     */
    T execute(Action action, Date happenedOn) throws CccCheckedException;
}
