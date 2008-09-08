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
package ccc.contentcreator.dialogs;

import ccc.contentcreator.callbacks.DisposingClickListener;
import ccc.contentcreator.controls.Application;
import ccc.contentcreator.controls.Constants;
import ccc.contentcreator.controls.FileControl;
import ccc.contentcreator.controls.PanelControl;
import ccc.contentcreator.controls.StringControl;
import ccc.contentcreator.widgets.ButtonBar;
import ccc.contentcreator.widgets.TwoColumnForm;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.Widget;


/**
 * Dialog for file upload.
 *
 * @author Civic Computing Ltd
 */
public class UploadFileDialog {

    private final ApplicationDialog     _delegate;
    private final Application        _app;
    private final Constants     _constants;
    private final PanelControl  _gui;

    private final StringControl _title;
    private final StringControl _description;
    private final StringControl _fileName;
    private final FileControl   _upload;
    private final StringControl _path;

    /**
     * Constructor.
     * @param name The name of the folder.
     * @param absolutePath The path of the folder.
     *
     */
    public UploadFileDialog(final Application app,
                            final String absolutePath,
                            final String name) {

        _app = app;
        _constants = _app.constants();
        _delegate = _app.dialog(_constants.uploadFileTo()+": "+name);
        _gui = _app.verticalPanel();

        _title = _app.textBox();
        _description = _app.textBox();
        _fileName = _app.textBox();
        _upload = _app.fileUpload();
        _path = _app.hidden();

        // Create a FormPanel and point it at a service.
        final PanelControl form =
            _app.formPanel("upload",
                           FormPanel.ENCODING_MULTIPART,
                           FormPanel.METHOD_POST,
                           new FileUploadFormHandler(),
                           _gui);

        _fileName.setName("fileName");
        _title.setName("title");
        _description.setName("description");
        _upload.setName("file");
        _path.setName("path");

        _path.model(absolutePath);

        _gui.add(
            new TwoColumnForm(_app, 4)
                .add(_constants.fileName(), _fileName)
                .add(_constants.title(), _title)
                .add(_constants.description(), _description)
                .add(_constants.localFile(), _upload)
            );
        _gui.add(_path);
        _gui.add(new ButtonBar(_app)
            .add(
                _constants.cancel(),
                new DisposingClickListener(_delegate))
            .add(
                _constants.upload(),
                new ClickListener() {
                    public void onClick(final Widget sender) {
                        form.submit();
                        }})
        );

        form.add(_gui);
        _delegate.gui(form);
    }


    /**
     * Takes care of the validation.
     *
     * @author Civic Computing Ltd
     */
    private final class FileUploadFormHandler implements FormHandler {

        public void onSubmit(final FormSubmitEvent event) {
            final StringBuffer errorText = new StringBuffer();
            if (_fileName.model().length() == 0) {
                errorText.append(_constants.fileName());
                errorText.append("\n");
            }
            if (_description.model().length() == 0) {
                errorText.append(_constants.description());
                errorText.append("\n");
            }
            if (_title.model().length() == 0) {
                errorText.append(_constants.title());
                errorText.append("\n");
            }
            if (_upload.getFilename() == null
                    || _upload.getFilename().length() == 0) {
                errorText.append(_constants.file());
                errorText.append("\n");
            }
            if (errorText.length() > 0) {
                _app.alert("Following fields must not be empty: \n"
                    +errorText.toString());
                event.setCancelled(true);
            }
        }

        public void onSubmitComplete(final FormSubmitCompleteEvent event) {
            _app.alert(event.getResults());
            _delegate.hide();
        }
    }


    /**
     * TODO: Add a description of this method.
     *
     */
    public void center() {
        _delegate.center();
    }
}
