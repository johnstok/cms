
package ccc.contentcreator.client;

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

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

//        Globals.enableExitConfirmation();

        final LeftRightPane contentPane = new LeftRightPane();
        contentPane.setLeftHandPane(
            new ResourceNavigator(contentPane, Globals.resourceService()));
        contentPane.setRightHandPane(new ContentPanel());

         final Viewport vp =
             layoutMainWindow(
                 new MainMenu(),
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
         vp.add(menu, new RowData(1, -1));
         vp.add(content, new RowData(1, 1));

         final Viewport v = new Viewport();
         v.setLayout(new FitLayout());
         v.add(vp);

        return v;
    }
}
