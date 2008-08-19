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
package ccc.view.contentcreator.dialogs;

import ccc.view.contentcreator.client.Constants;
import ccc.view.contentcreator.widgets.ButtonBar;
import ccc.view.contentcreator.widgets.TwoColumnForm;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Dialog for file upload.
 *
 * @author Civic Computing Ltd
 */
public class UploadFileDialog extends DialogBox {

    private final VerticalPanel _panel = new VerticalPanel();
    private final Constants _constants = GWT.create(Constants.class);
    private final TextBox _title = new TextBox();
    private final TextBox _description = new TextBox();
    private final TextBox _fileName = new TextBox();
    private final FileUpload _upload = new FileUpload();

    /**
     * Constructor.
     * @param name The name of the folder.
     * @param absolutePath The path of the folder.
     *
     */
    public UploadFileDialog(final String absolutePath, final String name) {
        super(false, true);
        // Create a FormPanel and point it at a service.
        final FormPanel form = new FormPanel();
        form.setAction("upload");

        setText(_constants.uploadFileTo()+": "+name);

        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);

        form.setWidget(_panel);

        _fileName.setName("fileName");
        _title.setName("title");
        _description.setName("description");

        _panel.add(new TwoColumnForm(3)
        .add(_constants.fileName(), _fileName)
        .add(_constants.title(), _title)
        .add(_constants.description(), _description)
        );

        _upload.setName("file");
        _panel.add(_upload);

        Hidden hiddenPath = new Hidden("path", absolutePath);
        _panel.add(hiddenPath);

        _panel.add(new ButtonBar()
            .add(
                _constants.cancel(),
                new ClickListener() {
                    public void onClick(final Widget sender) {
                        hide();
                    }})
            .add(
                _constants.upload(),
                new ClickListener() {
                    public void onClick(final Widget sender) {
                        form.submit();
                        }})
        );

        // Add an event handler to the form.
        form.addFormHandler(new FileUploadFormHandler());
        setWidget(form);
    }


    /**
     * Takes care of the validation.
     *
     * @author Civic Computing Ltd
     */
    private final class FileUploadFormHandler implements FormHandler {

        public void onSubmit(final FormSubmitEvent event) {
            StringBuffer errorText = new StringBuffer();
            if (_fileName.getText().length() == 0) {
                errorText.append(_constants.fileName());
                errorText.append("\n");
            }
            if (_description.getText().length() == 0) {
                errorText.append(_constants.description());
                errorText.append("\n");
            }
            if (_title.getText().length() == 0) {
                errorText.append(_constants.title());
                errorText.append("\n");
            }
            if (_upload.getFilename() == null
                    || _upload.getFilename().length() == 0) {
                errorText.append(_constants.file());
                errorText.append("\n");
            }
            if (errorText.length() > 0) {
                Window.alert("Following fields must not be empty: \n"
                    +errorText.toString());
                event.setCancelled(true);
            }
        }

        public void onSubmitComplete(final FormSubmitCompleteEvent event) {
            Window.alert(event.getResults());
            hide();
        }
    }
}
