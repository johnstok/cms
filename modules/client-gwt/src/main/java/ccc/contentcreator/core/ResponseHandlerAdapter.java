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
package ccc.contentcreator.core;

import ccc.contentcreator.events.Error;
import ccc.contentcreator.widgets.ContentCreator;
import ccc.serialization.Json;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;


/**
 * Default response handler implementation.
 * <p>Fails for all responses.
 *
 * @author Civic Computing Ltd.
 */
public class ResponseHandlerAdapter
    implements
        ResponseHandler {

    private final String  _name;


    /**
     * Constructor.
     *
     * @param name The action name.
     */
    public ResponseHandlerAdapter(final String name) {
        _name = name;
    }


    /** {@inheritDoc} */
    @Override
    public void onNoContent(final Response response) {
        onUnsupported(response);
    }


    /** {@inheritDoc} */
    @Override
    public void onOK(final Response response) {
        onUnsupported(response);
    }


    /** {@inheritDoc} */
    @Override
    public void onBadRequest(final Response response) {
        onFailed(new RuntimeException(response.getText()));
//        onFailed(new InvalidException(toJson(response)));
    }


    /** {@inheritDoc} */
    @Override
    public void onMethodNotAllowed(final Response response) {
        onUnsupported(response);
    }


    /** {@inheritDoc} */
    @Override
    public void onUnsupported(final Response response) {
        onFailed(
            new RuntimeException(// TODO Add UnsupportedResponseException?
                "Unsupported response: "
                + response.getStatusCode() + " "
                + response.getStatusText()));
    }



    /** {@inheritDoc} */
    @Override
    public void onConflict(final Response response) {
        onFailed(new RuntimeException(response.getText()));
//        onFailed(new ConflictException(toJson(response)));
    }


    /** {@inheritDoc} */
    @Override
    public void onError(final Response response) {
        onFailed(new RuntimeException(response.getText()));
//        onFailed(new InternalError(toJson(response)));
    }


    /** {@inheritDoc} */
    @Override
    public void onNotFound(final Response response) {
        onFailed(new RuntimeException(response.getText()));
//        onFailed(new EntityNotFoundException(toJson(response)));
    }


    /** {@inheritDoc} */
    @Override
    public void onUnauthorized(final Response response) {
        onFailed(new RuntimeException(response.getText()));
//        onFailed(new UnauthorizedException(toJson(response)));
    }


    /** {@inheritDoc} */
    @Override
    public void onSessionTimeout(final Response response) {
        onFailed(new SessionTimeoutException(response.getText()));
    }


    /** {@inheritDoc} */
    @Override
    public void onFailed(final Throwable throwable) {
        ContentCreator.EVENT_BUS.fireEvent(new Error(throwable, _name));
    }


    private Json toJson(final Response response) {
        final JSONValue value = JSONParser.parse(response.getText());
        final JSONObject object = value.isObject();
        final Json json = new GwtJson(object);
        return json;
    }
}
