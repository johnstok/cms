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
package ccc.client.gwt.core;

import ccc.client.remoting.TextParser;
import ccc.plugins.s11n.Json;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;


/**
 * Implementation of the {@link TextParser} API using the GWT library.
 *
 * @author Civic Computing Ltd.
 */
public class GWTTextParser
    implements
        TextParser {


    /** {@inheritDoc} */
    @Override
    public Json parseJson(final String text) {
        final JSONObject result =
            JSONParser.parse(text).isObject();
        final Json json = new GwtJson(result);
        return json;
    }
}
