
package ccc.contentcreator.client;

import java.util.Collection;

import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        Globals.enableExitConfirmation();

        final QueriesServiceAsync qs = GWT.create(QueriesService.class);

        qs.roots(new AsyncCallback<Collection<ResourceSummary>>(){

            public void onFailure(final Throwable arg0) {
                Globals.unexpectedError(arg0);
            }

            public void onSuccess(final Collection<ResourceSummary> arg0) {
                final LeftRightPane contentPane = new LeftRightPane();
                contentPane.setLeftHandPane(
                    new ResourceNavigator(contentPane,
                                          arg0));
                contentPane.setRightHandPane(new ContentPanel());

                final Viewport vp =
                    layoutMainWindow(
                        new MainMenu(),
                        contentPane);

                RootPanel.get().add(vp);
            }});

    }

    /**
     * Lay out the GUI components of the main window.
     *
     * @param menu The tool-bar for the main menu.
     * @param content The layout container for the content panel.
     * @return The layout as a GXT view-port for rendering by the browser.
     */
    Viewport layoutMainWindow(final ToolBar menu,
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
