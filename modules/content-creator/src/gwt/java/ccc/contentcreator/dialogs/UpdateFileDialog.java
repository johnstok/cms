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
import com.extjs.gxt.ui.client.widget.button.Button;
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
public class UpdateFileDialog extends AbstractBaseDialog {

    private final UIConstants       _constants = Globals.uiConstants();

    private final TextField<String> _title = new TextField<String>();
    private final TextField<String> _description = new TextField<String>();
    private final HiddenField<String> _id = new HiddenField<String>();
    private FileUploadField _file = new FileUploadField();
    private final FormPanel _form = new FormPanel();
    private final Image _image =
        new Image("images/gxt/shared/large-loading.gif");

    /**
     * Constructor.
     *
     * @param rt The left hand tree view in the main window.
     */
    public UpdateFileDialog(final FileDelta delta,
                            final ResourceTable rt) {
        super(Globals.uiConstants().updateFile());

        // Create a FormPanel and point it at a service.
        _form.setAction("update_file");
        _form.setEncoding(FormPanel.Encoding.MULTIPART);
        _form.setMethod(FormPanel.Method.POST);
        _form.setHeaderVisible(false);

        _title.setName("title");
        _title.setValue(delta._title);
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);

        _description.setName("description");
        _description.setValue(delta._description);
        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);

        _file.setName("file");
        _file.setFieldLabel(_constants.localFile());
        _file.setAllowBlank(false);

        _id.setName("id");
        _id.setValue(delta._id);

        _form.add(_id);
        _form.add(_title);
        _form.add(_description);
        _form.add(_file);

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
                if (!be.resultHtml.equals("File was updated successfully.")) {
                    Globals.unexpectedError(new Exception(be.resultHtml));
                }
            }
        });

        add(_form);
    }
}
