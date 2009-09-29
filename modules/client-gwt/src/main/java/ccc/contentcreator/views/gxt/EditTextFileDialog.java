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
import ccc.contentcreator.dialogs.AbstractEditDialog;
import ccc.contentcreator.views.EditTextFile;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextArea;


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

    private final TextArea _text = new TextArea();
    private Editable _presenter;
    private static final int TEXT_AREA_HEIGHT = 400;

    /**
     * Constructor.
     *
     */
    public EditTextFileDialog() {

        super(new IGlobalsImpl().uiConstants().edit(), new IGlobalsImpl());
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
    public void setText(final String text) {
        _text.setValue(text);
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
}
