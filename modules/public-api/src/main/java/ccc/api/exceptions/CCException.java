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
package ccc.api.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.api.types.DBC;
import ccc.api.types.Failure;
import ccc.plugins.s11n.JsonKeys;


/**
 * An exception representing the failure of a CCC command.
 *
 * @author Civic Computing Ltd.
 */
public class CCException
    extends
        RuntimeException {

    private UUID                _id     = UUID.randomUUID();
    private Map<String, String> _params = new HashMap<String, String>();


    /** Constructor. */
    protected CCException() { this(null, null, new HashMap<String, String>()); }


    /**
     * Constructor.
     *
     * @param message Description of the exception.
     * @param cause   Cause of the exception.
     * @param params  Further details describing the exception.
     */
    protected CCException(final String message,
                          final Throwable cause,
                          final Map<String, String> params) {
        super(message, cause);
        _params.putAll(params);
        _params.put(JsonKeys.MESSAGE, message); // TODO: Null check?
        _params.put(JsonKeys.CAUSE, (null==cause) ? null : cause.getMessage());
    }


    /**
     * Constructor.
     *
     * @param message Description of the exception.
     */
    public CCException(final String message) {
        this(
            message,
            null,
            new HashMap<String, String>());
    }


    /**
     * Constructor.
     *
     * @param message Description of the exception.
     * @param cause   Cause of the exception.
     */
    public CCException(final String message,
                       final Throwable cause) {
        this(
            message,
            cause,
            new HashMap<String, String>());
    }


    /**
     * Accessor.
     *
     * @return The failure.
     */
    public Failure getFailure() {
        return new Failure(_id, getClass().getName(), _params);
    }


    /**
     * Accessor.
     *
     * @param key The parameter's key.
     *
     * @return The parameter's value.
     */
    protected String getParam(final String key) {
        return _params.get(key);
    }


    /**
     * Mutator.
     *
     * @param id The id to set.
     */
    public void setId(final UUID id) {
        _id = DBC.require().notNull(id);
    }


    /**
     * Mutator.
     *
     * @param params The params to set.
     */
    public void setParams(final Map<String, String> params) {
        DBC.require().notNull(params);
        _params.clear();
        _params.putAll(params);
    }
}
