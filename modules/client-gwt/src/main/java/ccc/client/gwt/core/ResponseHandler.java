/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.client.gwt.core;

/**
 * API for handling HTTP responses.
 *
 * @author Civic Computing Ltd.
 */
public interface ResponseHandler {

    /**
     * Handle a '204 NO CONTENT' response from the remote server.
     *
     * @param response The server response.
     */
    void onNoContent(final Response response);

    /**
     * Handle a '200 OK' response from the remote server.
     *
     * @param response The server response.
     */
    void onOK(final Response response);


    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onMethodNotAllowed(final Response response);


    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onBadRequest(final Response response);


    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onSessionTimeout(final Response response);

    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onUnsupported(final Response response);

    /**
     * TODO: Add a description for this method.
     *
     * @param throwable
     */
    void onFailed(final Throwable throwable);

    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onNotFound(Response response);

    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onError(Response response);

    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onUnauthorized(Response response);

    /**
     * TODO: Add a description for this method.
     *
     * @param response
     */
    void onConflict(Response response);
}
