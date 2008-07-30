
package ccc.view.contentcreator.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ContentCreator implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        final TreeItem root = new TreeItem("root");
        populate(root);

        final Tree t = new Tree();
        t.addItem(root);
        t.addTreeListener(new TreeListener() {

            public void onTreeItemSelected(final TreeItem arg0) {

                GWT.log("Selected: " + arg0, null);
            }

            public void onTreeItemStateChanged(final TreeItem arg0) {

                GWT.log("Children displayed: " + arg0.getState(), null);
                populate(arg0);
            }

        });

        final Label label = new Label("Hello");

        final HorizontalSplitPanel hsp = new HorizontalSplitPanel();
        hsp.setSplitPosition("35%");
        hsp.setLeftWidget(t);
        hsp.setRightWidget(label);

        // TODO 30 Jul 2008 petteri: Check if Window.getClientHeight() can be 
        // replaced with css markup

        RootPanel.get().setSize("100%", 
            Window.getClientHeight()+"px");

        Window.addWindowResizeListener(new WindowResizeListener() {

            public void onWindowResized(int width, int height) {
                RootPanel.get().setSize("100%", 
                    height+"px");
            }
        });        
        RootPanel.get().add(hsp);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param parentItem
     */
    private void populate(final TreeItem parentItem) {

        final ResourceServiceAsync resourceService = (ResourceServiceAsync) GWT.create(ResourceService.class);

        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onSuccess(String result) {

                parentItem.removeItems();
                /*
                 * Bug in GWT 1.5 RC 1
                 * http://code.google.com/p/google-web-toolkit/issues/detail?id=2491
                 */ 
                DOM.setStyleAttribute(parentItem.getElement(),"paddingLeft","0px");

                JSONValue jsonResult = JSONParser.parse(result);
                JSONArray entries = jsonResult.isObject().get("entries").isArray();
                for (int i=0; i<entries.size(); i++) {
                    JSONObject entry = entries.get(i).isObject();
                    String name = entry.get("name").isString().stringValue();
                    final TreeItem item = new TreeItem(name);
                    if(entry.get("type").isString().stringValue().equals("FOLDER")) {
                        item.addItem("Loading contents...");
                    }
                    parentItem.addItem(item);
                }
            }

            public void onFailure(Throwable caught) {

                GWT.log("Error!", caught);
            }
        };

        final String absolutePath = GWTSupport.calculatePathForTreeItem(parentItem);
        resourceService.getResource(absolutePath, callback);

    }
}
