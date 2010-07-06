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
package ccc.client.core;

import java.util.MissingResourceException;

import ccc.api.types.ActionStatus;
import ccc.api.types.CommandType;
import ccc.client.i18n.ActionNameConstants;
import ccc.client.i18n.ActionStatusConstants;
import ccc.client.i18n.CommandTypeConstants;
import ccc.client.i18n.ErrorDescriptions;
import ccc.client.i18n.ErrorResolutions;
import ccc.client.i18n.UIConstants;
import ccc.client.i18n.UIMessages;



/**
 * Service locator for i18n.
 *
 * @author Civic Computing Ltd.
 */
public class I18n {

    public static UIConstants UI_CONSTANTS;
    public static UIMessages UI_MESSAGES;
    public static ErrorDescriptions ERROR_DESCRIPTIONS;
    public static ErrorResolutions ERROR_RESOLUTIONS;
    public static ActionStatusConstants ACTION_STATUSES;
    public static CommandTypeConstants COMMAND_TYPES;
    public static ActionNameConstants USER_ACTIONS;


    /**
     * Looks up for localised string for the {@link ActionStatus}.
     *
     * @param status The status to localise.
     *
     * @return The localised string or name of the enum if nothing found.
     */
    public static String getLocalisedStatus(final ActionStatus status) {
        final ActionStatusConstants types = ACTION_STATUSES;

        String local = null;
        try {
            local = types.getString(camelCase(status.name()));
        } catch (final MissingResourceException e) {
            local = status.name();
        }
        return local;
    }

    /**
     * Looks up for localised string for the command type.
     *
     * @param command The command to localise.
     *
     * @return The localised string or name of the enum if nothing found.
     */
    public static String getLocalisedType(final CommandType command) {
        final CommandTypeConstants types = COMMAND_TYPES;

        String local = null;
        try {
            local = types.getString(camelCase(command.name()));
        } catch (final MissingResourceException e) {
            local = command.name();
        }
        return local;
    }


    /**
     * Convert a string to 'camel case'.
     *
     * @param string The string to convert to camel case.
     *
     * @return The string in camel case.
     */
    public static String camelCase(final String string) {
        final StringBuilder sb = new StringBuilder();
        final char[] chars = string.toCharArray();
        for (int i=0; i<chars.length; i++) {
            if ('_'==chars[i]) {
                i++;
                sb.append(Character.toUpperCase(chars[i]));
            } else {
                sb.append(Character.toLowerCase(chars[i]));
            }
        }
        return sb.toString();
    }
}
