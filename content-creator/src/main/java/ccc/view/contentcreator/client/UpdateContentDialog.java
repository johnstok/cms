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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
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
    private final Map<String, RichTextArea> richTexts = 
        new HashMap<String, RichTextArea>();
    
    final ResourceServiceAsync resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);
    
    JSONObject content;
    private TextBox titleTextBox = new TextBox();
    
    /**
     * Constructor.
     *
     * @param contentPath The absolute path to the content resource this dialog
     *          will update.
     * @param title The title for the dialog.
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
        
        Button saveButton = new Button("Save", new ClickListener() {
            public void onClick(Widget arg0) {
                Map<String,String> paragraphs = new HashMap<String, String>();
                for (String key : richTexts.keySet()) {
                    paragraphs.put(key, richTexts.get(key).getHTML());
                }
                
                String id = content.get("id").isString().stringValue();
                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    public void onFailure(Throwable arg0) {
                        GWT.log("Content saving failed", arg0);
                    }
                    public void onSuccess(Void arg0) {

                    }
            
                };
                resourceService.saveContent(id, titleTextBox.getText()
                    , paragraphs, callback );
            }
        }
        );
        vPanel.add(saveButton);
        setWidget(vPanel);

        final JSONCallback callback = new JSONCallback() {

            /**
             * {@inheritDoc}
             */
            @Override
            public void onSuccess(final JSONValue jsonResult) {

                content = jsonResult.isObject();
                final String jsonTitle =
                    content.get("title").isString().stringValue();
                final JSONObject paragraphs =
                    content.get("paragraphs").isObject();

                titleTextBox.setText(jsonTitle);
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
                    
                    richTexts.put(key, bodyRTA);
                }

                paragraphsTabPanel.selectTab(0);
            }
        };

        resourceService.getResource(contentPath, callback);
    }

}
