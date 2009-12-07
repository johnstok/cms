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
package ccc.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;


/**
 * A serializable representation of CCC failure.
 *
 * @author Civic Computing Ltd.
 */
public class Failure implements Jsonable, Serializable {

    private UUID                _id          = UUID.randomUUID();
    private FailureCode         _code        = FailureCode.UNEXPECTED;
    private Map<String, String> _params      = new HashMap<String, String>();

    /** Constructor: for persistence only. */
    protected Failure() { super(); }


    /**
     * Constructor.
     *
     * @param code The internal code for this failure.
     */
    public Failure(final FailureCode code) {
        _code = code;
    }


    /**
     * Constructor.
     *
     * @param code The internal code for this failure.
     * @param params Further details describing the failure.
     */
    public Failure(final FailureCode code,
                   final Map<String, String> params) {
        _code = code;
        _params.putAll(params);
    }


    /**
     * Constructor.
     *
     * @param json JSON representation of a failure.
     */
    public Failure(final Json json) {
        _id = UUID.fromString(json.getString(JsonKeys.ID));
        _code = FailureCode.valueOf(json.getString(JsonKeys.CODE));
        _params = json.getStringMap(JsonKeys.PARAMETERS);
    }


    /**
     * Accessor.
     *
     * @return Returns the errorCode.
     */
    public FailureCode getCode() {
        return _code;
    }


    /**
     * Accessor.
     *
     * @return Returns the exceptionId.
     */
    public String getExceptionId() {
        return _id.toString();
    }


    /**
     * Accessor.
     *
     * @return Returns the parameters.
     */
    public final Map<String, String> getParams() {
        return _params;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.CODE, getCode().name());
        json.set(JsonKeys.ID, getExceptionId());
        json.set(JsonKeys.PARAMETERS, getParams());
    }
}
