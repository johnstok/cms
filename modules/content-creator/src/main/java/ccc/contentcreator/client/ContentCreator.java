
package ccc.contentcreator.client;

import ccc.contentcreator.api.Application;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    private Application _app = new GwtApplication();

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        final LeftRightPane contentPane = new LeftRightPane();
        contentPane.setLeftHandPane(
            new ResourceNavigator(contentPane, _app.lookupService()));
        contentPane.setRightHandPane(new ContentPanel());
        // TODO: Add 'Welcome panel to right hand pane?

         final Viewport vp =
             layoutMainWindow(
                 new MainMenu(_app),
                 contentPane);

        RootPanel.get().add(vp);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param menu
     * @param content
     * @return
     */
    private Viewport layoutMainWindow(final ToolBar menu,
                                      final LayoutContainer content) {

        final LayoutContainer vp = new LayoutContainer();
         vp.setLayout(new RowLayout());
         vp.add(menu, new RowData(1,-1));
         vp.add(content, new RowData(1,1));

         final Viewport v = new Viewport();
         v.setLayout(new FitLayout());
         v.add(vp);

        return v;
    }
}
