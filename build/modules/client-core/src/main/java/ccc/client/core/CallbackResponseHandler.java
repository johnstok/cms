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
package ccc.client.core;

import ccc.api.types.DBC;

/**
 * Response handler that maps to the simpler {@link Callback} interface.
 *
 * @author Civic Computing Ltd.
 *
 * @param <T> The type of the response from the server.
 */
public final class CallbackResponseHandler<T>
    extends
        ResponseHandlerAdapter {

    private final Callback<T> _callback;
    private final Parser<T>   _parser;


    /**
     * Constructor.
     *
     * @param name     The name of the action.
     * @param callback The callback to invoke.
     * @param parser   The response parser.
     */
    public CallbackResponseHandler(final String name,
                                   final Callback<T> callback,
                                   final Parser<T> parser) {
        super(name);
        _callback = DBC.require().notNull(callback);
        _parser   = DBC.require().notNull(parser);
    }


    /** {@inheritDoc} */
    @Override
    public void onNoContent(final Response response) {
        _callback.onSuccess(null);
    }

    /** {@inheritDoc} */
    @Override
    public void onOK(final Response response) {
        _callback.onSuccess(_parser.parse(response));
    }

    /** {@inheritDoc} */
    @Override
    public void onFailed(final Throwable throwable) {
        _callback.onFailure(throwable);
    }
}
