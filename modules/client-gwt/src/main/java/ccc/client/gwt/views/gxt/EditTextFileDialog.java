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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import static ccc.client.core.InternalServices.*;
import ccc.client.core.Editable;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.widgets.CodeMirrorEditor;
import ccc.client.gwt.widgets.CodeMirrorEditor.EditorListener;
import ccc.client.gwt.widgets.CodeMirrorEditor.Type;
import ccc.client.views.EditTextFile;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Dialog for text file content editing.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileDialog
    extends
        AbstractEditDialog
    implements
        EditTextFile,
        EditorListener {

    private Editable _presenter;
    private static final int DIALOG_HEIGHT = 620;
    /** TEXT_AREA_HEIGHT : int. */
    protected static final int TEXT_AREA_HEIGHT = 300;

    private final CodeMirrorEditor _cme;
    private String _text;
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();
    private final TextField<String> _mimePrimaryType = new TextField<String>();
    private final TextField<String> _mimeSubType = new TextField<String>();

    /**
     * Constructor.
     *
     */
    public EditTextFileDialog() {

        super(I18n.uiConstants.edit(), InternalServices.globals);

        setHeight(DIALOG_HEIGHT);

        _mimePrimaryType.setName("mimePrimaryType");
        _mimePrimaryType.setFieldLabel(getUiConstants().mimePrimaryType());
        _mimePrimaryType.setAllowBlank(false);
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

        _cme = new CodeMirrorEditor("textEditEditorID",
            this,
            CodeMirrorEditor.Type.TEXT,
            false);
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
    public void setText(final String text) {
        _text = text;
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

        result.addError(
            validator.notEmpty(
                _mimePrimaryType.getValue(), _mimePrimaryType.getFieldLabel()));
        result.addError(
            validator.notEmpty(
                _mimeSubType.getValue(), _mimeSubType.getFieldLabel()));
        result.addError(
            validator.notEmpty(
                _cme.getEditorCode(), getUiConstants().content()));

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String getComment() {
        return _comment.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMajorEdit() {
        return _majorEdit.getValue().booleanValue();
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
    public void setPrimaryMime(final String primary) {
        _mimePrimaryType.setValue(primary);
    }

    /** {@inheritDoc} */
    @Override
    public void setSubMime(final String sub) {
        _mimeSubType.setValue(sub);
    }


    /** {@inheritDoc} */
    @Override
    public void onInitialized(final Type type, final CodeMirrorEditor editor) {
        if (_text != null) {
            editor.setEditorCode(_text);
        }
    }
}
