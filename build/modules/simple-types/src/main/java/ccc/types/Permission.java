/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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


/**
 * Group permissions.
 *
 * @author Civic Computing Ltd.
 */
public final class Permission {

    private Permission() { super(); }

    /** CONTENT_CREATOR : String. */
    public static final String CONTENT_CREATOR = "CONTENT_CREATOR";

    /** SITE_BUILDER : String. */
    public static final String SITE_BUILDER = "SITE_BUILDER";

    /** ADMINISTRATOR : String. */
    public static final String ADMINISTRATOR = "ADMINISTRATOR";

    /** API_USER : String. */
    public static final String API_USER = "API_USER";
}
