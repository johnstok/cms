/*-----------------------------------------------------------------------------
EditTextFileDialog.java * Copyright (c) 2009 Civic Computing Ltd.
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

import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ValidationResult;
import ccc.contentcreator.dialogs.AbstractEditDialog;
import ccc.contentcreator.validation.Validations2;
import ccc.contentcreator.views.CreateTextFile;

import com.extjs.gxt.ui.client.event.ButtonEvent;
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
        CreateTextFile{

    private final TextField<String>   _fileName = new TextField<String>();
    private final TextField<String>   _mimeSubType = new TextField<String>();
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();
    private final TextArea _text = new TextArea();

    private Editable _presenter;
    private static final int TEXT_AREA_HEIGHT = 230;

    /**
     * Constructor.
     *
     */
    public CreateTextFileDialog() {

        super(new IGlobalsImpl().uiConstants().createTextFile(),
            new IGlobalsImpl());

        _fileName.setName("fileName");
        _fileName.setFieldLabel(getUiConstants().fileName());
        _fileName.setAllowBlank(false);
        addField(_fileName);

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

        _text.setFieldLabel(getUiConstants().content());
        _text.setHeight(TEXT_AREA_HEIGHT);
        addField(_text);
    }

    /** {@inheritDoc} */
    @Override
    public String getText() {
        return _text.getValue();
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
    public void setPresenter(final Editable presenter) {
        _presenter = presenter;
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
        if (!Validations2.notEmpty(_mimeSubType.getValue())) {
            result.addError(
                _mimeSubType.getFieldLabel()+getUiConstants().cannotBeEmpty());
        }
        if (!Validations2.notEmpty(_text.getValue())) {
            result.addError(
                _text.getFieldLabel()+getUiConstants().cannotBeEmpty());
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
    public String getSubMime() {
        return _mimeSubType.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMajorEdit() {
        return _majorEdit.getValue().booleanValue();
    }

}
