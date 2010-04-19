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

import ccc.api.types.Failure;
import ccc.api.types.FailureCode;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.Jsonable2;




/**
 * An exception representing the failure of a CCC command.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CCException
    extends
        RuntimeException
    implements
        Jsonable2 {

    private Failure _failure;

    @SuppressWarnings("unused") private CCException() { super(); }


    /**
     * Constructor.
     *
     * @param failure The failure.
     */
    public CCException(final Failure failure) {
        super("CCC Error: "+failure.getExceptionId());
        _failure = failure;
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this exception.
     */
    public CCException(final Json json) {
        fromJson(json);
    }

    /**
     * Accessor.
     *
     * @return The failure's code.
     */
    public FailureCode getCode() {
        return _failure.getCode();
    }

    /**
     * Accessor.
     *
     * @return The failure.
     */
    public Failure getFailure() {
        return _failure;
    }


    /** {@inheritDoc} */
    @Override
    public void fromJson(final Json json) {
        _failure = new Failure(json);
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        _failure.toJson(json);
    }
}
