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
import ccc.services.api.FileDelta;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.ui.Image;


/**
 * Dialog for file update.
 * TODO: Remove code duplicated from File upload form.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileDialog extends AbstractEditDialog {

    private final UIConstants       _constants = Globals.uiConstants();

    private final TextField<String>   _title = new TextField<String>();
    private final TextField<String>   _description = new TextField<String>();
    private final HiddenField<String> _id = new HiddenField<String>();
    private FileUploadField           _file = new FileUploadField();
    private final Image _image =
        new Image("images/gxt/shared/large-loading.gif");

    /**
     * Constructor.
     *
     * @param delta FileDelta of the file to be updated.
     * @param rt The left hand tree view in the main window.
     */
    public UpdateFileDialog(final FileDelta delta,
                            final ResourceTable rt) {
        super(Globals.uiConstants().updateFile());
        setHeight(Globals.DEFAULT_UPLOAD_HEIGHT);
        // Create a FormPanel and point it at a service.
        _panel.setAction("update_file");
        _panel.setEncoding(FormPanel.Encoding.MULTIPART);
        _panel.setMethod(FormPanel.Method.POST);

        _title.setName("title");
        _title.setValue(delta._title);
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        addField(_title);

        _description.setName("description");
        _description.setValue(delta._description);
        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        addField(_description);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(_constants.localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _id.setName("id");
        _id.setValue(delta._id);
        addField(_id);

        _image.setVisible(false);
        _panel.add(_image);

        _panel.addListener(Events.Submit, new Listener<FormEvent>() {
            public void handleEvent(final FormEvent be) {
                hide();
                rt.update(null); // FIXME
                if (!be.resultHtml.equals("File was updated successfully.")) {
                    Globals.unexpectedError(new Exception(be.resultHtml));
                }
            }
        });
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
