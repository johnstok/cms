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
 * Revision      $Rev: 3439 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2011-02-09 09:54:36 +0000 (Wed, 09 Feb 2011) $
 *
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.actions;

import ccc.client.core.Action;
import ccc.client.core.InternalServices;


/**
 * Display build documentation window.
 *
 * @author Civic Computing Ltd.
 */
public final class OpenBuildDocsAction
    implements
        Action {

    /** {@inheritDoc} */
    public void execute() {
    	InternalServices.window.openUrl(InternalServices.globals.appURL()+"static/manual/api/index.html",
          "_blank",
          "height=480,width=640,"
          + "menubar=no,toolbar=no,location=no,"
          + "resizable=yes,scrollbars=yes,status=no");
    }
}
