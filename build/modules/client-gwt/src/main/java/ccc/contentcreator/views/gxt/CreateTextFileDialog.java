/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views.gxt;

import ccc.contentcreator.client.CodeMirrorEditor;
import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.Event;
import ccc.contentcreator.client.EventBus;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ValidationResult;
import ccc.contentcreator.client.Event.Type;
import ccc.contentcreator.dialogs.AbstractEditDialog;
import ccc.contentcreator.events.CMEditorReadyEvent;
import ccc.contentcreator.validation.Validations2;
import ccc.contentcreator.views.CreateTextFile;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for creating a text file.
 *
 * @author Civic Computing Ltd.
 */
public class CreateTextFileDialog
    extends
        AbstractEditDialog
    implements
        CreateTextFile, EventBus {

    private final TextField<String> _fileName = new TextField<String>();
    private final TextField<String> _mimePrimaryType = new TextField<String>();
    private final TextField<String> _mimeSubType = new TextField<String>();

    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();

    private CodeMirrorEditor _cme;

    private Editable _presenter;
    private static final int DIALOG_HEIGHT = 650;
    /** TEXT_AREA_HEIGHT : int. */
    protected static final int TEXT_AREA_HEIGHT = 300;

    /**
     * Constructor.
     *
     */
    public CreateTextFileDialog() {

        super(new IGlobalsImpl().uiConstants().createTextFile(),
            new IGlobalsImpl());

        setHeight(DIALOG_HEIGHT);

        _fileName.setName("fileName");
        _fileName.setFieldLabel(getUiConstants().fileName());
        _fileName.setAllowBlank(false);
        addField(_fileName);

        _mimePrimaryType.setName("mimePrimaryType");
        _mimePrimaryType.setFieldLabel(getUiConstants().mimePrimaryType());
        _mimePrimaryType.setAllowBlank(false);
        _mimePrimaryType.setValue("text");
        addField(_mimePrimaryType);

        _mimeSubType.setName("fileMimeSubType");
        _mimeSubType.setFieldLabel(getUiConstants().mimeSubType());
        _mimeSubType.setAllowBlank(false);
        addField(_mimeSubType);

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.TRUE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());
        addField(_majorEdit);

        _comment.setFieldLabel(getUiConstants().comment());
        _comment.setName("comment");
        addField(_comment);

        _cme = new CodeMirrorEditor(
            "textEditEditorID",
            this,
            CodeMirrorEditor.Type.TEXT);
        addField(_cme.parserSelector(getUiConstants()));
        addField(_cme);

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                final int eheight =
                    be.getHeight()-(DIALOG_HEIGHT - TEXT_AREA_HEIGHT);
                if (eheight > (DIALOG_HEIGHT - TEXT_AREA_HEIGHT)) {
                    _cme.setEditorHeight(eheight+"px");
                }
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public String getText() {
        return _cme.getEditorCode();
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                getPresenter().save();
            }
        };
    }

    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }

    /**
     * Accessor.
     *
     * @return Returns the presenter.
     */
    Editable getPresenter() {
        return _presenter;
    }

    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();

        if (!Validations2.notEmpty(_fileName.getValue())) {
            result.addError(
                _fileName.getFieldLabel()+getUiConstants().cannotBeEmpty());
        } else if (!Validations2.notValidResourceName(_fileName.getValue())) {
            result.addError(getUiConstants().resourceNameIsInvalid());
        }
        if (!Validations2.notEmpty(_mimePrimaryType.getValue())) {
            result.addError(
                _mimePrimaryType.getFieldLabel()
                +getUiConstants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(_mimeSubType.getValue())) {
            result.addError(
                _mimeSubType.getFieldLabel()+getUiConstants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(_cme.getEditorCode())) {
            result.addError(
                getUiConstants().content()+getUiConstants().cannotBeEmpty());
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return _fileName.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getComment() {
        return _comment.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getPrimaryMime() {
        return _mimePrimaryType.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public String getSubMime() {
        return _mimeSubType.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMajorEdit() {
        return _majorEdit.getValue().booleanValue();
    }

    /** {@inheritDoc} */
    @Override
    public void put(final Event event) {
        if (Type.CM_EDITOR_READY==event.getType()) {
            ((CMEditorReadyEvent) event).getCodeMirrorEditor();
        }
    }
}
