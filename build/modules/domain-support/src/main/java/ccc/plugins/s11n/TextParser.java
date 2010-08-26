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

package ccc.plugins.s11n;

import java.util.Map;

import ccc.plugins.s11n.json.Json;


/**
 * API for parsing text.
 *
 * @author Civic Computing Ltd.
 */
public interface TextParser {


    /**
     * Parse text into a JSON object.
     *
     * @param text The text to parse.
     *
     * @return The corresponding JSON object.
     */
    Json parseJson(final String text);


    /**
     * Parse a string map into a JSON object.
     *
     * @param map The map to parse.
     *
     * @return The corresponding JSON object.
     */
    Json parseJson(final Map<String, String> map);


    /**
     * Create a new JSON object.
     *
     * @return The new object.
     */
    Json newJson();
}
