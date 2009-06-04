/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client;

import ccc.api.UserSummary;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.SecurityServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;

import com.google.gwt.core.client.GWT;


/**
 * {@link IGlobals} implementation that delegates to the deprecated
 * {@link Globals} class.
 *
 * @author Civic Computing Ltd.
 */
public class IGlobalsImpl
    implements
        IGlobals {

    /** {@inheritDoc} */
    @Override
    public void alert(final String string) {
        Globals.alert(string);
    }

    /** {@inheritDoc} */
    @Override
    public String apiURL() {
        return Globals.apiURL();
    }

    /** {@inheritDoc} */
    @Override
    public String appURL() {
        return Globals.appURL();
    }

    /** {@inheritDoc} */
    @Override
    public CommandServiceAsync commandService() {
        return Globals.commandService();
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary currentUser() {
        return Globals.currentUser();
    }

    /** {@inheritDoc} */
    @Override
    public void currentUser(final UserSummary user) {
        Globals.currentUser(user);
    }

    /** {@inheritDoc} */
    @Override
    public void disableExitConfirmation() {
        Globals.disableExitConfirmation();
    }

    /** {@inheritDoc} */
    @Override
    public void enableExitConfirmation() {
        Globals.enableExitConfirmation();
    }

    /** {@inheritDoc} */
    @Override
    public String hostURL() {
        return Globals.hostURL();
    }

    /** {@inheritDoc} */
    @Override
    public void installUnexpectedExceptionHandler() {
        Globals.installUnexpectedExceptionHandler();
    }

    /** {@inheritDoc} */
    @Override
    public QueriesServiceAsync queriesService() {
        return Globals.queriesService();
    }

    /** {@inheritDoc} */
    @Override
    public void redirectTo(final String relativeURL) {
        Globals.redirectTo(relativeURL);
    }

    /** {@inheritDoc} */
    @Override
    public void refresh() {
        Globals.refresh();
    }

    /** {@inheritDoc} */
    @Override
    public SecurityServiceAsync securityService() {
        return Globals.securityService();
    }

    /** {@inheritDoc} */
    @Override
    public UIConstants uiConstants() {
        return Globals.uiConstants();
    }

    /** {@inheritDoc} */
    @Override
    public UIMessages uiMessages() {
        return Globals.uiMessages();
    }

    /** {@inheritDoc} */
    @Override
    public void unexpectedError(final Throwable e, final String action) {
        Globals.unexpectedError(e, action);
    }

    /** {@inheritDoc} */
    @Override
    public ActionNameConstants userActions() {
        return GWT.create(ActionNameConstants.class);
    }
}
