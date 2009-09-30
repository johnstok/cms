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
import ccc.contentcreator.dialogs.AbstractEditDialog;
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
    private final TextField<String>   _mimeExt = new TextField<String>();
    private final CheckBox _majorEdit = new CheckBox();
    private final TextArea _comment = new TextArea();
    private final TextArea _text = new TextArea();

    private Editable _presenter;
    private static final int TEXT_AREA_HEIGHT = 400;

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

        _mimeExt.setName("fileMimeSubType");
        _mimeExt.setFieldLabel(getUiConstants().mimeSubType());
        _mimeExt.setAllowBlank(false);
        addField(_mimeExt);

        _majorEdit.setName("majorEdit");
        _majorEdit.setValue(Boolean.TRUE);
        _majorEdit.setBoxLabel(getUiConstants().yes());
        _majorEdit.setFieldLabel(getUiConstants().majorEdit());
        addField(_majorEdit);

        _comment.setFieldLabel(getUiConstants().comment());
        _comment.setName("comment");
        addField(_comment);

        _text.setHeight(TEXT_AREA_HEIGHT);
        _text.setHideLabel(true);
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
    public boolean isValid() {
        return true;
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
    public String getMime() {
        return _mimeExt.getValue();
    }

    /** {@inheritDoc} */
    @Override
    public boolean majorEdit() {
        return _majorEdit.getValue();
    }

}
