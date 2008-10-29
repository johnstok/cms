/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.client.dialogs;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.ButtonBar;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.TwoColumnForm;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Dialog for file upload.
 *
 * @author Civic Computing Ltd.
 */
public class UploadFileDialog extends DialogBox {

    private final UIConstants       _constants = Globals.uiConstants();
    private final Panel      _gui = new VerticalPanel();

    private final TextBox     _title;
    private final TextBox     _description;
    private final TextBox     _fileName;
    private final FileUpload       _upload;
    private final Hidden     _path;
    private final Tree              _tree;

    /**
     * Constructor.
     *
     * @param folder The folder in which this file should be saved.
     * @param name The name of the folder.
     * @param tree The left hand tree view in the main window.
     */
    public UploadFileDialog(final String folder,
                            final String name,
                            final Tree tree) {

        setText(_constants.uploadFileTo()+": "+name);

        _tree = tree;

        _title = new TextBox();
        _description = new TextBox();
        _fileName = new TextBox();
        _upload = new FileUpload();
        _path = new Hidden();

        // Create a FormPanel and point it at a service.
        final FormPanel form = new FormPanel();
        form.setWidget(_gui);
        form.setAction("upload");
        form.setEncoding(FormPanel.ENCODING_MULTIPART);
        form.setMethod(FormPanel.METHOD_POST);
        form.addFormHandler(new FileUploadFormHandler());

        _fileName.setName("fileName");
        _title.setName("title");
        _description.setName("description");
        _upload.setName("file");
        _path.setName("path");

        _path.setValue(folder);

        _gui.add(
            new TwoColumnForm(4)
                .add(_constants.fileName(), _fileName)
                .add(_constants.title(), _title)
                .add(_constants.description(), _description)
                .add(_constants.localFile(), _upload)
            );
        _gui.add(_path);
        _gui.add(new ButtonBar()
            .add(
                _constants.cancel(),
                new ClickListener(){
                    public void onClick(final Widget sender) {
                        hide();
                    }
                }
            )
            .add(
                _constants.upload(),
                new ClickListener() {
                    public void onClick(final Widget sender) {
                        form.submit();
                    }
                }
            )
        );

        add(form);
    }


    /**
     * Takes care of the validation.
     *
     * @author Civic Computing Ltd
     */
    private final class FileUploadFormHandler implements FormHandler {

        public void onSubmit(final FormSubmitEvent event) {
            final StringBuffer errorText = new StringBuffer();
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
                Globals.alert("Following fields must not be empty: \n"
                    +errorText.toString());
                event.setCancelled(true);
            }
        }

        public void onSubmitComplete(final FormSubmitCompleteEvent event) {
            Globals.alert(event.getResults());
            _tree.fireEvent(Events.SelectionChange);
            hide();
        }
    }
}
