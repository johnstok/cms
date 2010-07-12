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

package ccc.commons;

/**
 * Tools for helping with Java5 enum's.
 *
 * @author Civic Computing Ltd.
 */
public final class EnumTools {

    /**
     * Retrieve an enum constant.
     *
     * @param className The enum's class.
     * @param value The enum's name.
     *
     * @return The corresponding enum.
     */
    @SuppressWarnings("unchecked")
    public Enum<?> of(final String className, final String value) {
        try {
            return
                Enum.valueOf(
                    (Class<Enum>) Class.forName(className), value);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}