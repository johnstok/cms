/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.plugins.search;


/**
 * Exceptional condition for the search engine.
 *
 * @author Civic Computing Ltd.
 */
public class SearchException
    extends
        Exception {


    /**
     * Constructor.
     */
    public SearchException() {
        super();
    }


    /**
     * Constructor.
     *
     * @param message The message describing this exception.
     * @param cause The root cause of the exception.
     */
    public SearchException(final String message, final Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructor.
     *
     * @param message The message describing this exception.
     */
    public SearchException(final String message) {
        super(message);
    }


    /**
     * Constructor.
     *
     * @param cause The root cause of the exception.
     */
    public SearchException(final Throwable cause) {
        super(cause);
    }
}
