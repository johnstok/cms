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


/**
 * API for accessing the UI window.
 *
 * @author Civic Computing Ltd.
 */
public interface Window {

    /**
     * Factory for alert dialogs.
     *
     * @param string The message for the dialog.
     */
    void alert(final String string);


    /**
     * Factory for confirm dialogs.
     *
     * @param string The message for the dialog.
     *
     * @return True if the user confirmed the action, false otherwise.
     */
    boolean confirm(final String string);


    /**
     * Refresh the application.
     */
    void refresh();


    /**
     * Redirect to another url. Use with caution the application will exit and
     * all local state will be lost.
     *
     * @param relativeURL The host-relative URL.
     */
    void redirectTo(final String relativeURL);


    /**
     * Get a startup parameter.
     *
     * @param string The parameter name.
     *
     * @return The parameter's value, as a String; NULL if no parameter exists.
     */
    String getParameter(String string);


    /**
     * Configure the app to request confirmation from the user if they try to
     * navigate away from the app.
     */
    void enableExitConfirmation();


    /**
     * Disable app confirmation from the user if they try to navigate away from
     * the app.
     */
    void disableExitConfirmation();
}
