/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
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
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.api.types;

/**
 * A list of resource names for predefined resources.
 *
 * @author Civic Computing Ltd
 */
public final class PredefinedResourceNames {

    private PredefinedResourceNames() { super(); }

    /** CONTENT : String. */
    public static final String CONTENT = "content";

    /** ASSETS : String. */
    public static final String ASSETS  = "assets";

    /** TRASH : String. */
    public static final String TRASH  = "trash";

    /** TEMPLATES : String. */
    public static final String TEMPLATES  = "templates";

    /** FILES : String. */
    public static final String FILES  = "files";

    /** IMAGES : String. */
    public static final String IMAGES  = "images";

    /** CSS : String. */
    public static final String CSS  = "css";
}
