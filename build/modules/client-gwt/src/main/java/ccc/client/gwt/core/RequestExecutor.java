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
 * Revision      $Rev: 2636 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-04-09 14:56:56 +0100 (Fri, 09 Apr 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.client.gwt.core;

/**
 * Sends an HTTP request to a host server.
 *
 * @author Civic Computing Ltd.
 */
public interface RequestExecutor {

    /**
     * Send the specified request.
     *
     * @param request The request to send.
     */
    void invokeRequest(final Request request);

}
