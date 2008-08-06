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

    private final Constants constants = GWT.create(Constants.class);
    private final String title = constants.updateContent();
    private final Map<String, RichTextArea> richTexts =
        new HashMap<String, RichTextArea>();
    private final ResourceServiceAsync resourceService =
        (ResourceServiceAsync) GWT.create(ResourceService.class);
    private final TextBox titleTextBox = new TextBox();

    private final String contentPath;
    private JSONObject content;

    /**
     * Constructor.
     *
     * @param contentPath The absolute path to the content resource this dialog
     *          will update.
     */
    UpdateContentDialog(final String contentPath) {

        super(false, true);
        this.contentPath = contentPath;
        this.ensureDebugId("dialogBox");
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
        vPanel.ensureDebugId("vPanel");
        vPanel.add(
            new Button(
                constants.cancel(),
                new ClickListener() {
                    public void onClick(final Widget sender) {
                        hide();
                    }
                }
            )
        );

        final Button saveButton = new Button(constants.save(), new ClickListener() {
            public void onClick(final Widget arg0) {

                if (titleTextBox.getText() == null
                        || titleTextBox.getText().trim().length() == 0) {
                    titleTextBox.setStyleName("gwt-TextBox-error");
                    return;
                } else {
                    titleTextBox.setStyleName("gwt-TextBox");
                }

                Map<String, String> paragraphs = new HashMap<String, String>();
                for (String key : richTexts.keySet()) {
                    paragraphs.put(key, richTexts.get(key).getHTML());
                }

                String id = content.get("id").isString().stringValue();
                AsyncCallback<Void> callback = new AsyncCallback<Void>() {
                    public void onFailure(final Throwable arg0) {
                        GWT.log("Page saving failed", arg0);
                    }
                    public void onSuccess(final Void arg0) {
                        hide();
                    }
                };
                resourceService.saveContent(id, titleTextBox.getText()
                    , paragraphs, callback);
            }
        });
        saveButton.ensureDebugId("saveButton");
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
                    bodyRTA.ensureDebugId("bodyRTA"+key);
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
