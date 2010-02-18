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
package ccc.contentcreator.views.gxt;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.ImagePaths;
import ccc.contentcreator.client.RemoteException;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.core.GwtJson;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.core.SessionTimeoutException;
import ccc.contentcreator.overlays.FailureOverlay;
import ccc.contentcreator.validation.Validate;
import ccc.contentcreator.validation.Validations;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.JsonKeys;

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
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.ui.Image;


/**
 * Dialog for file upload.
 *
 * @author Civic Computing Ltd.
 */
public class UploadFileDialog extends AbstractEditDialog {

    private final TextField<String>   _fileName = new TextField<String>();
    private final ResourceSummaryModelData _parent;
    private final HiddenField<String> _path = new HiddenField<String>();
    private final FileUploadField           _file = new FileUploadField();

    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();

    private final Image _image =
        new Image(ImagePaths.LARGE_LOADING);

    /**
     * Constructor.
     *
     * @param parentFolder The folder in which this file should be saved.
     * @param ssm The selection model.
     */
    public UploadFileDialog(final ResourceSummaryModelData parentFolder,
                            final SingleSelectionModel ssm) {
        super(new IGlobalsImpl().uiConstants().uploadFileTo()
            +": "+parentFolder.getName(), new IGlobalsImpl());

        _parent = parentFolder;
        setHeight(IGlobals.DEFAULT_UPLOAD_HEIGHT);

        // Create a FormPanel and point it at a service.
        getPanel().setAction("upload");
        getPanel().setEncoding(FormPanel.Encoding.MULTIPART);
        getPanel().setMethod(FormPanel.Method.POST);

        _fileName.setName("fileName");
        _fileName.setFieldLabel(getUiConstants().fileName());
        _fileName.setAllowBlank(false);
        addField(_fileName);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(getUiConstants().localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _path.setName("path");
        _path.setValue(_parent.getId().toString());
        addField(_path);

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.TRUE);
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
                    if (SessionTimeoutException.isTimeoutMessage(
                            be.getResultHtml())) {
                        getGlobals().unexpectedError(
                            new SessionTimeoutException(be.getResultHtml()),
                            getUiConstants().uploadFile());
                    } else {

                        final JSONObject o =
                            JSONParser.parse(be.getResultHtml()).isObject();

                        if (o.containsKey(JsonKeys.CODE)) { // CommandFailedEx
                            getGlobals().unexpectedError(
                                new RemoteException(
                                    FailureOverlay.fromJson(
                                        be.getResultHtml())),
                                        getUiConstants().uploadFile());
                        } else {
                            hide();
                            final JSONObject json =
                                JSONParser.parse(be.getResultHtml()).isObject();
                            final ResourceSummary rs =
                                new ResourceSummary(new GwtJson(json));
                            ssm.create(
                                new ResourceSummaryModelData(rs), _parent);
                        }
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
                .check(Validations.notEmpty(_fileName))
                .check(Validations.notValidResourceName(_fileName))
                .stopIfInError()
                    .check(Validations.uniqueResourceName(_parent, _fileName))
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
