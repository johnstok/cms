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
 * Revision      $Rev: 1762 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2009-09-01 15:17:03 +0100 (Tue, 01 Sep 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;


/**
 * API for a action scheduler.
 *
 * @author Civic Computing Ltd.
 */
@Produces("application/json")
@Consumes("application/json")
@Deprecated
// FIXME: Remove this class; fold into Actions.
public interface ActionScheduler extends Scheduler {

    /** NAME : String. */
    String NAME = "ActionScheduler";

}
