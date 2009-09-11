package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.client.LeftRightPane;
import ccc.contentcreator.client.MainMenu;
import ccc.contentcreator.client.ResourceNavigator;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class DrawMainWindowAction
    extends
        GetRootsAction {

    /** _user : UserSummary. */
    private final UserDto _user;

    /**
     * Constructor.
     *
     * @param user The currently logged in user.
     */
    public DrawMainWindowAction(final UserDto user) {
        _user = user;
    }

    /** {@inheritDoc} */
    @Override
    protected void onSuccess(final Collection<ResourceSummary> arg0) {
        final LeftRightPane contentPane = new LeftRightPane();
        contentPane.setRightHandPane(new ContentPanel());
        contentPane.setLeftHandPane(
            new ResourceNavigator(contentPane,
                arg0,
                _user));

        final Viewport vp =
            layoutMainWindow(new MainMenu(_user), contentPane);

        RootPanel.get().add(vp);
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
