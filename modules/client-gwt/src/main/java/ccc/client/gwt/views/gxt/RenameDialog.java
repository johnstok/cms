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

import static ccc.client.core.InternalServices.validator;
import ccc.client.core.Editable;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.core.ValidationResult;
import ccc.client.views.RenameResource;

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
        super(I18n.uiConstants.rename(), InternalServices.globals);
        setHeight(Globals.DEFAULT_MIN_HEIGHT);

        _oldName.setFieldLabel(constants().originalName());
        _oldName.setReadOnly(true);
        _oldName.disable();

        _newName.setFieldLabel(constants().newName());
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

        result.addError(
            validator.notEmpty(
                _newName.getValue(), _newName.getFieldLabel()));
        result.addError(
            validator.notValidResourceName(
                _newName.getValue(), _newName.getFieldLabel()));

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        super.show();
    }
}
