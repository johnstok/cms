/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
import ccc.contentcreator.views.EditTextFile;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.weborient.codemirror.client.CodeMirrorConfiguration;
import com.weborient.codemirror.client.CodeMirrorEditorWidget;


/**
 * Dialog for text file content editing.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileDialog
    extends
        AbstractEditDialog
    implements
        EditTextFile{

    private Editable _presenter;
    private static final int DIALOG_HEIGHT = 580;
    private CodeMirrorEditorWidget _text = new CodeMirrorEditorWidget();
//    private final TextArea _text = new TextArea();
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();

    /**
     * Constructor.
     *
     */
    public EditTextFileDialog() {

        super(new IGlobalsImpl().uiConstants().edit(), new IGlobalsImpl());
        final CodeMirrorConfiguration configuration =
            new CodeMirrorConfiguration();

        setHeight(DIALOG_HEIGHT);

        _text = new CodeMirrorEditorWidget(configuration);
        _text.getToolBar().removeFromParent(); // remove toolbar

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.TRUE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());
        addField(_majorEdit);

        _comment.setFieldLabel(getUiConstants().comment());
        _comment.setName("comment");
        addField(_comment);

//        _text.setFieldLabel(getUiConstants().content());
//        _text.setHeight(TEXT_AREA_HEIGHT);
        addField(_text);
    }

    /** {@inheritDoc} */
    @Override
    public String getText() {
        return _text.getText();
//        return "";
    }

    /** {@inheritDoc} */
    @Override
    public void setText(final String text) {
        _text.setText(text);
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
        if (!Validations2.notEmpty(_text.getText())) {
            result.addError(
                getUiConstants().content()+getUiConstants().cannotBeEmpty());
        }
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
}
