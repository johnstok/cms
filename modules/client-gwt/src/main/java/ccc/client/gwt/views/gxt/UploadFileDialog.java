/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import static ccc.client.core.InternalServices.*;
import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.ImagePaths;
import ccc.client.core.InternalServices;
import ccc.client.core.RemoteException;
import ccc.client.core.S11nHelper;
import ccc.client.core.SessionTimeoutException;
import ccc.client.core.SingleSelectionModel;
import ccc.client.core.ValidationResult;
import ccc.plugins.s11n.S11nException;

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

    private final TextField<String>   _title = new TextField<String>();
    private final ResourceSummary     _parent;
    private final S11nHelper          _s11n      = new S11nHelper();
    private final TextField<String>   _fileName  = new TextField<String>();
    private final HiddenField<String> _path      = new HiddenField<String>();
    private final FileUploadField     _file      = new FileUploadField();

    private final CheckBox _majorEdit = new CheckBox();
    private final CheckBox _publish   = new CheckBox();
    private final TextArea _comment = new TextArea();

    private final Image _image =
        new Image(ImagePaths.LARGE_LOADING);

    /**
     * Constructor.
     *
     * @param parentFolder The folder in which this file should be saved.
     * @param ssm The selection model.
     */
    public UploadFileDialog(final ResourceSummary parentFolder,
                            final SingleSelectionModel ssm) {
        super(I18n.uiConstants.uploadFileTo()
            +": "+parentFolder.getName(), InternalServices.globals);

        _parent = parentFolder;
        setHeight(Globals.DEFAULT_UPLOAD_HEIGHT);

        // Create a FormPanel and point it at a service.
        getPanel().setAction(
            Globals.API_URL
            + InternalServices.api.getLink(File.Links.LIST_BINARY));
        getPanel().setEncoding(FormPanel.Encoding.MULTIPART);
        getPanel().setMethod(FormPanel.Method.POST);

        _file.setName("file");
        _file.setWidth("100%");
        _file.setFieldLabel(getUiConstants().localFile());
        _file.setAllowBlank(false);
        addField(_file);

        _title.setName("title");
        _title.setFieldLabel(getUiConstants().title());
        _title.setAllowBlank(true);
        addField(_title);

        _path.setName("path");
        _path.setValue(_parent.getId().toString());
        addField(_path);

        _publish.setName("publish");
        _publish.setValue(Boolean.FALSE);
        _publish.setBoxLabel(getUiConstants().yes());
        _publish.setFieldLabel(getUiConstants().publish());
        _publish.setValueAttribute("true");
        addField(_publish);

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
                    _image.setVisible(false);

                    final String response = be.getResultHtml();

                    // TODO: Handle 404 with notfound.jsp

                    if (SessionTimeoutException.isTimedout(response)) {
                        InternalServices.exHandler.unexpectedError(
                            new SessionTimeoutException(be.getResultHtml()),
                            getUiConstants().uploadFile());

                    } else {
                        final JSONObject o =
                            JSONParser.parse(be.getResultHtml()).isObject();

                        // Error.
                        try {
                            InternalServices.exHandler.unexpectedError(
                                new RemoteException(
                                    _s11n.readFailure(response)),
                                getUiConstants().uploadFile());

                        // Assume success.
                        } catch (final S11nException e) {
                            hide();
                            ssm.create(
                                _s11n.readFile(be.getResultHtml()));
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

                final ValidationResult vr = new ValidationResult();
                vr.addError(
                    validator.notEmpty(
                        _title.getValue(), _title.getFieldLabel()));
                vr.addError(
                    validator.notValidResourceName(
                        _fileName.getValue(), _fileName.getFieldLabel()));
                vr.addError(
                    validator.noBrackets(
                        _title.getValue(), _title.getFieldLabel()));


                if (!vr.isValid()) {
                    InternalServices.window.alert(vr.getErrorText());
                    return;
                }

                submit();
            }
        };
    }


    private void submit() {
        _image.setVisible(true);
        getPanel().submit();
    }
}
