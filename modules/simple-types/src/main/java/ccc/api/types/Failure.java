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
package ccc.api.types;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;


/**
 * A serializable representation of CCC failure.
 *
 * @author Civic Computing Ltd.
 */
public class Failure implements Jsonable, Serializable {

    private UUID                _id;
    private String              _code;
    private Map<String, String> _params      = new HashMap<String, String>();

    /** Constructor: for persistence only. */
    protected Failure() { super(); }


    /**
     * Constructor.
     *
     * @param id     The failure's ID.
     * @param code   The internal code for this failure.
     * @param params Further details describing the failure.
     */
    public Failure(final UUID id,
                   final String code,
                   final Map<String, String> params) {
        _code = DBC.require().notNull(code);
        _id   = DBC.require().notNull(id);
        _params.putAll(DBC.require().notNull(params));
    }


    /**
     * Constructor.
     *
     * @param json JSON representation of a failure.
     */
    public Failure(final Json json) {
        this(
            json.getId(JsonKeys.ID),
            json.getString(JsonKeys.CODE),
            json.getStringMap(JsonKeys.PARAMETERS));
    }


    /**
     * Accessor.
     *
     * @return Returns the errorCode.
     */
    public String getCode() {
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
     * @return Returns the failure's ID.
     */
    public UUID getId() {
        return _id;
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
        json.set(JsonKeys.CODE, getCode());
        json.set(JsonKeys.ID, getExceptionId());
        json.set(JsonKeys.PARAMETERS, getParams());
    }
}
