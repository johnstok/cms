
package ccc.view.contentcreator.client;

import java.util.ArrayList;

import ccc.view.contentcreator.commands.CreateDisplayTemplateCommand;
import ccc.view.contentcreator.dialogs.UpdateContentDialog;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ContentCreator implements EntryPoint {

    private final Constants constants = GWT.create(Constants.class);

    private HorizontalSplitPanel hsp = new HorizontalSplitPanel();

    /** MENUBAR_HEIGHT : Reserved space for menu bar. */
    private static final int MENUBAR_HEIGHT = 30;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        final VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.setWidth("100%");
        mainPanel.setHeight("100%");

        final TreeItem root = new TreeItem("root");
        root.ensureDebugId("root_treeitem");
        populate(root);

        final Tree t = new Tree() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onBrowserEvent(final Event event) {
                // Handle double-clicks
                if (DOM.eventGetType(event) == Event.ONDBLCLICK) {
                    Element e = DOM.eventGetTarget(event);

                    ArrayList<Element> chain = new ArrayList<Element>();
                    collectElementChain(chain, getElement(), e);

                    TreeItem item = findItemByChain(chain, 0, root);
                    item.setState(!item.getState(), true);
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };

        t.sinkEvents(Event.ONDBLCLICK);
        t.addItem(root);
        t.ensureDebugId("folder_tree");
        t.addTreeListener(new TreeListener() {
            public void onTreeItemSelected(final TreeItem arg0) {

                GWT.log("Selected: " + arg0, null);
                displayChildrenInRightHandPane(arg0);
            }

            public void onTreeItemStateChanged(final TreeItem arg0) {

                GWT.log("Children displayed: " + arg0.getState(), null);
                populate(arg0);
            }
        });

        final Label label = new Label("Hello");
        hsp.setSize("100%", (Window.getClientHeight() - MENUBAR_HEIGHT) + "px");
        hsp.setSplitPosition("35%");
        hsp.setLeftWidget(t);
        hsp.setRightWidget(label);

        Window.addWindowResizeListener(new WindowResizeListener() {

            public void onWindowResized(final int width, final int height) {
                RootPanel.get().setSize("100%",
                    height + "px");
                hsp.setSize("100%", (height - MENUBAR_HEIGHT) + "px");
            }
        });

        final Command manualCmd = new Command() {
            public void execute() {
              Window.open("manual/ContentCreatorManual.html",
                  "_blank", "resizable=yes,scrollbars=yes,status=no");
            }
          };

        final MenuBar menu = new MenuBar();

        final MenuBar helpMenu = new MenuBar(true);
        helpMenu.addItem(constants.manual(), manualCmd);
        menu.addItem(constants.help(), helpMenu);

        final MenuBar assetsMenu = new MenuBar(true);
        assetsMenu.addItem(
            constants.createDisplayTemplate(),
            new CreateDisplayTemplateCommand());
        menu.addItem(constants.assets(), assetsMenu);

        mainPanel.add(menu);
        mainPanel.add(hsp);

        RootPanel.get().add(mainPanel);
    }

    /**
     * Display the specified tree item's children in the right hand pane.
     *
     * @param item The tree item to display.
     */
    protected void displayChildrenInRightHandPane(final TreeItem item) {

        final String absolutePath = GWTSupport.calculatePathForTreeItem(item);

        final ResourceServiceAsync resourceService =
            (ResourceServiceAsync) GWT.create(ResourceService.class);

        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onSuccess(final String result) {

                JSONValue jsonResult = JSONParser.parse(result);
                JSONArray entries =
                    jsonResult.isObject().get("entries").isArray();

                final Grid children = new Grid(entries.size()+1, 3);
                children.ensureDebugId("children_grid");
                children.setWidth("100%");
                children.setText(0, 0, constants.type());
                children.setText(0, 1, constants.title());
                children.setText(0, 2, constants.actions());

                for (int i = 0; i < entries.size(); i++) {
                    JSONObject entry = entries.get(i).isObject();

                    final String name =
                        entry.get("name").isString().stringValue();
                    String type = entry.get("type").isString().stringValue();

                    children.setText(i+1, 0, type);
                    children.setText(i+1, 1, name);
                    if (type.equals("PAGE")) {
                        children.setWidget(
                            i+1,
                            2,
                            new Button(
                                constants.edit(),
                                new ClickListener() {
                                    public void onClick(final Widget sender) {
                                      new UpdateContentDialog(
                                          absolutePath+name+"/")
                                          .show();
                                    }
                                }
                            )
                        );

                    } else {
                        children.setText(i+1, 2, "");
                    }
                }

                hsp.setRightWidget(children);
            }

            public void onFailure(final Throwable caught) {
                GWT.log("Error!", caught);
            }
        };

        resourceService.getResource(absolutePath, callback);
    }

    /**
     * Populates child tree items for given parentItem.
     *
     * @param parentItem
     */
    private void populate(final TreeItem parentItem) {

        final ResourceServiceAsync resourceService =
            (ResourceServiceAsync) GWT.create(ResourceService.class);

        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onSuccess(final String result) {

                parentItem.removeItems();
                /*
                 * Bug in GWT 1.5 RC 1
                 * http://code.google.com/p/google-web-toolkit/issues/detail?id=2491
                 */
                DOM.setStyleAttribute(
                    parentItem.getElement(),
                    "paddingLeft","0px");

                JSONValue jsonResult = JSONParser.parse(result);
                JSONArray entries =
                    jsonResult.isObject().get("entries").isArray();
                for (int i=0; i<entries.size(); i++) {
                    JSONObject entry = entries.get(i).isObject();
                    if(entry.get("type").isString().stringValue().equals("FOLDER")) {
                        String name = entry.get("name").isString().stringValue();
                        final TreeItem item = new TreeItem(name);
                        item.ensureDebugId(name);
                        if(new Integer(entry.get("folder-count").isString().stringValue())>0) {
                            item.addItem("Loading contents...");
                        }
                        parentItem.addItem(item);
                    }
                }
            }

            public void onFailure(final Throwable caught) {

                GWT.log("Error!", caught);
            }
        };

        final String absolutePath =
            GWTSupport.calculatePathForTreeItem(parentItem);
        resourceService.getResource(absolutePath, callback);

    }

    private void collectElementChain(final ArrayList<Element> chain,
                                     final Element hRoot,
                                     final Element hElem) {
        if ((hElem == null) || (hElem == hRoot)) {
            return;
        }

        collectElementChain(chain, hRoot, DOM.getParent(hElem));
        chain.add(hElem);
    }

    private TreeItem findItemByChain(final ArrayList<Element> chain,
                                     final int idx,
                                     final TreeItem root) {
        if (idx == chain.size()) {
            return root;
        }

        final Element hCurElem = chain.get(idx);
        for (int i = 0, n = root.getChildCount(); i < n; ++i) {
            final TreeItem child = root.getChild(i);
            if (child.getElement() == hCurElem) {
                final TreeItem retItem =
                    findItemByChain(chain, idx + 1, root.getChild(i));
                if (retItem == null) {
                    return child;
                }
                return retItem;
            }
        }

        return findItemByChain(chain, idx + 1, root);
    }
}
