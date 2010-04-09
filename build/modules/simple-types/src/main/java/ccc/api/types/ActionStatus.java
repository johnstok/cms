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

/**
 * Supported statuses for an action.
 *
 * @author Civic Computing Ltd.
 */
public enum ActionStatus {

    /** SCHEDULED : ActionStatus. */
    SCHEDULED,
    /** COMPLETE : ActionStatus. */
    COMPLETE,
    /** FAILED : ActionStatus. */
    FAILED,
    /** CANCELLED : ActionStatus. */
    CANCELLED;

    /**
     * Get the name of the name in 'camel case'.
     *
     * @return The camel case name as a string.
     */
    public String camelCaseName() {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = name().toCharArray();
        for (int i=0; i<chars.length; i++) {
            if ('_'==chars[i]) {
                i++;
                sb.append(chars[i]);
            } else {
                sb.append(Character.toLowerCase(chars[i]));
            }
        }
        return sb.toString();
    }
}
