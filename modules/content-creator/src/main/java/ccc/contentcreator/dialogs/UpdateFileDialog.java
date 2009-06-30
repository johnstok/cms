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

import ccc.api.ID;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.IGlobals;
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
import com.google.gwt.user.client.ui.Image;


/**
 * Dialog for file update.
 *
 * @author Civic Computing Ltd.
 */
public class UpdateFileDialog extends AbstractEditDialog {

    private final HiddenField<String> _id = new HiddenField<String>();
    private final FileUploadField           _file = new FileUploadField();
    private final Image _image =
        new Image("images/gxt/shared/large-loading.gif");

    private static final String UPDATE_OK = "File was updated successfully.";
    /**
     * Constructor.
     *
     * @param fileId The {@link ID} of the file.
     */
    public UpdateFileDialog(final ID fileId) {
        super(Globals.uiConstants().updateFile());
        setHeight(IGlobals.DEFAULT_UPLOAD_HEIGHT);
        // Create a FormPanel and point it at a service.
        _panel.setAction("update_file");
        _panel.setEncoding(FormPanel.Encoding.MULTIPART);
        _panel.setMethod(FormPanel.Method.POST);

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
                    if (!be.resultHtml.equals(UPDATE_OK)
                       || be.resultHtml.startsWith("<!-- LOGIN_REQUIRED -->")) {
                        Globals.unexpectedError(
                            new Exception(be.resultHtml),
                            _constants.updateFile());
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
