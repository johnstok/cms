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
package ccc.view.contentcreator.commands;

import ccc.view.contentcreator.client.GwtApp;

import com.google.gwt.user.client.Command;


/**
 * A command that has access to the application.
 *
 * @author Civic Computing Ltd.
 */
public abstract class ApplicationCommand implements Command {

    /** _app : GwtApp. */
    protected final GwtApp _app;

    /**
     * Constructor.
     *
     * @param application The application used by this command.
     */
    protected ApplicationCommand(final GwtApp application) {
        _app = application;
    }
}
