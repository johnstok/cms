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
package ccc.rest;

import ccc.rest.exceptions.RestException;
import ccc.serialization.Json;
import ccc.types.Failure;
import ccc.types.FailureCode;


/**
 * Indicates an unexpected internal CC error.
 * FIXME Rename to avoid collision with JRE exception.
 *
 * @author Civic Computing Ltd.
 */
public class InternalError
    extends
        RestException {


    /**
     * Constructor.
     *
     * @param failure
     */
    public InternalError() {
        super(new Failure(FailureCode.UNEXPECTED));
    }


    /**
     * Constructor.
     *
     * @param json The JSON representation of this exception.
     */
    public InternalError(final Json json) {
        super(json);
    }


    /**
     * Constructor.
     *
     * @param failure The failure that caused this exception.
     */
    public InternalError(final Failure failure) {
        super(failure);
    }
}
