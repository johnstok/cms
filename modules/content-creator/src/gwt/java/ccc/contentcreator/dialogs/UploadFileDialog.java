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
import ccc.contentcreator.client.GxtResourceSummary;
import ccc.contentcreator.client.SingleSelectionModel;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Image;


/**
 * Dialog for file upload.
 *
 * @author Civic Computing Ltd.
 */
public class UploadFileDialog extends AbstractEditDialog {

    private final UIConstants       _constants = Globals.uiConstants();

    private final TextField<String>   _fileName = new TextField<String>();
    private final TextField<String>   _title = new TextField<String>();
    private final TextField<String>   _description = new TextField<String>();
    private final HiddenField<String> _path = new HiddenField<String>();
    private FileUploadField           _file = new FileUploadField();

    private final Image _image =
        new Image("images/gxt/shared/large-loading.gif");

    /**
     * Constructor.
     *
     * @param parentFolder The folder in which this file should be saved.
     * @param ssm
     */
    public UploadFileDialog(final ModelData parentFolder,
                            final SingleSelectionModel ssm) {
        super(
            Globals.uiConstants().uploadFileTo()+": "+parentFolder.get("name"));
        setHeight(Globals.DEFAULT_UPLOAD_HEIGHT);

        // Create a FormPanel and point it at a service.
        _panel.setAction("upload");
        _panel.setEncoding(FormPanel.Encoding.MULTIPART);
        _panel.setMethod(FormPanel.Method.POST);

        _fileName.setName("fileName");
        _fileName.setFieldLabel(_constants.fileName());
        _fileName.setAllowBlank(false);
        addField(_fileName);

        _title.setName("title");
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        addField(_title);

        _description.setName("description");
        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        addField(_description);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(_constants.localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _path.setName("path");
        _path.setValue(parentFolder.<String>get("id"));
        addField(_path);

        _image.setVisible(false);
        _panel.add(_image);

        _panel.addListener(
            Events.Submit,
            new Listener<FormEvent>() {
                public void handleEvent(final FormEvent be) {
                    hide();
                    if (be.resultHtml.startsWith("File Upload failed.")) {
                      Globals.unexpectedError(new Exception(be.resultHtml));
                    } else {
                        ssm.create(
                            new GxtResourceSummary(
                                JSONParser.parse(be.resultHtml)), parentFolder);
                    }
                }
            }
        );
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(final ButtonEvent ce) {
                if (!_panel.isValid()) {
                    return;
                }
                _image.setVisible(true);
                _panel.submit();
            }
        };
    }
}
