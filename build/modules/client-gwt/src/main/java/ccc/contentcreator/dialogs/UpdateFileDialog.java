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

import java.util.UUID;

import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ImagePaths;
import ccc.contentcreator.client.RemoteException;
import ccc.contentcreator.client.SessionTimeoutException;
import ccc.contentcreator.overlays.FailureOverlay;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
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
        new Image(ImagePaths.LARGE_LOADING);

    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();

    private static final String UPDATE_OK = "NULL";
    /**
     * Constructor.
     *
     * @param fileId The {@link UUID} of the file.
     */
    public UpdateFileDialog(final UUID fileId) {
        super(new IGlobalsImpl().uiConstants().updateFile(),
              new IGlobalsImpl());
        setHeight(IGlobals.DEFAULT_UPLOAD_HEIGHT);
        // Create a FormPanel and point it at a service.
        getPanel().setAction("update_file");
        getPanel().setEncoding(FormPanel.Encoding.MULTIPART);
        getPanel().setMethod(FormPanel.Method.POST);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(getUiConstants().localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _id.setName("id");
        _id.setValue(fileId.toString());
        addField(_id);

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.FALSE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());
        addField(_majorEdit);

        _comment.setFieldLabel(getUiConstants().comment());
        _comment.setName("comment");
        addField(_comment);

        _image.setVisible(false);
        getPanel().add(_image);

        getPanel().addListener(
            Events.Submit,
            new Listener<FormEvent>() {
                public void handleEvent(final FormEvent be) {
                    if (be.getResultHtml().equals(UPDATE_OK)) {
                        hide();
                    } else if (
                        SessionTimeoutException.isTimeoutMessage(
                            be.getResultHtml())) {
                        getGlobals().unexpectedError(
                            new SessionTimeoutException(be.getResultHtml()),
                            getUiConstants().updateFile());
                    } else {
                        getGlobals().unexpectedError(
                            new RemoteException(
                                FailureOverlay.fromJson(be.getResultHtml())),
                                getUiConstants().updateFile());
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
                if (!getPanel().isValid()) {
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
                getPanel().submit();
            }
        };
    }
}
