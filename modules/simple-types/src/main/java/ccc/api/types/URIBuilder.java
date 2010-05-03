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
package ccc.api.types;



/**
 * Helper class for building URIs.
 *
 * @author Civic Computing Ltd.
 */
// FIXME: Move this out of the api-types module.
public class URIBuilder {

    private String _uri;


    /**
     * Constructor.
     *
     * @param uri The URI as a string.
     */
    public URIBuilder(final String uri) {
        _uri = DBC.require().notNull(uri);
    }


    /**
     * Replace a wildcard in the URI.
     *
     * @param name The wildcard name.
     * @param value The replacement value.
     *
     * @return Return a reference to 'this' to allow method chaining.
     */
    public URIBuilder replace(final String name, final String value) {
        // FIXME: URL encoding?
        _uri = _uri.replaceAll("\\{"+name+"\\}", value);
        return this;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _uri;
    }
}
