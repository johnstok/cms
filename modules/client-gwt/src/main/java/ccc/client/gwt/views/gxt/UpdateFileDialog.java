/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import ccc.api.core.ResourceSummary;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.ImagePaths;
import ccc.client.core.SessionTimeoutException;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.RemoteException;
import ccc.client.gwt.overlays.FailureOverlay;
import ccc.client.gwt.validation.Validate;
import ccc.client.gwt.validation.Validations;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.plugins.s11n.JsonKeys;

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
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
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


    /**
     * Constructor.
     *
     * @param file The file to update.
     */
    public UpdateFileDialog(final ResourceSummary file) {
        super(I18n.UI_CONSTANTS.updateFile(),
              new GlobalsImpl());
        setHeight(Globals.DEFAULT_UPLOAD_HEIGHT);
        // Create a FormPanel and point it at a service.
        getPanel().setAction(Globals.API_URL + file.selfBinary());

        getPanel().setEncoding(FormPanel.Encoding.MULTIPART);
        getPanel().setMethod(FormPanel.Method.POST);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(getUiConstants().localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _id.setName("id");
        _id.setValue(file.getId().toString());
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
                    final String response = be.getResultHtml();

                    if (
                        SessionTimeoutException.isTimedout(response)) {
                        ContentCreator.EX_HANDLER.unexpectedError(
                            new SessionTimeoutException(be.getResultHtml()),
                            getUiConstants().updateFile());

                    } else {
                        final JSONObject o =
                            JSONParser.parse(response).isObject();

                        if (o.containsKey(JsonKeys.CODE)) { // Error
                            ContentCreator.EX_HANDLER.unexpectedError(
                                new RemoteException(
                                    FailureOverlay.fromJson(response)),
                                getUiConstants().uploadFile());
                        } else {
                            hide();
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
