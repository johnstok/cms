
package ccc.contentcreator.client;


import ccc.api.UserSummary;
import ccc.contentcreator.actions.DrawMainWindowAction;
import ccc.contentcreator.actions.GetCurrentUserAction;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.LoginDialog;

import com.google.gwt.core.client.EntryPoint;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    private IGlobals _globals = new IGlobalsImpl();


    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        _globals.installUnexpectedExceptionHandler();
        _globals.securityService().isLoggedIn(
            new ErrorReportingCallback<Boolean>(
                                       _globals.userActions().internalAction()){
                public void onSuccess(final Boolean isLoggedIn) {

                    if (isLoggedIn.booleanValue()) {
                        _globals.enableExitConfirmation();
                        new GetCurrentUserAction(){
                            @Override
                            protected void execute(final UserSummary user) {
                                GLOBALS.currentUser(user);
                                new DrawMainWindowAction(user).execute();
                            }
                        }.execute();

                    } else {
                        new LoginDialog().show();
                    }
                }
            }
        );
    }
}
