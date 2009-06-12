
package ccc.contentcreator.client;

import java.util.Collection;

import ccc.api.ResourceSummary;
import ccc.api.UserSummary;
import ccc.contentcreator.api.ActionNameConstants;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.LoginDialog;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {
    private static final ActionNameConstants USER_ACTIONS =
        GWT.create(ActionNameConstants.class);

    private final QueriesServiceAsync _qs = GWT.create(QueriesService.class);

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Globals.installUnexpectedExceptionHandler();
        Globals.securityService().isLoggedIn(
            new ErrorReportingCallback<Boolean>(USER_ACTIONS.internalAction()){
                public void onSuccess(final Boolean isLoggedIn) {
                    if (isLoggedIn) {
                        drawMainWindow();
                    } else {
                        new LoginDialog().show();
                    }
                }
            }
        );
    }

    /**
     * Draws the main window based on user roles.
     *
     */
    public void drawMainWindow() {
        Globals.enableExitConfirmation();
        _qs.loggedInUser(new ErrorReportingCallback<UserSummary>(
            USER_ACTIONS.internalAction()){
            public void onSuccess(final UserSummary user) {
                Globals.currentUser(user);
                renderUI(user);

            };
        });
    }

    private void renderUI(final UserSummary user) {

        final QueriesServiceAsync qs = GWT.create(QueriesService.class);
        qs.roots(new ErrorReportingCallback<Collection<ResourceSummary>>(
            USER_ACTIONS.internalAction()) {
            // TODO: refactor
            public void onSuccess(final Collection<ResourceSummary> arg0) {
                final LeftRightPane contentPane = new LeftRightPane();
                contentPane.setRightHandPane(new ContentPanel());
                contentPane.setLeftHandPane(
                    new ResourceNavigator(contentPane,
                        arg0,
                        user));

                final Viewport vp =
                    layoutMainWindow(new MainMenu(user), contentPane);

                RootPanel.get().add(vp);
            }
        });
    }

    /**
     * Lay out the GUI components of the main window.
     *
     * @param menu The tool-bar for the main menu.
     * @param content The layout container for the content panel.
     * @return The layout as a GXT view-port for rendering by the browser.
     */
    private Viewport layoutMainWindow(final ToolBar menu,
                              final LayoutContainer content) {

        final LayoutContainer vp = new LayoutContainer();
         vp.setLayout(new RowLayout());
         vp.add(menu, new RowData(1, -1));
         vp.add(content, new RowData(1, 1));

         final Viewport v = new Viewport();
         v.setLayout(new FitLayout());
         v.add(vp);

        return v;
    }
}
