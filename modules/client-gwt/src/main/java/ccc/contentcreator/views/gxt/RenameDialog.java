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
package ccc.contentcreator.views.gxt;

import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.contentcreator.client.ValidationResult;
import ccc.contentcreator.dialogs.AbstractEditDialog;
import ccc.contentcreator.validation.Validations2;
import ccc.contentcreator.views.RenameResource;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * MVP view for renaming a resource.
 *
 * @author Civic Computing Ltd.
 */
public class RenameDialog
    extends
        AbstractEditDialog
    implements
        RenameResource {

    private final TextField<String> _oldName = new TextField<String>();
    private final TextField<String> _newName = new TextField<String>();

    private Editable _presenter;


    /**
     * Constructor.
     */
    public RenameDialog() {
        super(new IGlobalsImpl().uiConstants().rename(), new IGlobalsImpl());
        setHeight(IGlobals.DEFAULT_MIN_HEIGHT);

        setPanelId("RenamePanel");

        _oldName.setFieldLabel(constants().originalName());
        _oldName.setId("originalName");
        _oldName.setReadOnly(true);
        _oldName.disable();

        _newName.setFieldLabel(constants().newName());
        _newName.setId("newName");
        _newName.setAllowBlank(false);

        addField(_oldName);
        addField(_newName);
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
    public String getName() {
        return _newName.getValue();
    }


    /** {@inheritDoc} */
    @Override
    public void setName(final String name) {
        _newName.setValue(name);
        _oldName.setValue(name);
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult result = new ValidationResult();

        if (!Validations2.notEmpty(_newName.getValue())) {
            result.addError(
                _newName.getFieldLabel()+getUiConstants().cannotBeEmpty());
        } else if (!Validations2.notValidResourceName(_newName.getValue())) {
            result.addError(getUiConstants().resourceNameIsInvalid());
        }
//          && Validations2.uniqueResourceName(_item.getParent(), _newName))
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }
}
