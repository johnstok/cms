/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */

package ccc.view.contentcreator.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A dialog box for updating content.
 *
 * @author Civic Computing Ltd
 */
public class UpdateContentDialog extends DialogBox {

    private final String contentPath;
    private final String title;

    /**
     * Constructor.
     *
     * @param contentPath The absolute path to the content resource this dialog will update.
     */
    UpdateContentDialog(final String contentPath, final String title) {

        super(false, true);
        this.contentPath = contentPath;
        this.title = title;

        drawGUI();

    }

    /**
     * TODO: Add a description of this method.
     *
     * @param title
     */
    private void drawGUI() {

        // Set the dialog box's caption.
        setText(title);

        final VerticalPanel vPanel = new VerticalPanel();
        final TextBox titleTextBox = new TextBox();
        final TabPanel paragraphsTabPanel = new TabPanel();
        vPanel.add(titleTextBox);
        vPanel.add(paragraphsTabPanel);
        vPanel.add(
            new Button(
                "Cancel",
                new ClickListener() {
                    public void onClick(final Widget sender) {
                        hide();
                    }
                }
            )
        );
        setWidget(vPanel);

        final ResourceServiceAsync resourceService =
            (ResourceServiceAsync) GWT.create(ResourceService.class);

        final AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onSuccess(String result) {
                JSONValue jsonResult = JSONParser.parse(result);

                final String title =
                    jsonResult.isObject().get("title").isString().stringValue();
                final JSONObject paragraphs =
                    jsonResult.isObject().get("paragraphs").isObject();

                titleTextBox.setText(title);
                for (String key : paragraphs.keySet()) {
                    RichTextArea bodyRTA = new RichTextArea();
                    bodyRTA.setWidth("100%");
                    RichTextToolbar toolbar = new RichTextToolbar(bodyRTA);
                    bodyRTA.setHTML(
                        paragraphs
                            .get(key).isObject()
                            .get("body").isString().stringValue());
                    final VerticalPanel rtPanel = new VerticalPanel();
                    rtPanel.add(toolbar);
                    rtPanel.add(bodyRTA);
                    paragraphsTabPanel.add(rtPanel, key);
                }

                paragraphsTabPanel.selectTab(0);
            }

            public void onFailure(Throwable caught) {
                GWT.log("Error!", caught);
            }
        };

        resourceService.getResource(contentPath, callback);
    }

}
