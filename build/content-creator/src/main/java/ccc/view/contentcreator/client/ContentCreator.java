
package ccc.view.contentcreator.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
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
        hsp.setSplitPosition("50%");
        hsp.setLeftWidget(t);
        hsp.setRightWidget(label);

        RootPanel.get().setSize("800px", "800px");
        RootPanel.get().add(hsp);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param root
     */
    private void populate(final TreeItem root) {

        final ResourceServiceAsync resourceService = (ResourceServiceAsync) GWT.create(ResourceService.class);

        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onSuccess(String result) {

                root.removeItems();
                JSONValue jsonResult = JSONParser.parse(result);
                JSONArray entries = jsonResult.isObject().get("entries").isArray();
                for (int i=0; i<entries.size(); i++) {
                    JSONObject entry = entries.get(i).isObject();
                    String name = entry.get("name").isString().stringValue();
                    final TreeItem item = new TreeItem(name);
                    if(entry.get("type").isString().stringValue().equals("FOLDER")) {
                        item.addItem("Loading contents...");
                    }
                    root.addItem(item);
                }
            }

            public void onFailure(Throwable caught) {

                GWT.log("Error!", caught);
            }
        };

        final String absolutePath = GWTSupport.calculatePathForTreeItem(root);
        resourceService.getResource(absolutePath, callback);

    }
}
