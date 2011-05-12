/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

package ccc.client.core;

import ccc.api.core.Resource;
import ccc.api.core.ResourceSummary;
import ccc.api.types.Link.Encoder;


/**
 * An action that makes a server-side call.
 *
 * @param <T> The type of object we're invoking a command for.
 * @param <U> The type of the return value.
 *
 * @author Civic Computing Ltd.
 */
public abstract class Command<T, U>
    extends
        S11nHelper {

    private RequestExecutor _executor = InternalServices.executor;
    private Encoder         _encoder  = InternalServices.encoder;


    /**
     * Invoke the command.
     *
     * @param subject  The object the command will be invoked for.
     * @param callback The callback handling the outcome.
     */
    public abstract void invoke(T subject, final Callback<U> callback);


    /**
     * Accessor.
     *
     * @return Returns the executor.
     */
    protected final RequestExecutor getExecutor() {
        return _executor;
    }

    /**
     * Accessor.
     *
     * @return Returns the encoder.
     */
    protected final Encoder getEncoder() {
        return _encoder;
    }


    /**
     * Get the base URL for a command.
     *
     * @return The URL as a string.
     */
    protected final String getBaseUrl() { return Globals.API_URL; }


    /* ====================================================================
     * Parser factory.
     * ================================================================== */

    /**
     * Create a parser for a resource.
     *
     * @return A {@link Parser} implementation.
     */
    protected Parser<Resource> resourceParser() {
        return new Parser<Resource>() {
            @Override
            public Resource parse(final Response response) {
                return readResource(response);
            }
        };
    }


    /**
     * Create a NULL parser for a void response.
     *
     * @return A {@link Parser} implementation.
     */
    protected Parser<Void> voidParser() {
        return new Parser<Void>() {
            @Override public Void parse(final Response response) {
                return null;
            }
        };
    }


    /**
     * Create a parser for a resource summary.
     *
     * @return A {@link Parser} implementation.
     */
    protected Parser<ResourceSummary> resourceSummaryParser() {
        return new Parser<ResourceSummary>() {
            @Override
            public ResourceSummary parse(final Response response) {
                return parseResourceSummary(response);
            }
        };
    }
}
