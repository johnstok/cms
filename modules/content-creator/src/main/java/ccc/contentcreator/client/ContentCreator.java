
package ccc.contentcreator.client;

import java.util.List;

import ccc.contentcreator.dialogs.CreateContentTemplateDialog;
import ccc.contentcreator.dialogs.GXTResourceExplorerPanel;
import ccc.contentcreator.dialogs.UpdateOptionsDialog;
import ccc.contentcreator.dto.DTO;
import ccc.contentcreator.dto.OptionDTO;

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

         final Viewport vp =
             createMainWindow(
                 createMainMenu(),
                 new GXTResourceExplorerPanel(_app).view());

        RootPanel.get().add(vp);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    private ToolBar createMainMenu() {

        final TextToolItem help = new TextToolItem(_app.constants().help());
        final Menu helpMenu = new Menu();
        help.setMenu(helpMenu);
        final MenuItem openManual = new MenuItem();
        openManual.setText(_app.constants().manual());
        openManual.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                Window.open("manual/ContentCreatorManual.html",
                  "_blank", "resizable=yes,scrollbars=yes,status=no");
            }
        });
        helpMenu.add(openManual);

        final TextToolItem assets = new TextToolItem(_app.constants().assets());
        final Menu assetsMenu = new Menu();
        assets.setMenu(assetsMenu);
        final MenuItem createTemplate = new MenuItem();
        createTemplate.setText(_app.constants().createDisplayTemplate());
        createTemplate.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                new CreateContentTemplateDialog(_app).center();
            }
        });
        assetsMenu.add(createTemplate);

        final TextToolItem tools = new TextToolItem(_app.constants().tools());
        final Menu toolsMenu = new Menu();
        tools.setMenu(toolsMenu);

        final MenuItem updateOptions = new MenuItem();
        updateOptions.setText(_app.constants().options());
        updateOptions.addSelectionListener(new SelectionListener<MenuEvent>() {
            @Override
            public void componentSelected(final MenuEvent ce) {
                final ResourceServiceAsync resourceService =
                    _app.lookupService();

                resourceService.listOptions(
                 new AsyncCallback<List<OptionDTO<? extends DTO>>>(){

                     public void onFailure(final Throwable arg0) {
                         Window.alert(_app.constants().error());
                     }

                     public void onSuccess(
                                final List<OptionDTO<? extends DTO>> options) {
                         new UpdateOptionsDialog(_app, options).center();
                     }});
            }
        });
        toolsMenu.add(updateOptions);

        final ToolBar menu = new ToolBar();
        menu.add(assets);
        menu.add(tools);
        menu.add(help);

        return menu;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param menu
     * @param content
     * @return
     */
    private Viewport createMainWindow(final ToolBar menu,
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

//                    if (type.equals("PAGE")) {
//                        children.setWidget(
//                            i+1,
//                            2,
//                            new ButtonBar(new GwtApplication())
//                            .add(constants.edit(),
//                                new ClickListener() {
//                                    public void onClick(final Widget sender) {
//                                      new UpdateContentDialog(
//                                          absolutePath+name+"/")
//                                          .center();
//                                    }
//                                }
//                            )
//                            .add(constants.preview(),
//                                new ClickListener() {
//                                    public void onClick(final Widget sender) {
//                                      new PreviewContentDialog(new GwtApplication(),
//                                          absolutePath+name+"/")
//                                          .center();
//                                    }
//                                }
//                            )
//                        );
//
//                    } else {
//                        children.setWidget(
//                            i+1,
//                            2,
//                            new ButtonBar(new GwtApplication())
//                            .add(constants.preview(),
//                                new ClickListener() {
//                                    public void onClick(final Widget sender) {
//                                      new PreviewContentDialog(new GwtApplication(),
//                                          absolutePath+name+"/")
//                                          .center();
//                                    }
//                                }
//                            )
//                        );
//                    }
//                }
}
