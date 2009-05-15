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

import ccc.api.FileDelta;
import ccc.api.ID;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

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
                            final ID fileId,
                            final SingleSelectionModel rt) {
        super(Globals.uiConstants().updateFile());
        setHeight(Globals.DEFAULT_UPLOAD_HEIGHT);
        // Create a FormPanel and point it at a service.
        _panel.setAction("update_file");
        _panel.setEncoding(FormPanel.Encoding.MULTIPART);
        _panel.setMethod(FormPanel.Method.POST);

        _title.setName("title");
        _title.setValue(delta.getTitle());
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        addField(_title);

        _description.setName("description");
        _description.setValue(delta.getDescription());
        _description.setFieldLabel(_constants.description());
        _description.setAllowBlank(false);
        addField(_description);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(_constants.localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _id.setName("id");
        _id.setValue(fileId.toString());
        addField(_id);

        _image.setVisible(false);
        _panel.add(_image);

        _panel.addListener(
            Events.Submit,
            new Listener<FormEvent>() {
                public void handleEvent(final FormEvent be) {
                    hide();
                    if (!be.resultHtml.equals("File was updated successfully.")) {
                        Globals.unexpectedError(new Exception(be.resultHtml));
                    } else {
                        final ResourceSummaryModelData md = rt.tableSelection();
                        md.setTitle(_title.getValue());
                        rt.update(md);
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
                Validate.callTo(submit())
                .check(Validations.notEmpty(_title))
                .check(Validations.noBrackets(_title))
                .check(Validations.notEmpty(_description))
                .check(Validations.noBrackets(_description))
                .callMethodOr(Validations.reportErrors());

            }
        };
    }

    private Runnable submit() {
        return new Runnable() {
            public void run() {
                _image.setVisible(true);
                _panel.submit();
            }
        };
    }
}
