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

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.ui.Image;


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
    private final Image _image =
        new Image("images/gxt/shared/large-loading.gif");

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
        _form.setBorders(false);
        _form.setBodyBorder(false);
        _form.setHeaderVisible(false);

        _fileName.setName("fileName");
        _fileName.setFieldLabel(_constants.fileName());
        _fileName.setAllowBlank(false);
        _form.add(_fileName, new FormData("95%"));

        _title.setName("title");
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        _form.add(_title, new FormData("95%"));

        _description.setName("description");
        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        _form.add(_description, new FormData("95%"));

        _file.setName("file");
        _file.setFieldLabel(_constants.localFile());
        _file.setAllowBlank(false);
        _form.add(_file, new FormData("95%"));

        _path.setName("path");
        _path.setValue(folder);
        _form.add(_path, new FormData("95%"));

        _image.setVisible(false);
        _form.add(_image);

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
                    _image.setVisible(true);
                    _form.submit();

                }
            }
        ));

        _form.addListener(Events.Submit, new Listener<FormEvent>() {
            public void handleEvent(final FormEvent be) {
                hide();
                rt.refreshTable();
                if (!be.resultHtml.equals("File was uploaded successfully.")) {
                    Globals.unexpectedError(new Exception(be.resultHtml));
                }
            }
        });

        add(_form);
    }
}
