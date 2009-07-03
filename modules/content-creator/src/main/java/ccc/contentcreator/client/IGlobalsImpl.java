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
import ccc.contentcreator.api.SecurityService;
import ccc.contentcreator.api.SecurityServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.api.UIMessages;
import ccc.contentcreator.dialogs.ErrorDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;


/**
 * {@link IGlobals} implementation.
 *
 * @author Civic Computing Ltd.
 */
public class IGlobalsImpl
    implements
        IGlobals {

    private HandlerRegistration _handlerRegistration = null;

    private static final boolean ENABLE_EXIT_CONFIRMATION =
        (null == Window.Location.getParameter("dec"));

    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);

    private static UserSummary _user;


    /** {@inheritDoc} */
    @Override
    public void alert(final String string) {
        Window.alert(string);
    }

    /** {@inheritDoc} */
    @Override
    public String apiURL() {
        return GWT.getHostPageBaseURL()+API_URL;
    }

    /** {@inheritDoc} */
    @Override
    public String appURL() {
        return GWT.getHostPageBaseURL();
    }

    /** {@inheritDoc} */
    @Override
    public CommandServiceAsync commandService() {
        return GWT.create(CommandService.class);
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary currentUser() {
        return _user;
    }

    /** {@inheritDoc} */
    @Override
    public void currentUser(final UserSummary user) {
        _user = user;
    }

    /** {@inheritDoc} */
    @Override
    public void disableExitConfirmation() {
        if (_handlerRegistration != null) {
            _handlerRegistration.removeHandler();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void enableExitConfirmation() {
        if (ENABLE_EXIT_CONFIRMATION) {
            _handlerRegistration =
                Window.addWindowClosingHandler(new ExitHandler());
        }
    }

    /** {@inheritDoc} */
    @Override
    public String hostURL() {
        return GWT.getHostPageBaseURL()
        .substring(
            0,
            GWT.getHostPageBaseURL()
               .lastIndexOf(APP_URL));
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
        redirect(hostURL()+relativeURL);
    }

    /** {@inheritDoc} */
    @Override
    public void refresh() {
        Window.Location.reload();
    }

    /** {@inheritDoc} */
    @Override
    public SecurityServiceAsync securityService() {
        return GWT.create(SecurityService.class);
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

    /**
     * Handler for window closing.
     *
     * @author Civic Computing Ltd.
     */
    private class ExitHandler implements ClosingHandler {

        /** {@inheritDoc} */
        @Override
        public void onWindowClosing(final ClosingEvent event) {
            event.setMessage(uiConstants().exitWarning());
        }
    }

    private static void redirect(final String url) {
        Window.Location.assign(url);
    }

}
