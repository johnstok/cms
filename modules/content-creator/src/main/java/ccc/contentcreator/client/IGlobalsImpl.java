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
import ccc.contentcreator.api.ActionStatusConstants;
import ccc.contentcreator.api.CommandService;
import ccc.contentcreator.api.CommandServiceAsync;
import ccc.contentcreator.api.CommandTypeConstants;
import ccc.contentcreator.api.ErrorDescriptions;
import ccc.contentcreator.api.ErrorResolutions;
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

    private static final CommandServiceAsync COMMAND_SERVICE =
        GWT.create(CommandService.class);
    private static final QueriesServiceAsync QUERIES_SERVICE =
        GWT.create(QueriesService.class);
    private static final SecurityServiceAsync SECURITY_SERVICE =
        GWT.create(SecurityService.class);
    private static final UIConstants UI_CONSTANTS =
        GWT.create(UIConstants.class);
    private static final UIMessages UI_MESSAGES =
        GWT.create(UIMessages.class);
    private static final ActionStatusConstants ACTION_STATUSES =
        GWT.create(ActionStatusConstants.class);
    private static final CommandTypeConstants COMMAND_TYPES =
        GWT.create(CommandTypeConstants.class);
    private static final ErrorDescriptions ERROR_DESCRIPTIONS =
        GWT.create(ErrorDescriptions.class);
    private static final ErrorResolutions ERROR_RESOLUTIONS =
        GWT.create(ErrorResolutions.class);
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);
    private static final boolean ENABLE_EXIT_CONFIRMATION =
        (null == Window.Location.getParameter("dec"));

    private HandlerRegistration _handlerRegistration = null;


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
        return COMMAND_SERVICE;
    }

    /** {@inheritDoc} */
    @Override
    public UserSummary currentUser() {
        return UserStore.currentUser();
    }

    /** {@inheritDoc} */
    @Override
    public void currentUser(final UserSummary user) {
        UserStore.currentUser(user);
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
        return QUERIES_SERVICE;
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
        return SECURITY_SERVICE;
    }

    /** {@inheritDoc} */
    @Override
    public UIConstants uiConstants() {
       return UI_CONSTANTS;
    }

    /** {@inheritDoc} */
    @Override
    public UIMessages uiMessages() {
        return UI_MESSAGES;
    }

    /** {@inheritDoc} */
    @Override
    public void unexpectedError(final Throwable e, final String action) {
        // TODO: Add clause for InvocationException
        // TODO: Add clause for IncompatibleRemoteServiceException
        if (e instanceof CommandFailedException) {
            final CommandFailedException re = (CommandFailedException) e;
            new ErrorDialog(re, action, this).show();
        } else if (e instanceof NullPointerException) {
            new ErrorDialog(e, action, this).show();
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
                new ErrorDialog(e, action, this).show();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public ActionNameConstants userActions() {
        return USER_ACTIONS;
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

    /**
     * User Summary shared by all instances of {@link IGlobalsImpl}.
     *
     * @author Civic Computing Ltd.
     */
    private static final class UserStore {
        private static UserSummary _user;

        private UserStore() {
            // no-op
        }

        public static UserSummary currentUser() {
            return _user;
        }

        public static void currentUser(final UserSummary user) {
            _user = user;
        }
    }

    /** {@inheritDoc} */
    @Override
    public ActionStatusConstants actionStatusConstants() {
        return ACTION_STATUSES;
    }

    /** {@inheritDoc} */
    @Override
    public CommandTypeConstants commandTypeConstants() {
        return COMMAND_TYPES;
    }

    /** {@inheritDoc} */
    @Override
    public ErrorDescriptions errorDescriptions() {
        return ERROR_DESCRIPTIONS;
    }

    /** {@inheritDoc} */
    @Override
    public ErrorResolutions errorResolutions() {
        return ERROR_RESOLUTIONS;
    }

}
