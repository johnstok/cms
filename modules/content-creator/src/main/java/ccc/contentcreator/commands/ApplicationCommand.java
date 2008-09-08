/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.commands;

import ccc.contentcreator.client.Application;

import com.google.gwt.user.client.Command;


/**
 * A command that has access to the application.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ApplicationCommand implements Command {

    /** _app : Application. */
    protected final Application _app;

    /**
     * Constructor.
     *
     * @param application The application used by this command.
     */
    protected ApplicationCommand(final Application application) {
        _app = application;
    }
}
