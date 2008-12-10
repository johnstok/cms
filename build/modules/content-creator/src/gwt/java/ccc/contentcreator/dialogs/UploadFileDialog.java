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
package ccc.contentcreator.dialogs;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTable;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for file upload.
 *
 * @author Civic Computing Ltd.
 */
public class UploadFileDialog extends AbstractBaseDialog {

    private final UIConstants       _constants = Globals.uiConstants();

    private final TextField<String> _fileName = new TextField<String>();
    private final TextField<String> _title = new TextField<String>();
    private final TextField<String> _description = new TextField<String>();
    private final HiddenField<String> _path = new HiddenField<String>();
    private FileUploadField _file = new FileUploadField();
    private final FormPanel _form = new FormPanel();


    /**
     * Constructor.
     *
     * @param folder The folder in which this file should be saved.
     * @param name The name of the folder.
     * @param rt The left hand tree view in the main window.
     */
    public UploadFileDialog(final String folder,
                            final String name,
                            final ResourceTable rt) {
        super(Globals.uiConstants().uploadFileTo()+": "+name);

        // Create a FormPanel and point it at a service.
        _form.setAction("upload");
        _form.setEncoding(FormPanel.Encoding.MULTIPART);
        _form.setMethod(FormPanel.Method.POST);
        _form.setHeaderVisible(false);

        _fileName.setName("fileName");
        _fileName.setFieldLabel(_constants.fileName());
        _fileName.setAllowBlank(false);

        _title.setName("title");
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);

        _description.setName("description");
        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);

        _file.setName("file");
        _file.setFieldLabel(_constants.localFile());
        _file.setAllowBlank(false);

        _path.setName("path");
        _path.setValue(folder);

        _form.add(_fileName);
        _form.add(_title);
        _form.add(_description);
        _form.add(_file);
        _form.add(_path);

        _form.addButton(new Button(
        constants().cancel(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    close();
                }
            }
        ));

        _form.addButton(new Button(
            _constants.upload(),
            new SelectionListener<ButtonEvent>() {
                @Override
                public void componentSelected(final ButtonEvent ce) {
                    if (!_form.isValid()) {
                        return;
                    }
                    _form.submit();
                    hide();
                    rt.refreshTable();
                }
            }
        ));
        add(_form);
    }


//    /**
//     * Takes care of the validation.
//     *
//     * @author Civic Computing Ltd
//     */
//    private final class FileUploadFormHandler implements FormHandler {
//
//        public void onSubmit(final FormSubmitEvent event) {
//            final StringBuffer errorText = new StringBuffer();
//            if (_fileName.getFileName().length() == 0) {
//                errorText.append(_constants.fileName());
//                errorText.append("\n");
//            }
//            if (_description.getText().length() == 0) {
//                errorText.append(_constants.description());
//                errorText.append("\n");
//            }
//            if (_title.getText().length() == 0) {
//                errorText.append(_constants.title());
//                errorText.append("\n");
//            }
//            if (_upload.getFilename() == null
//                    || _upload.getFilename().length() == 0) {
//                errorText.append(_constants.file());
//                errorText.append("\n");
//            }
//            if (errorText.length() > 0) {
//                Globals.alert("Following fields must not be empty: \n"
//                    +errorText.toString());
//                event.setCancelled(true);
//            }
//        }
//
//        public void onSubmitComplete(final FormSubmitCompleteEvent event) {
//            Globals.alert(event.getResults());
//            _tree.fireEvent(Events.SelectionChange);
//            hide();
//        }
//    }
}
