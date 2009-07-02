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

import ccc.api.CommandFailedException;
import ccc.api.UserSummary;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.CommandService;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.SecurityServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.dialogs.ErrorDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;


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
        Window.alert(string);
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
        return GWT.create(CommandService.class);
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
        Window.removeWindowCloseListener(CLOSE_LISTENER);
    }

    /** {@inheritDoc} */
    @Override
    public void enableExitConfirmation() {
        if (ENABLE_EXIT_CONFIRMATION) {
            Window.addWindowCloseListener(CLOSE_LISTENER);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String hostURL() {
        return Globals.hostURL();
    }

    /** {@inheritDoc} */
    @Override
    public void installUnexpectedExceptionHandler() {
        GWT.setUncaughtExceptionHandler(
            new UncaughtExceptionHandler(){
                public void onUncaughtException(final Throwable e) {
                    unexpectedError(e, USER_ACTIONS.unknownAction());
                }
            }
        );
    }

    /** {@inheritDoc} */
    @Override
    public QueriesServiceAsync queriesService() {
        return GWT.create(QueriesService.class);
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
       return GWT.create(UIConstants.class);
    }

    /** {@inheritDoc} */
    @Override
    public UIMessages uiMessages() {
        return GWT.create(UIMessages.class);
    }

    /** {@inheritDoc} */
    @Override
    public void unexpectedError(final Throwable e, final String action) {
     // TODO: Add clause for InvocationException
        // TODO: Add clause for IncompatibleRemoteServiceException
        if (e instanceof CommandFailedException) {
            final CommandFailedException re = (CommandFailedException) e;
            new ErrorDialog(re, action).show();
        } else if (e instanceof NullPointerException) {
            new ErrorDialog(e, action).show();
            GWT.log("An unexpected error occured.", e);
        } else {
            final String errorMesssage = e.getMessage();
            final String causeMessage =
                (null==e.getCause()) ? "" : e.getCause().getMessage();
            if (errorMesssage.startsWith("<!-- LOGIN_REQUIRED -->")){
                alert(uiConstants().sessionTimeOutPleaseRestart());
            } else if (causeMessage.startsWith("<!-- LOGIN_REQUIRED -->")) {
                alert(uiConstants().sessionTimeOutPleaseRestart());
            } else {
                GWT.log("An unexpected error occured.", e);
                new ErrorDialog(e, action).show();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public ActionNameConstants userActions() {
        return GWT.create(ActionNameConstants.class);
    }

    private static final WindowCloseListener CLOSE_LISTENER =
        new WindowCloseListener(){

        public void onWindowClosed() { /* No Op */ }

        public String onWindowClosing() {
            return new IGlobalsImpl().uiConstants().exitWarning();
        }
    };

    private static final boolean ENABLE_EXIT_CONFIRMATION =
        (null == Window.Location.getParameter("dec"));

    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
}
