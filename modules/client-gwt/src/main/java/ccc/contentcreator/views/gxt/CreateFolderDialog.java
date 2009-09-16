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

import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.dialogs.AbstractEditDialog;
import ccc.contentcreator.validation.Validations2;
import ccc.contentcreator.views.CreateFolder;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;


/**
 * Dialog for folder creation.
 *
 * @author Civic Computing Ltd.
 */
public class CreateFolderDialog
    extends
        AbstractEditDialog
    implements
        CreateFolder {

    private final TextField<String> _text = new TextField<String>();
    private Editable _presenter;


    /**
     * Constructor.
     */
    public CreateFolderDialog() {
        super(new IGlobalsImpl().uiConstants().createFolder(),
              new IGlobalsImpl());

        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);
        setLayout(new FitLayout());
        setPanelId("create-folder-dialog");

        _text.setId("folder-name");
        _text.setFieldLabel(constants().name());
        _text.setEmptyText(constants().theFolderName());
        _text.setAllowBlank(false);
        addField(_text);
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
    public String getName() {
        return _text.getValue();
    }


    /** {@inheritDoc} */
    @Override
    public void setName(final String name) {
        _text.setValue(name);
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
    public void setPresenter(final Editable presenter) {
        _presenter = presenter;
    }


    /** {@inheritDoc} */
    @Override
    public boolean isValid() {
        return
            Validations2.notEmpty(_text.getValue())
            && Validations2.notValidResourceName(_text.getValue());
//          && Validations2.uniqueResourceName(_item.getParent(), _newName))
    }
}
